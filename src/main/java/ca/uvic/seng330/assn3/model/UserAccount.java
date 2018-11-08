package ca.uvic.seng330.assn3.model;

import java.util.ArrayList;
import java.util.Stack;
import java.util.UUID;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ca.uvic.seng330.assn3.model.storage.StorageEntity;

public class UserAccount implements StorageEntity {

  private AccessLevel accessLevel;
  private Hub hub;
  private final UUID id = UUID.randomUUID();
  private ArrayList<UUID> blackList = new ArrayList<UUID>();
  private Stack<JSONMessaging> notificationList = new Stack<JSONMessaging>();
  private final String username;
  private final String password;

  public UserAccount(Hub h, AccessLevel isAdmin, String username, String password) {
    this.hub = h;
    this.accessLevel = isAdmin;
    this.username = username;
    this.password = password;
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

  public void blackList(UUID illegal) {
    // TODO: add Logging and Alert
    if (this.isAdmin() || this.blackList.contains(illegal)) {
      // TODO: Throw exception?
      return;
    }
    this.blackList.add(illegal);
  }

  public void whiteList(UUID legal) {
    // TODO: add Logging and Alert
    if (this.blackList.contains(legal)) {
      this.blackList.remove(legal);
    } else {
      // TODO: Throw exception?
    }
  }

  protected ArrayList<UUID> getBlackList() {
    // TODO: clone for encapsulation
    return this.blackList;
  }

  public Stack<JSONMessaging> getMessages() {
    // TODO: UNIT TESTS FOR THIS!!!
    Stack<JSONMessaging> copy = new Stack<JSONMessaging>();
    while (!this.notificationList.isEmpty()) {
      copy.push(notificationList.pop());
    }
    return copy;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  public JSONObject getJSON() {
    JSONObject json = new JSONObject();
    try {
      json.put("username", this.username);
      json.put("password", this.password);
      json.put("id", this.id);
      json.put("access_level", this.getAccessLevel());

      // Create a JSON list of the UUID's of devices in the blacklist and add this under key
      // "black_list"
      JSONArray jsonBlackList = new JSONArray();
      for (UUID d : this.blackList) {
        jsonBlackList.put(d);
      }
      json.put("black_list", jsonBlackList);

      // Create a JSON list of notifications waiting for the user and add this under key
      // "notifications"
      JSONArray notifications = new JSONArray();
      Stack<JSONMessaging> temp = new Stack<JSONMessaging>(); // To prevent losing notifications
      while (!this.notificationList.isEmpty()) {
        notifications.put(this.notificationList.peek().invoke());
        temp.push(notificationList.pop());
      }
      while (!temp.isEmpty()) {
        notificationList.push(temp.pop()); // rebuild notificationList
      }
    } catch (JSONException e) {
      // TODO Log this failure
    }
    return json;
  }
}
