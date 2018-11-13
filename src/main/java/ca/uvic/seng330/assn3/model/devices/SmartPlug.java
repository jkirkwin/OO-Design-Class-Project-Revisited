package ca.uvic.seng330.assn3.model.devices;

import ca.uvic.seng330.assn3.model.Hub;
import java.util.UUID;
import org.json.JSONObject;

public class SmartPlug extends Device {

  static int numPlug = 0;
  private boolean isOn;

  public SmartPlug(Hub hub) {
    super("SmartPlug" + numPlug, Status.ON, hub);
    this.isOn = false;
    numPlug++;
  }

  public SmartPlug(String label, Hub hub) {
    super(label, Status.ON, hub);
    this.isOn = false;
  }

  protected SmartPlug(boolean isOn, UUID id, String label, Hub hub) {
    super(id, label, Status.ON, hub);
    this.isOn = isOn;
  }

  public boolean isOn() {
    return this.isOn;
  }

  public void toggle() {
    // TODO Logging
    //    if (this.isOn) {
    //      getMediator().log("Turning SmartPlug Off", Level.INFO, getIdentifier());
    //    } else {
    //      getMediator().log("Turning SmartPlug On", Level.INFO, getIdentifier());
    //    }
    this.isOn = !this.isOn;
  }

  @Override
  public JSONObject getJSON() {
    JSONObject json = super.getJSON();
    json.put("device_type", "SmartPlug");
    JSONObject state = new JSONObject();
    state.put("is_on", this.isOn());
    json.put("state", state);
    return json;
  }
}
