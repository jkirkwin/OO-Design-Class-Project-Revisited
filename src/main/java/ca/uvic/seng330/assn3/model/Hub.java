package ca.uvic.seng330.assn3.model;

import ca.uvic.seng330.assn3.controller.DeviceType;
import ca.uvic.seng330.assn3.model.devices.*;
import ca.uvic.seng330.assn3.model.storage.Storage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.UUID;

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
   * @pre newDevice != null
   */
  public void register(Device newDevice) throws HubRegistrationException {
    assert newDevice != null;
    if (!deviceRegistry.containsKey(newDevice.getIdentifier())) {
      deviceRegistry.put(newDevice.getIdentifier(), newDevice);
    } else {
      throw new HubRegistrationException("Device already registered.");
    }
  }

  /*
   * @pre newAccount != null
   */
  public void register(UserAccount newAccount) {
    assert newAccount != null;
    try {
      registerNew(newAccount);
    } catch (HubRegistrationException e) {
      // TODO: Logging and Alerts
    }
  }

  /*
   * @pre newAccount != null
   */
  private void registerNew(UserAccount newAccount) throws HubRegistrationException {
    assert newAccount != null;
    if (!userAccountRegistry.containsKey(newAccount.getIdentifier())) {
      userAccountRegistry.put(newAccount.getIdentifier(), newAccount);
    } else {
      throw new HubRegistrationException("User already registered");
    }
  }

  public void addRoom(Room r) {
    assert r != null;
    if (!roomRegistry.containsKey(r.getID())) {
      roomRegistry.put(r.getID(), r);
    }
  }

  public void removeRoom(Room r) {
    // TODO If time permits, do a small refactor of device registry to hold a set of rooms that
    //      partitions the device registry to make this not an O(n) operation
    assert r != null;
    assert roomRegistry.containsKey(r.getID());
    for (Device d : deviceRegistry.values()) {
      if (d.hasRoom() && d.getRoom().equals(r)) {
        d.removeRoom();
      }
    }
    this.roomRegistry.remove(r.getID());
  }

  /*
   * @pre uuid != null
   */
  public void unregister(UUID uuid) {
    assert uuid != null;
    if (this.deviceRegistry.containsKey(uuid)) {
      try {
        unregister(this.deviceRegistry.get(uuid));
      } catch (HubRegistrationException e) {
        // TODO: logging & alert
      }
    } else if (this.userAccountRegistry.containsKey(uuid)) {
      try {
        unregister(this.userAccountRegistry.get(uuid));
      } catch (HubRegistrationException e) {
        // TODO: logging & alert
      }
    } else {
      // TODO: alert that nothing corresponds to given UUID
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
    try {
      unregisterRetired(deletedAccount);
    } catch (HubRegistrationException e) {
      // TODO:  Logging and Alerts
    }
  }

  /*
   * @pre deletedAccount != null
   */
  private void unregisterRetired(UserAccount deletedAccount) throws HubRegistrationException {
    assert deletedAccount != null;
    switch (deletedAccount.getAccessLevel()) {
      case ADMIN:
        userAccountRegistry.remove(deletedAccount.getIdentifier());
        break;
      case BASIC:
        userAccountRegistry.remove(deletedAccount.getIdentifier());
        break;
    }
  }

  public void log(String msg, UUID id) {
    // TODO
  }

  public void alert(String msg, Device pDevice) throws HubRegistrationException {
    // TODO should be moved to controller
    // Or should alert some list of observers in which Controller has registered
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
      this.roomRegistry.put(r.getID(), r);
    }
  }

  /*
   * Populate storage files with JSON representations of device/user registries
   */
  public void shutdown() {
    // TODO: turn off all devices
    Storage.store(
        this.deviceRegistry.values(),
        this.userAccountRegistry.values(),
        this.roomRegistry.values());
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
        // TODO: throw an error here and remove the assertion
        assert (false);
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

  public Room getRoom(UUID roomId) {
    assert roomId != null;
    assert roomRegistry.containsKey(roomId);
    return roomRegistry.get(roomId);
  }
}
