package ca.uvic.seng330.assn3.model;

import ca.uvic.seng330.assn3.logging.Logging;
import ca.uvic.seng330.assn3.model.storage.Storage;
import ca.uvic.seng330.assn3.model.storage.StorageEntity;
import java.util.ArrayList;
import java.util.Stack;
import java.util.UUID;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.event.Level;

public class UserAccount implements StorageEntity {

  private AccessLevel accessLevel;
  private Hub hub;
  private final UUID id;
  private ArrayList<UUID> blackList;
  private Stack<JSONMessaging> notificationList;
  private final String username;
  private final String password;

  public UserAccount(Hub h, AccessLevel level, String username, String password) {
    this.hub = h;
    this.accessLevel = level;
    this.username = username;
    this.password = password;
    this.notificationList = new Stack<JSONMessaging>();
    this.blackList = new ArrayList<UUID>();
    this.id = UUID.randomUUID();
    try {
      hub.register(this);
    } catch (HubRegistrationException e) {
      Logging.logWithID(
          "Failed to register useraccount from public constructor.", this.id, Level.ERROR);
    }
  }

  private UserAccount(
      Hub h,
      AccessLevel level,
      String username,
      String password,
      UUID accountID,
      Stack<JSONMessaging> notificationList,
      ArrayList<UUID> blackList) {
    this.hub = h;
    this.accessLevel = level;
    this.username = username;
    this.password = password;
    this.id = accountID;
    this.notificationList = notificationList;
    this.blackList = blackList;
    try {
      hub.register(this);
    } catch (HubRegistrationException e) {
      Logging.logWithID(
          "Failed to register useraccount from private constructor.", this.id, Level.ERROR);
    }
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
    if (this.isAdmin()) {
      Logging.logWithID(
          "Unable to blacklist device for admin account. No Action taken.", illegal, Level.WARN);
    } else if (this.blackList.contains(illegal)) {
      Logging.logWithID("Device already blacklisted. No Action taken.", illegal, Level.WARN);
    } else {
      this.blackList.add(illegal);
      Logging.logWithID("Device blacklisted for User " + this.getUsername(), illegal, Level.INFO);
    }
  }

  public void whiteList(UUID legal) {
    if (this.blackList.contains(legal)) {
      this.blackList.remove(legal);
      Logging.logWithID("Device whitelisted for User " + this.getUsername(), legal, Level.INFO);
    } else {
      Logging.logWithID("Device already whitelisted. No action taken.", legal, Level.WARN);
    }
  }

  protected ArrayList<UUID> getBlackList() {
    assert this.blackList != null;
    return this.blackList;
  }

  public void newNotification(UUID deviceID, JSONMessaging msg) {
    if (!getBlackList().contains(deviceID)) {
      this.notificationList.push(msg);
    }
  }

  public void newNotification(JSONMessaging msg) {
    this.notificationList.push(msg);
  }

  public Stack<JSONObject> getMessages() {
    Stack<JSONObject> copy = new Stack<JSONObject>();
    while (!this.notificationList.isEmpty()) {
      copy.push(notificationList.pop().invoke());
    }
    return copy;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  @Override
  public boolean equals(Object other) {
    if (!(other instanceof UserAccount)) {
      return false;
    }
    UserAccount o = (UserAccount) other;
    return this.accessLevel.equals(o.accessLevel)
        && this.id.equals(o.id)
        && this.blackList.equals(o.blackList)
        && this.notificationList.equals(o.notificationList)
        && this.username.equals(o.username)
        && this.password.equals(o.password);
  }

  public JSONObject getJSON() {
    JSONObject json = new JSONObject();
    try {
      json.put("username", this.username);
      json.put("password", this.password);
      json.put("id", Storage.getJsonUUID(this.id));
      json.put("access_level", this.getAccessLevel());

      // Create a JSON list of the UUID's of devices in the blacklist and add this under key
      // "black_list"
      JSONArray jsonBlackList = new JSONArray();
      for (UUID d : this.blackList) {
        jsonBlackList.put(Storage.getJsonUUID(d));
      }
      json.put("black_list", jsonBlackList);

      // Create a JSON list of notifications waiting for the user and add this under key
      // "notifications"
      JSONArray notifications = new JSONArray();
      Stack<JSONMessaging> temp = new Stack<JSONMessaging>(); // To prevent losing notifications
      while (!this.notificationList.isEmpty()) {
        JSONMessaging messaging = this.notificationList.peek();
        JSONObject notificationWrapper = new JSONObject();
        JSONObject message = messaging.invoke();
        UUID talkerID = messaging.getTalker().getIdentifier();
        notificationWrapper.put("body", message);
        notificationWrapper.put("id", Storage.getJsonUUID(talkerID));
        notifications.put(notificationWrapper);
        temp.push(notificationList.pop());
      }
      json.put("notifications", notifications);
      while (!temp.isEmpty()) {
        notificationList.push(temp.pop()); // rebuild notificationList
      }
    } catch (JSONException e) {
      Logging.log("JSONException thrown.", Level.ERROR);
    }
    return json;
  }

  public static UserAccount getAccountFromJSON(JSONObject o, Hub hub) {
    String username = o.getString("username");
    String password = o.getString("password");
    AccessLevel level = o.getEnum(AccessLevel.class, "access_level");
    UUID accountID = Storage.getUUID(o.getJSONObject("id"));

    // Create notificationList
    Stack<JSONMessaging> notificationList = new Stack<JSONMessaging>();
    JSONArray JSONnotifications = o.getJSONArray("notifications");
    for (int i = JSONnotifications.length() - 1; i >= 0; i--) {
      JSONObject notificationObj = JSONnotifications.getJSONObject(i);
      JSONObject body = notificationObj.getJSONObject("body");
      UUID talkerID = Storage.getUUID(notificationObj.getJSONObject("id"));
      UUID messageID = Storage.getUUID(body.getJSONObject("msg_id"));
      notificationList.push(
          new JSONMessaging(hub.getDevice(talkerID), body.getString("payload"), messageID));
    }

    // Create blackList
    ArrayList<UUID> blackList = new ArrayList<UUID>();
    JSONArray JSONblackList = new JSONArray(o.getJSONArray("black_list").toString());
    for (int i = 0; i < JSONblackList.length(); i++) {
      JSONObject listEntry = JSONblackList.getJSONObject(i);
      blackList.add(Storage.getUUID(listEntry));
    }
    return new UserAccount(hub, level, username, password, accountID, notificationList, blackList);
  }
}
