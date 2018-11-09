package ca.uvic.seng330.assn3.model;

import ca.uvic.seng330.assn3.model.devices.Device;
import java.util.Date;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONMessaging {

  static int messagesSent = 0;
  Device talker;
  String message = "test message";
  private final int messageNumber = messagesSent + 1;

  public JSONMessaging(Device talker, String message) {
    this.talker = talker;
    this.message = message;
  }

  public JSONObject invoke() {
    JSONObject json = new JSONObject();
    try {
      // TODO: increment msg_id
      json.put("msg_id", messageNumber);
      json.put("node_id", talker.getIdentifier());
      json.put("status", talker.getStatus());
      json.put("payload", message);
      json.put("created_at", new Date());
    } catch (JSONException e) {
    }
    return json;
  }

  public Device getTalker() {
    return this.talker;
  }
}
