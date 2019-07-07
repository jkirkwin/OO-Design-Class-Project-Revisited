package ca.jkirkwin.seng330.controller;

import ca.jkirkwin.seng330.model.devices.Status;
import java.util.Stack;
import org.json.JSONObject;

public class HubController extends Controller {

  // TODO Refactor this and HubSceneBuilder into 2 classes each:
  // one basic one and an extension that includes admin functionality

  public void handleAdminManageUsersClick() {
    client.setView(findBuilder(ViewType.MANAGE_USERS));
  }

  public void handleAdminManageDevicesClick() {
    client.setView(findBuilder(ViewType.MANAGE_DEVICES));
  }

  public void allOff() {
    hub.massSetStatus(Status.OFF);
    hub.shutdown();
    this.refresh();
  }

  public void notificationView() {
    client.setView(findBuilder(ViewType.SEE_NOTIFICATIONS));
  }

  public Stack<JSONObject> getNotifications() {
    return hub.getNotifications(this.activeUser);
  }

  public void handleAdminManageRoomsClick() {
    client.setView(findBuilder(ViewType.MANAGE_ROOMS));
  }
}
