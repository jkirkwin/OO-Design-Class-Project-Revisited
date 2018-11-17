package ca.uvic.seng330.assn3.model.devices;

import ca.uvic.seng330.assn3.model.Hub;
import java.util.UUID;
import org.json.JSONObject;

public class Lightbulb extends Device {

  public Lightbulb(Hub hub) {
    super(hub);
  }

  public Lightbulb(String label, Hub hub) {
    super(label, Status.ON, hub);
  }

  protected Lightbulb(UUID id, String label, Hub hub) {
    super(id, label, Status.ON, hub);
  }

  public boolean isOn() {
    return this.getStatus().equals(Status.ON);
  }

  public void toggle() {
    // TODO Log change
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
    json.put("device_type", "Lightbulb");
    JSONObject state = new JSONObject();
    json.put("state", state);
    return json;
  }
}
