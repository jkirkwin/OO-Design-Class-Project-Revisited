package ca.jkirkwin.seng330.controller;

import ca.jkirkwin.seng330.logging.Logging;
import ca.jkirkwin.seng330.model.Hub;
import ca.jkirkwin.seng330.model.HubRegistrationException;
import ca.jkirkwin.seng330.model.UserAccount;
import ca.jkirkwin.seng330.model.devices.Device;
import ca.jkirkwin.seng330.view.Client;
import ca.jkirkwin.seng330.view.scenebuilders.CreateDeviceBuilder;
import ca.jkirkwin.seng330.view.scenebuilders.HubSceneBuilder;
import ca.jkirkwin.seng330.view.scenebuilders.LoginSceneBuilder;
import ca.jkirkwin.seng330.view.scenebuilders.ManageDevicesBuilder;
import ca.jkirkwin.seng330.view.scenebuilders.ManageNotificationsBuilder;
import ca.jkirkwin.seng330.view.scenebuilders.ManageRoomsBuilder;
import ca.jkirkwin.seng330.view.scenebuilders.ManageUsersBuilder;
import ca.jkirkwin.seng330.view.scenebuilders.SceneBuilder;
import ca.jkirkwin.seng330.view.scenebuilders.SeeNotificationsBuilder;
import ca.jkirkwin.seng330.view.scenebuilders.SelectDevicesBuilder;
import ca.jkirkwin.seng330.view.scenebuilders.devicebuilders.CameraSceneBuilder;
import ca.jkirkwin.seng330.view.scenebuilders.devicebuilders.LightbulbSceneBuilder;
import ca.jkirkwin.seng330.view.scenebuilders.devicebuilders.SmartPlugSceneBuilder;
import ca.jkirkwin.seng330.view.scenebuilders.devicebuilders.ThermostatSceneBuilder;
import java.util.ArrayList;
import java.util.Stack;
import java.util.UUID;
import javafx.scene.control.Alert.AlertType;
import org.slf4j.event.Level;

public abstract class Controller {
  // TODO: implement observable

  // Currently holds functions used by multiple subclasses as well as general purpose functions that
  // can be called at any time in the app's life-cycle (refresh, for example)

  protected static Hub hub;
  protected static Client client;
  protected static UserAccount activeUser;
  protected static final Stack<ViewType> views = new Stack<ViewType>();

  public void init(Client client, Hub hub) {
    assert client != null;
    assert hub != null;
    this.client = client;
    this.hub = hub;
  }

  public void exitApplication() {
    hub.shutdown();
  }

  /*
   * @pre views cannot be empty
   */
  public void refresh() {
    assert !views.isEmpty();
    ViewType currentView = views.pop();
    client.setView(findBuilder(currentView));
    Logging.log("Scene Refreshed", Level.DEBUG);
  }

  public void handleBackClick() {
    assert !views.isEmpty();
    if (views.peek() == ViewType.LOGIN) {
      exitApplication();
      client.close();
      return;
    } else if (views.peek() == ViewType.HUB_BASIC || views.peek() == ViewType.HUB_ADMIN) {
      // TODO: log out
      activeUser = null;
    }
    client.setTitle(views.peek().toString());
    views.pop();
    client.setView(findBuilder(views.pop()));
    Logging.log("Back to Previous Scene", Level.DEBUG);
  }

  /*
   * @pre view != null
   */
  public SceneBuilder findBuilder(ViewType view) {
    assert view != null;
    // Generate the appropriate SceneBuilder based on for the ViewType
    views.push(view);
    client.setTitle(view.toString());
    Logging.log("View " + view.toString() + " pushed to stack", Level.DEBUG);
    switch (view) {
      case LOGIN:
        return new LoginSceneBuilder(new LoginController(), "Close");

      case CREATE_DEVICE:
        Logging.log("ManageDevicesController used.", Level.DEBUG);
        return new CreateDeviceBuilder(new ManageDevicesController(), "Back");

      case HUB_ADMIN:
        Logging.log("HubController used.", Level.DEBUG);
        return new HubSceneBuilder(new HubController(), "Log Out", true);

      case HUB_BASIC:
        Logging.log("HubController used.", Level.DEBUG);
        return new HubSceneBuilder(new HubController(), "Log Out", false);

      case MANAGE_DEVICES:
        Logging.log("ManageDevicesController used.", Level.DEBUG);
        return new ManageDevicesBuilder(new ManageDevicesController(), "Back");

      case MANAGE_USERS:
        Logging.log("Main Controller used.", Level.DEBUG);
        return new ManageUsersBuilder(this, "Back");

      case MANAGE_ROOMS:
        Logging.log("RoomController used.", Level.DEBUG);
        return new ManageRoomsBuilder(new RoomController(), "Back");

      case SELECT_DEVICES:
        Logging.log("Main Controller used.", Level.DEBUG);
        return new SelectDevicesBuilder(this, "Back");

      case SEE_NOTIFICATIONS:
        Logging.log("Main Controller used.", Level.DEBUG);
        return new SeeNotificationsBuilder(this, "Back");

      default:
        Logging.log(
            "No case in controller.findBuilder() for viewType " + view.toString(), Level.WARN);
        break;
    }
    return null;
  }

  public ArrayList<UUID> getDeviceIDList() {
    ArrayList<UUID> refined = new ArrayList<UUID>();
    for (UUID id : hub.getIDList(true)) {
      if (!hub.getBlackList(activeUser).contains(id)) {
        refined.add(id);
      }
    }
    return refined;
  }

