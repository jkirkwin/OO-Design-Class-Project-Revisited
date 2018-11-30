package ca.uvic.seng330.assn3.model;

import ca.uvic.seng330.assn3.controller.DeviceType;
import ca.uvic.seng330.assn3.logging.Logging;
import ca.uvic.seng330.assn3.model.devices.*;
import ca.uvic.seng330.assn3.model.storage.Storage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Stack;
import java.util.UUID;
import org.json.JSONObject;
import org.slf4j.event.Level;

public class Hub {

  private final HashMap<UUID, Device> deviceRegistry;
  private HashMap<UUID, UserAccount> userAccountRegistry;
  private HashMap<UUID, Room> roomRegistry;

  public Hub() {
    this.deviceRegistry = new HashMap<UUID, Device>();
    this.userAccountRegistry = new HashMap<UUID, UserAccount>();
    this.roomRegistry = new HashMap<UUID, Room>();
  }

  /*
   * @pre newDevice != null
   */
  public void register(Device newDevice) throws HubRegistrationException {
    assert newDevice != null;
    if (!deviceRegistry.containsKey(newDevice.getIdentifier())) {
      deviceRegistry.put(newDevice.getIdentifier(), newDevice);
      Logging.logWithID("Device registered", newDevice.getIdentifier(), Level.INFO);
      notification(newDevice.getLabel() + " registered", newDevice.getIdentifier());
    } else {
      throw new HubRegistrationException("Device with matching UUID previously registered.");
    }
  }

  /*
   * @pre newAccount != null
   */
  public void register(UserAccount newAccount) throws HubRegistrationException {
    assert newAccount != null;
    if (!userAccountRegistry.containsKey(newAccount.getIdentifier())) {
      userAccountRegistry.put(newAccount.getIdentifier(), newAccount);
      Logging.logWithID("Account registered", newAccount.getIdentifier(), Level.INFO);
    } else {
      throw new HubRegistrationException("User with matching UUID previously registered");
    }
  }

  public void register(Room newRoom) throws HubRegistrationException {
    assert newRoom != null;
    if (!roomRegistry.containsKey(newRoom.getIdentifier())) {
      roomRegistry.put(newRoom.getIdentifier(), newRoom);
      Logging.logWithID("Room registered", newRoom.getIdentifier(), Level.INFO);
      notification("New Room " + newRoom.getLabel() + " Created", newRoom.getIdentifier());
    } else {
      throw new HubRegistrationException("Room with matching UUID previously registered");
    }
  }

  public void unregister(Room r) throws HubRegistrationException {
    assert r != null;
    if (roomRegistry.isEmpty()) {
      throw new HubRegistrationException("No rooms registered.");
    }
    if (roomRegistry.containsKey(r.getIdentifier())) {
      r.empty();
      roomRegistry.remove(r.getIdentifier());
      Logging.logWithID("Room unregistered", r.getIdentifier(), Level.INFO);
      notification("Room " + r.getLabel() + " has been deconstructed.", r.getIdentifier());
    } else {
      throw new HubRegistrationException("No such room registered to hub");
    }
  }

  /*
   * @pre retiredDevice != null
   */
  public void unregister(Device retiredDevice) throws HubRegistrationException {
    assert retiredDevice != null;
    if (deviceRegistry.isEmpty()) {
      throw new HubRegistrationException("No devices registered.");
    }
    if (deviceRegistry.containsKey(retiredDevice.getIdentifier())) {
      deviceRegistry.remove(retiredDevice.getIdentifier());
      if (retiredDevice.hasRoom()) {
        Room r = retiredDevice.getRoom();
        assert roomRegistry.containsKey(r.getIdentifier());
        roomRegistry.get(r.getIdentifier()).removeRoomDevice(retiredDevice);
      }
      Logging.logWithID("Device unregistered", retiredDevice.getIdentifier(), Level.INFO);
      notification(
          "Device " + retiredDevice.getLabel() + " has been deconstructed.",
          retiredDevice.getIdentifier());
    } else {
      throw new HubRegistrationException("Device does not exist.");
    }
  }

  /*
   * @pre deletedAccount != null
   */
  public void unregister(UserAccount deletedAccount) throws HubRegistrationException {
    assert deletedAccount != null;
    if (userAccountRegistry.isEmpty()
        || !userAccountRegistry.containsKey(deletedAccount.getIdentifier())) {
      throw new HubRegistrationException("Account does not exist.");
    }
    switch (deletedAccount.getAccessLevel()) {
      case ADMIN:
        userAccountRegistry.remove(deletedAccount.getIdentifier());
        break;
      case BASIC:
        userAccountRegistry.remove(deletedAccount.getIdentifier());
        break;
    }
    Logging.logWithID("Unregistered account", deletedAccount.getIdentifier(), Level.INFO);
  }

