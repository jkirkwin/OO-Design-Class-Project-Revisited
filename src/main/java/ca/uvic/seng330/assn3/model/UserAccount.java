package ca.uvic.seng330.assn3.model;

import ca.uvic.seng330.assn3.model.storage.Storage;
import ca.uvic.seng330.assn3.model.storage.StorageEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.UUID;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UserAccount implements StorageEntity {

  private AccessLevel accessLevel;
  private Hub hub;
  private final UUID id;
  private List<UUID> blackList;
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
    hub.register(this);
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
    hub.register(this);
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

  protected List<UUID> getBlackList() {
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
  
  @Override
  public boolean equals(Object other) {
//    System.out.println("In UserAccount.equals()");
    if(!(other instanceof UserAccount)) {
      return false;
    }
    
    UserAccount o = (UserAccount) other;
//    System.out.println("----------------"); 
//    System.out.println("this.hub == o.hub: " + (this.hub == o.hub));
//    System.out.println("this.id.equals(o.id): " + (this.id.equals(o.id)));
//    System.out.println("this.blackList.equals(o.blackList): " + this.blackList.equals(o.blackList));
//    System.out.println("this.notificationList.equals(o.notificationList): " + this.notificationList.equals(o.notificationList));
//    System.out.println("this.username.equals(o.username): " + this.username.equals(o.username));
//    System.out.println("this.password.equals(o.password): " + this.password.equals(o.password));
//    System.out.println("----------------"); 
    
    return this.accessLevel.equals(o.accessLevel)
        && this.hub == o.hub        // Note we are checking for identity here
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
      // TODO Log this failure
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
      notificationList.push(new JSONMessaging(hub.getDevice(talkerID), body.getString("payload"), messageID));
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
