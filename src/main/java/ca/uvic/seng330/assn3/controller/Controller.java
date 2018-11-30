package ca.uvic.seng330.assn3.controller;

import ca.uvic.seng330.assn3.model.Hub;
import ca.uvic.seng330.assn3.model.HubRegistrationException;
import ca.uvic.seng330.assn3.model.UserAccount;
import ca.uvic.seng330.assn3.model.devices.Device;
import ca.uvic.seng330.assn3.view.Client;
import ca.uvic.seng330.assn3.view.scenebuilders.CreateDeviceBuilder;
import ca.uvic.seng330.assn3.view.scenebuilders.HubSceneBuilder;
import ca.uvic.seng330.assn3.view.scenebuilders.LoginSceneBuilder;
import ca.uvic.seng330.assn3.view.scenebuilders.ManageDevicesBuilder;
import ca.uvic.seng330.assn3.view.scenebuilders.ManageRoomsBuilder;
import ca.uvic.seng330.assn3.view.scenebuilders.ManageUsersBuilder;
import ca.uvic.seng330.assn3.view.scenebuilders.SceneBuilder;
import ca.uvic.seng330.assn3.view.scenebuilders.SeeNotificationsBuilder;
import ca.uvic.seng330.assn3.view.scenebuilders.SelectDevicesBuilder;
import ca.uvic.seng330.assn3.view.scenebuilders.devicebuilders.CameraSceneBuilder;
import ca.uvic.seng330.assn3.view.scenebuilders.devicebuilders.LightbulbSceneBuilder;
import ca.uvic.seng330.assn3.view.scenebuilders.devicebuilders.SmartPlugSceneBuilder;
import ca.uvic.seng330.assn3.view.scenebuilders.devicebuilders.ThermostatSceneBuilder;
import ca.uvic.seng330.assn3.view.scenebuilders.ManageNotificationsBuilder;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Stack;
import java.util.UUID;
import javafx.scene.control.Alert.AlertType;

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
  }

  public void handleBackClick() {
    assert !views.isEmpty();
    if (views.peek() == ViewType.LOGIN) {
      exitApplication();
      client.close();
      return;
    } else if (views.peek() == ViewType.HUB_BASIC || views.peek() == ViewType.HUB_ADMIN) {
      // log out
      activeUser = null;
    }
    client.setTitle(views.peek().toString());
    views.pop();
    client.setView(findBuilder(views.pop()));
  }

  /*
   * @pre view != null
   */
  public SceneBuilder findBuilder(ViewType view) {
    assert view != null;
    // Generate the appropriate SceneBuilder based on for the ViewType
    views.push(view);
    client.setTitle(view.toString());
    switch (view) {
      case LOGIN:
        return new LoginSceneBuilder(new LoginController(), "Close");

      case CREATE_DEVICE:
        // TODO
        // We don't need to create a new one, since it's the same kind of controller used
        // at manage devices, which is the sole precursor to this view
        return new CreateDeviceBuilder(new ManageDevicesController(), "Back");

      case HUB_ADMIN:
        return new HubSceneBuilder(new HubController(), "Log Out", true);

      case HUB_BASIC:
        return new HubSceneBuilder(new HubController(), "Log Out", false);

      case MANAGE_DEVICES:
        return new ManageDevicesBuilder(new ManageDevicesController(), "Back");

      case MANAGE_USERS:
        return new ManageUsersBuilder(this, "Back");

      case MANAGE_ROOMS:
        return new ManageRoomsBuilder(new RoomController(), "Back");

      case SELECT_DEVICES:
        return new SelectDevicesBuilder(this, "Back");

      case SEE_NOTIFICATIONS:
        return new SeeNotificationsBuilder(this, "Back");

      default:
        // TODO: logging/Alert
        System.out.println("No case in controller.findBuilder() for viewType " + view);
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
    assert isDevice || isUserAccount;
    String label = hub.getLabel(uuid);
    try {
      hub.unregister(uuid);
    } catch (HubRegistrationException e) {
      // TODO remove stacktrace print and add log
      e.printStackTrace();
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
        break;
      case LIGHTBULB:
        client.setView(new LightbulbSceneBuilder(new LightbulbController(uuid), "Back", uuid));
        break;
      case SMARTPLUG:
        client.setView(new SmartPlugSceneBuilder(new SmartPlugController(uuid), "Back", uuid));
        break;
      case THERMOSTAT:
        client.setView(new ThermostatSceneBuilder(new ThermostatController(uuid), "Back", uuid));
        break;
    }
  }

  protected DeviceType getDeviceType(Device d) {
    return DeviceType.valueOf(d.getClass().getSimpleName().toUpperCase());
  }

  // TODO remove and replace usages with use of library function DeviceType.values()
  public ArrayList<DeviceType> getDeviceTypes() {
    ArrayList<DeviceType> deviceTypes = new ArrayList<DeviceType>();
    EnumSet.allOf(DeviceType.class).forEach(devType -> deviceTypes.add(devType));
    return deviceTypes;
  }

  // TODO Move this?
  public void handleUserViewClick(UUID id) {
    client.setView(findBuilder(ViewType.SELECT_DEVICES));
  }

  public void handleUsersVisabilityClick(UUID userData) {
    views.push(ViewType.MANAGE_NOTIFICATIONS);
    client.setTitle(ViewType.MANAGE_NOTIFICATIONS.toString());
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
    } else {
      this.getHub().getUser(user).blackList(device);
    }
    ViewType currentView = views.pop();
    this.handleUsersVisabilityClick(user);
  }
}
