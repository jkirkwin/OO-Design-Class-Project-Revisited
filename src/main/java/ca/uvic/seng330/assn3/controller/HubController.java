package ca.uvic.seng330.assn3.controller;

public class HubController extends Controller {

  // TODO Refactor this and HubSceneBuilder into 2 classes each:
  // one basic one and an extension that includes admin functionality

  // ===================== admin panel ========================= //

  public void handleAdminManageUsersClick() {
    client.setView(findBuilder(ViewType.MANAGE_USERS));
  }

  public void handleAdminManageDevicesClick() {
    client.setView(findBuilder(ViewType.MANAGE_DEVICES));
  }

  public void handleAdminManageNotificationsClick() {
    // TODO
  }
}
