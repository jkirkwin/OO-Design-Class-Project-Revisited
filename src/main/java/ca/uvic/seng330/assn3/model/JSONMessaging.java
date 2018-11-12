package ca.uvic.seng330.assn3.model;

import ca.uvic.seng330.assn3.model.devices.Device;
import ca.uvic.seng330.assn3.model.storage.Storage;
import java.util.Date;
import java.util.UUID;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONMessaging {

  private final Device talker;
  private final String message;
  private final UUID id;

  public JSONMessaging(Device talker, String message) {
    this.talker = talker;
    this.message = message;
    this.id = UUID.randomUUID();
  }

  public JSONMessaging(Device talker, String message, UUID id) {
    this.talker = talker;
    this.message = message;
    this.id = id;
  }

  public JSONObject invoke() {
    JSONObject json = new JSONObject();
    try {
      json.put("msg_id", Storage.getJsonUUID(this.id));
      json.put("node_id", talker.getIdentifier());
      json.put("status", talker.getStatus());
      json.put("payload", message);
      json.put("sent_at", new Date());
    } catch (JSONException e) {
      // TODO Log failure
    }
    return json;
  }

  public Device getTalker() {
    return this.talker;
  }

  @Override
  public boolean equals(Object other) {
    if (other == null) {
      return false;
    }
    if (!(other instanceof JSONMessaging)) {
      return false;
    }
    JSONMessaging o = (JSONMessaging) other;
    return this.talker.equals(o.talker) && this.message.equals(o.message) && this.id.equals(o.id);
  }
}
