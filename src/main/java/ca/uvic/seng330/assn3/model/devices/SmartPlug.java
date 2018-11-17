package ca.uvic.seng330.assn3.model.devices;

import ca.uvic.seng330.assn3.model.Hub;
import java.util.UUID;
import org.json.JSONObject;

public class SmartPlug extends Device {

  public SmartPlug(Hub hub) {
    super(hub);
  }

  public SmartPlug(String label, Hub hub) {
    super(label, Status.ON, hub);
  }

  protected SmartPlug(UUID id, String label, Hub hub) {
    super(id, label, Status.ON, hub);
  }

  public boolean isOn() {
    return this.getStatus().equals(Status.ON);
  }

  public void toggle() {
    // TODO Logging
    //    if (this.isOn) {
    //      getMediator().log("Turning SmartPlug Off", Level.INFO, getIdentifier());
    //    } else {
    //      getMediator().log("Turning SmartPlug On", Level.INFO, getIdentifier());
    //    }
    if (this.status.equals(Status.OFF)) {
      setStatus(Status.ON);
    } else if (this.status.equals(Status.ON)) {
      setStatus(Status.OFF);
    } else {
      // TODO Log error
      assert false;
    }
  }

  @Override
  public JSONObject getJSON() {
    JSONObject json = super.getJSON();
    json.put("device_type", "SmartPlug");
    JSONObject state = new JSONObject();
    json.put("state", state);
    return json;
  }
}