  /*
   * @pre uuid != null
   */
  public void unregister(UUID uuid) throws HubRegistrationException {
    assert uuid != null;
    if (this.deviceRegistry.containsKey(uuid)) {
      unregister(this.deviceRegistry.get(uuid));
    } else if (this.userAccountRegistry.containsKey(uuid)) {
      unregister(this.userAccountRegistry.get(uuid));
    } else if (this.roomRegistry.containsKey(uuid)) {
      unregister(this.roomRegistry.get(uuid));
    } else {
      throw new HubRegistrationException("No entity with matching UUID is registered to the hub.");
    }
  }

  public Room getRoomByRoomID(UUID roomId) {
    assert roomId != null;
    assert roomRegistry.containsKey(roomId);
    return roomRegistry.get(roomId);
  }

  public Room getRoomByDeviceID(UUID deviceId) {
    assert deviceId != null;
    assert this.deviceRegistry.containsKey(deviceId);
    Device d = deviceRegistry.get(deviceId);
    assert d.hasRoom();
    return d.getRoom();
  }

  public void notifyRoom(UUID deviceId, IOEEventType event) {
    getRoomByDeviceID(deviceId).notifyOccupants(event);
    Logging.logWithID("Event Occurred: " + event.toString(), deviceId, Level.INFO);
    notification(
        "Event Occurred: "
            + event.toString()
            + " in Room "
            + getRoomByDeviceID(deviceId).getLabel(),
        deviceId);
  }

  public List<Device> getRoomContents(Room r) {
    assert r != null;
    return getRoomContents(r.getIdentifier());
  }

  public List<Device> getRoomContents(UUID roomID) {
    assert roomID != null;
    assert isRegisteredRoom(roomID);
    return roomRegistry.get(roomID).getOccupants();
  }

  public void notification(String msg, UUID entitiyID) {
    JSONObject notification;
    for (UserAccount account : userAccountRegistry.values()) {
      if (deviceRegistry.containsKey(entitiyID)) {
        notification = JSONMessaging.getDeviceNotification(msg, deviceRegistry.get(entitiyID));
      } else if (roomRegistry.containsKey(entitiyID)) {
        notification = JSONMessaging.getRoomNotification(msg, roomRegistry.get(entitiyID));
      } else if (userAccountRegistry.containsKey(entitiyID)) {
        notification =
            JSONMessaging.getUserAccountNotification(msg, userAccountRegistry.get(entitiyID));
      } else {
        notification = JSONMessaging.getPlainNotification(msg);
      }
      account.newNotification(entitiyID, notification);
    }
  }

  public void notification(String msg) {
    for (UserAccount account : userAccountRegistry.values()) {
      account.newNotification(JSONMessaging.getPlainNotification(msg));
    }
  }

  public Stack<JSONObject> getNotifications(UserAccount activeUser) {
    return activeUser.getMessages();
  }

  /*
   * @pre label != null
   */
  public boolean isLabelUsed(String label) {
    assert label != null;
    for (Device d : deviceRegistry.values()) {
      if (d.getLabel().equals(label)) {
        return true;
      }
    }
    for (Room r : roomRegistry.values()) {
      if (r.getLabel().equals(label)) {
        return true;
      }
    }
    return false;
  }

  /*
   * @pre username != null
   * @pre password != null
   */
  public UserAccount getUser(String username, String password) throws NoSuchElementException {
    assert username != null;
    assert password != null;
    if (!isUser(username)) {
      throw new NoSuchElementException("No user with username " + username);
    }
    for (UserAccount user : userAccountRegistry.values()) {
      if (user.getUsername().equals(username)) {
        if (user.getPassword().equals(password)) {
          return user;
        } else {
          /* No two users are allowed to have the same user name,
           * so we can safely exit without checking the rest of
           * the userAccounts for a match
           */
          break;
        }
      }
    }
    throw new NoSuchElementException("Incorrect password");
  }

  public UserAccount getUser(UUID user) {
    return this.userAccountRegistry.get(user);
  }

  /*
   * @pre username != null
   */
  public boolean isUser(String username) {
    assert username != null;
    for (UserAccount user : userAccountRegistry.values()) {
      if (user.getUsername().equals(username)) {
        return true;
      }
    }
    return false;
  }

  /*
   * @pre id != null
   */
  public boolean isRegisteredDevice(UUID id) {
    assert id != null;
    return this.deviceRegistry.containsKey(id);
  }

