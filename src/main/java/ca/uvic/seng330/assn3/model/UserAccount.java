package ca.uvic.seng330.assn3.model;

import ca.uvic.seng330.assn3.model.devices.Device;
import java.util.ArrayList;
import java.util.Stack;
import java.util.UUID;

public class UserAccount {

  private AccessLevel accessLevel;
  private Hub hub;
  private final UUID id = UUID.randomUUID();
  private ArrayList<Device> blackList = new ArrayList<Device>();
  private Stack<JSONMessaging> notificationList = new Stack<JSONMessaging>();

  public UserAccount(Hub h, AccessLevel isAdmin) {
    this.hub = h;
    this.accessLevel = isAdmin;
  }

  public UUID getIdentifier() {
    return id;
  }

  public boolean isAdmin() {
    return accessLevel == AccessLevel.ADMIN;
  }

  public AccessLevel getAccessLevel() {
    return accessLevel;
  }

  public void blackList(Device illegal) {
    // TODO: add Logging and Alert
    if (this.isAdmin() || this.blackList.contains(illegal)) {
      // TODO: Throw exception?
      return;
    }
    this.blackList.add(illegal);
  }

  public void whiteList(Device legal) {
    // TODO: add Logging and Alert
    if (this.blackList.contains(legal)) {
      this.blackList.remove(legal);
    } else {
      // TODO: Throw exception?
    }
  }

  public Stack<JSONMessaging> getMessages() {
    // TODO: UNIT TESTS FOR THIS!!!
    Stack<JSONMessaging> copy = new Stack<JSONMessaging>();
    while (!this.notificationList.isEmpty()) {
      copy.push(notificationList.pop());
    }
    return copy;
  }
}
