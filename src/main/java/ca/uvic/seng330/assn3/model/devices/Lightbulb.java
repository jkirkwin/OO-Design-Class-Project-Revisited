package ca.uvic.seng330.assn3.model.devices;

import ca.uvic.seng330.assn3.model.Hub;
import java.util.UUID;
import org.json.JSONObject;

public class Lightbulb extends Device {

  static int numBulbs = 0;
  private boolean isOn;

  public Lightbulb(Hub hub) {
    super("LightBulb" + numBulbs, Status.NORMAL, hub);
    this.isOn = false;
    numBulbs++;
  }

  public Lightbulb(String label, Hub hub) {
    super(label, Status.NORMAL, hub);
    this.isOn = false;
  }

  protected Lightbulb(boolean isOn, UUID id, String label, Hub hub) {
    super(id, label, Status.NORMAL, hub);
    this.isOn = isOn;
  }

  public boolean isOn() {
    return this.isOn;
  }

  public void toggle() {
    // TODO Logging
    //    if (this.isOn) {
    //      getHub().log("Turning Lightbulb Off", Level.INFO, getIdentifier());
    //    } else {
    //      getHub().log("Turning Lightbulb On", Level.INFO, getIdentifier());
    //    }

    this.isOn = !this.isOn;
  }

  @Override
  public JSONObject getJSON() {
    JSONObject json = super.getJSON();
    json.put("device_type", "Lightbulb");
    JSONObject state = new JSONObject();
    state.put("is_on", this.isOn());
    json.put("state", state);
    return json;
  }
}
