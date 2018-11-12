package ca.uvic.seng330.assn3.model;

import ca.uvic.seng330.assn3.model.devices.Device;
import ca.uvic.seng330.assn3.model.storage.Storage;

import java.util.Date;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONMessaging {

  Device talker;
  String message = "test message";

  public JSONMessaging(Device talker, String message) {
    this.talker = talker;
    this.message = message;
  }

  public JSONObject invoke() {
    JSONObject json = new JSONObject();
    try {
      json.put("msg_id", Storage.getJsonUUID(UUID.randomUUID()));
      json.put("node_id", talker.getIdentifier());
      json.put("status", talker.getStatus());
      json.put("payload", message);
      json.put("created_at", new Date());
    } catch (JSONException e) {
      // TODO Log failure
    }
    return json;
  }

  public Device getTalker() {
    return this.talker;
  }
  
  
}
