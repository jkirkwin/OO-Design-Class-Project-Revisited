package ca.uvic.seng330.assn3.model;

import ca.uvic.seng330.assn3.model.devices.Device;
import java.util.Date;
import java.util.UUID;
import org.json.JSONObject;

public class JSONMessaging {

  public static JSONObject getPlainNotification(String message) {
    assert message != null;
    JSONObject messageWrapper = new JSONObject();
    messageWrapper.put("message", message);
    messageWrapper.put("message_id", UUID.randomUUID());
    messageWrapper.put("sent_at", new Date());
    return messageWrapper;
  }

  public static JSONObject getDeviceNotification(String message, Device d) {
    assert message != null;
    assert d != null;
    JSONObject messageWrapper = getPlainNotification(message);
    messageWrapper.put("device_status", d.getStatus());
    messageWrapper.put("device_label", d.getLabel());
    return messageWrapper;
  }

  public static JSONObject getRoomNotification(String message, Room r) {
    assert message != null;
    assert r != null;
    JSONObject messageWrapper = getPlainNotification(message);
    messageWrapper.put("room_label", r.getLabel());
    return messageWrapper;
  }

  public static JSONObject getUserAccountNotification(String message, UserAccount u) {
    assert message != null;
    assert u != null;
    JSONObject messageWrapper = getPlainNotification(message);
    messageWrapper.put("username", u.getUsername());
    messageWrapper.put("access_type", u.getAccessLevel());
    return messageWrapper;
  }

  //  private final Device talker;
  //  private final String message;
  //  private final UUID id;
  //
  //  public JSONMessaging(Device talker, String message) {
  //    this.talker = talker;
  //    this.message = message;
  //    this.id = UUID.randomUUID();
  //  }
  //
  //  public JSONMessaging(Device talker, String message, UUID id) {
  //    this.talker = talker;
  //    this.message = message;
  //    this.id = id;
  //  }

  //  public JSONObject invoke() {
  //    JSONObject json = new JSONObject();
  //    try {
  //      json.put("msg_id", Storage.getJsonUUID(this.id));
  //      json.put("node_id", talker.getIdentifier());
  //      json.put("status", talker.getStatus());
  //      json.put("payload", message);
  //      json.put("sent_at", new Date());
  //    } catch (JSONException e) {
  //      // TODO Log failure
  //      Logging.logWithID("JSONObject not created", id, Level.WARN);
  //    }
  //    return json;
  //  }
  //
  //  public Device getTalker() {
  //    return this.talker;
  //  }

}
