package ca.jkirkwin.seng330.model.devices;

import ca.jkirkwin.seng330.logging.Logging;
import ca.jkirkwin.seng330.model.Hub;
import java.util.UUID;
import org.json.JSONObject;
import org.slf4j.event.Level;

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
    if (this.status.equals(Status.OFF)) {
      setStatus(Status.ON);
      Logging.logWithID("plug turned on", getIdentifier(), Level.INFO);
    } else if (this.status.equals(Status.ON)) {
      setStatus(Status.OFF);
      Logging.logWithID("plug turned off", getIdentifier(), Level.INFO);
    } else {
      Logging.logWithID(
          "Toggling plug failed. Not in ON or OFF state. Likely ERROR state.",
          getIdentifier(),
          Level.ERROR);
    }
    this.getHub()
        .notification(
            "SmartPlug " + this.getLabel() + " has been turned " + this.getStatus().toString(),
            this.getIdentifier());
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