  /*
   * Returns a list of all *NON-ADMIN* users's UUIDs
   */
  public ArrayList<UUID> getBasicUserAccountIDs() {
    ArrayList<UUID> refined = new ArrayList<UUID>();
    for (UUID id : hub.getIDList(false)) {
      refined.add(id);
    }
    return refined;
  }

  /*
   * If uuid corresponds to a device, returns device's label
   * If uuid corresponds to a userAcct, returns username
   * @pre uuid != null
   */
  public String getLabel(UUID uuid) {
    assert uuid != null;
    return hub.getLabel(uuid);
  }

  public String devStatus(UUID id) {
    return hub.getDevice(id).getStatus().toString();
  }

  public Hub getHub() {
    return hub;
  }

  /*
   * @pre uuid != null
   * @pre uuid must be the identifier for some entity registered to the hub.
   */
  public void handleDeleteClick(UUID uuid) {
    assert uuid != null;
    boolean isDevice = hub.isRegisteredDevice(uuid);
    boolean isUserAccount = hub.isRegisteredUserAccount(uuid);
    boolean isRoom = hub.isRegisteredRoom(uuid);
    assert isDevice || isUserAccount || isRoom;
    String label = hub.getLabel(uuid);
    try {
      hub.unregister(uuid);
    } catch (HubRegistrationException e) {
      Logging.logWithID("Hub has not registered entity", uuid, Level.ERROR);
    }
    if (isDevice) {
      client.alertUser(
          AlertType.INFORMATION,
          "Device Removed",
          "Device Removed",
          "Unregistered Device: " + label);
    } else if (isUserAccount) {
      client.alertUser(
          AlertType.INFORMATION, "User Removed", "User Removed", "Unregistered User: " + label);
    } else if (isRoom) {
      client.alertUser(
          AlertType.INFORMATION, "Room Removed", "Room Removed", "Unregistered Room: " + label);
    }
    refresh();
  }

  /*
   * @pre uuid != null
   */
  public void handleDeviceViewClick(UUID uuid) {
    assert uuid != null;

    views.push(ViewType.DEVICE_VIEW);
    client.setTitle(ViewType.DEVICE_VIEW.toString());
    deviceViewSwitch(uuid);
  }

  /*
   * Allows the skipping of views.push() etc...
   */
  protected void deviceViewSwitch(UUID uuid) {
    assert uuid != null;
    switch (getDeviceType(hub.getDevice(uuid))) {
      case CAMERA:
        client.setView(
            new CameraSceneBuilder(new CameraController(uuid), "Back", uuid)); // TODO Fix
        Logging.log(
            getDeviceType(hub.getDevice(uuid)).toString()
                + " View pushed to stack: CameraController used.",
            Level.DEBUG);
        break;
      case LIGHTBULB:
        client.setView(new LightbulbSceneBuilder(new LightbulbController(uuid), "Back", uuid));
        Logging.log(
            getDeviceType(hub.getDevice(uuid)).toString()
                + " View pushed to stack: LightbulbController used.",
            Level.DEBUG);
        break;
      case SMARTPLUG:
        client.setView(new SmartPlugSceneBuilder(new SmartPlugController(uuid), "Back", uuid));
        Logging.log(
            getDeviceType(hub.getDevice(uuid)).toString()
                + " View pushed to stack: SmartPlugController used.",
            Level.DEBUG);
        break;
      case THERMOSTAT:
        client.setView(new ThermostatSceneBuilder(new ThermostatController(uuid), "Back", uuid));
        Logging.log(
            getDeviceType(hub.getDevice(uuid)).toString()
                + " View pushed to stack: ThermostatController used.",
            Level.DEBUG);
        break;
    }
  }

  protected DeviceType getDeviceType(Device d) {
    return DeviceType.valueOf(d.getClass().getSimpleName().toUpperCase());
  }

  /*
   * @pre baseLabel != null
   */
  protected String getUniqueDeviceLabel(String baseLabel) {
    assert baseLabel != null;
    String uniqueLabel = baseLabel;
    int i = 1;
    while (hub.isLabelUsed(uniqueLabel)) {
      uniqueLabel = baseLabel + "(" + i + ")";
      i++;
    }
    Logging.log("Label Collision. Label changed to " + uniqueLabel, Level.TRACE);
    return uniqueLabel;
  }

  // TODO Move this?
  public void handleUserViewClick(UUID id) {
    client.setView(findBuilder(ViewType.SELECT_DEVICES));
  }

  public void handleUsersVisabilityClick(UUID userData) {
    views.push(ViewType.MANAGE_NOTIFICATIONS);
    client.setTitle(ViewType.MANAGE_NOTIFICATIONS.toString());
    Logging.log(
        "View " + ViewType.MANAGE_NOTIFICATIONS.toString() + " pushed to stack", Level.DEBUG);
    client.setView(new ManageNotificationsBuilder(this, "Back", userData));
  }

  public String isDeviceBlackListed(UUID user, UUID device) {
    if (this.getHub().getBlackList(this.getHub().getUser(user)).contains(device)) {
      return "BlackListed";
    } else {
      return "WhiteListed";
    }
  }

  public void blackListToggle(UUID user, UUID device) {
    if (this.getHub().getBlackList(this.getHub().getUser(user)).contains(device)) {
      this.getHub().getUser(user).whiteList(device);
      Logging.logWithID(
          " device has been whitelisted by user " + user.toString(), device, Level.INFO);
    } else {
      this.getHub().getUser(user).blackList(device);
      Logging.logWithID(
          " device has been blacklisted by user " + user.toString(), device, Level.INFO);
    }
    ViewType currentView = views.pop();
    this.handleUsersVisabilityClick(user);
  }
}