  /*
   * @pre id != null
   */
  public boolean isRegisteredUserAccount(UUID id) {
    assert id != null;
    return this.userAccountRegistry.containsKey(id);
  }

  /*
   * @pre id != null
   */
  public boolean isRegisteredRoom(UUID id) {
    assert id != null;
    return this.roomRegistry.containsKey(id);
  }

  /*
   * Populate deviceRegistry and userRegistry from storage files
   */
  public void startup() {
    Collection<Room> storedRooms = Storage.getRooms(this);
    Collection<Device> storedDevices = Storage.getDevices(this);
    Collection<UserAccount> storedAccounts = Storage.getAccounts(this);
    for (Device d : storedDevices) {
      this.deviceRegistry.put(d.getIdentifier(), d);
    }
    for (UserAccount u : storedAccounts) {
      this.userAccountRegistry.put(u.getIdentifier(), u);
    }
    for (Room r : storedRooms) {
      this.roomRegistry.put(r.getIdentifier(), r);
    }
    Logging.log("startup complete", Level.INFO);
  }

  /*
   * Populate storage files with JSON representations of device/user registries
   */
  public void shutdown() {
    // massSetStatus(Status.OFF);
    Storage.store(
        this.deviceRegistry.values(),
        this.userAccountRegistry.values(),
        this.roomRegistry.values());
    Logging.log("Shutdown complete", Level.INFO);
  }

  public void massSetStatus(Status onOff) {
    for (Entry<UUID, Device> entry : deviceRegistry.entrySet()) {
      Device value = entry.getValue();
      value.setStatus(onOff);
    }
    Logging.log("All devices turned off", Level.INFO);
    notification("All devices turned off");
  }

  /*
   * When returning users, returns ONLY non-admin users
   */
  public ArrayList<UUID> getIDList(boolean isDeviceList) {
    ArrayList<UUID> idList = new ArrayList<UUID>();
    if (isDeviceList) {
      for (UUID key : deviceRegistry.keySet()) {
        idList.add(key);
      }
    } else {
      for (UUID key : userAccountRegistry.keySet()) {
        if (!userAccountRegistry.get(key).isAdmin()) {
          idList.add(key);
        }
      }
    }
    return idList;
  }

  /*
   * @pre uuid != null
   */
  public String getLabel(UUID uuid) {
    assert uuid != null;
    if (deviceRegistry.containsKey(uuid)) {
      return deviceRegistry.get(uuid).getLabel();
    } else if (userAccountRegistry.containsKey(uuid)) {
      return userAccountRegistry.get(uuid).getUsername();
    } else if (roomRegistry.containsKey(uuid)) {
      return roomRegistry.get(uuid).getLabel();
    } else {
      throw new NoSuchElementException("No user or device contains the specified UUID");
    }
  }

  /*
   * @pre user != null
   */
  public ArrayList<UUID> getBlackList(UserAccount user) {
    assert user != null;
    return userAccountRegistry.get(user.getIdentifier()).getBlackList();
  }

  /*
   * @pre uuid != null
   */
  public Device getDevice(UUID uuid) {
    assert uuid != null;
    return deviceRegistry.get(uuid);
  }

  /*
   * @pre newDevice != null
   */
  public void makeNewDevice(DeviceType newDevice, boolean startingState, String customLabel) {
    assert newDevice != null;
    Device added = null;
    switch (newDevice) {
      case CAMERA:
        added = new Camera(this);
        break;
      case SMARTPLUG:
        added = new SmartPlug(this);
        break;
      case LIGHTBULB:
        added = new Lightbulb(this);
        break;
      case THERMOSTAT:
        added = new Thermostat(this);
        break;
      default:
        Logging.log("Invalid parameter passed. No such device type.", Level.ERROR);
        assert false;
        return;
    }
    if (startingState) {
      added.setStatus(Status.ON);
    } else {
      added.setStatus(Status.OFF);
    }
    added.setLabel(customLabel);
  }

  /*
   * Returns the UUID of the first device with a matching label. Null otherwise.
   */
  public UUID getFirstID(String label) {
    assert label != null;
    for (Device d : deviceRegistry.values()) {
      if (d.getLabel().equals(label)) {
        return d.getIdentifier();
      }
    }
    return null;
  }

  // TODO: kill?
  public void alert(String msg, Device pDevice) {
    // TODO should be moved to controller
    // Or should alert some list of observers in which Controller has registered
  }

  // TODO: merge with getIGList()
  public ArrayList<UUID> getRoomsIds() {
    ArrayList<UUID> idList = new ArrayList<UUID>();
    for (UUID key : roomRegistry.keySet()) {
      idList.add(key);
    }
    return idList;
  }
}
