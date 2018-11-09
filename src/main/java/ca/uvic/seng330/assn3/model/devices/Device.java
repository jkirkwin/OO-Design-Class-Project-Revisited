package ca.uvic.seng330.assn3.model.devices;

import ca.uvic.seng330.assn3.model.Hub;
import ca.uvic.seng330.assn3.model.HubRegistrationException;
import ca.uvic.seng330.assn3.model.devices.Temperature.Unit;
import ca.uvic.seng330.assn3.model.storage.Storage;
import ca.uvic.seng330.assn3.model.storage.StorageEntity;
import java.util.UUID;
import org.json.JSONObject;

public abstract class Device implements StorageEntity {
  private final UUID id;
  private String label;
  private Status status;
  private Hub hub;

  /*
   * Returns the Device instance corresponding to the JSONObject
   * @pre o is a non-null well-formed JSONObject representation of an
   * instance of a subclass of Device
   * @pre h != null
   */
  public static Device getDeviceFromJSON(JSONObject o, Hub h) {
    assert o != null;

    // TODO Log device creation

    String dLabel = o.getString("label");

    UUID dID = Storage.getUUID(o.getJSONObject("id"));

    JSONObject dState = o.getJSONObject("state");

    Device d = null;
    switch (o.getString("device_type")) {
      case "Camera":
        int diskSize = dState.getInt("disk_size");
        int maxSize = dState.getInt("max_size");
        boolean isRecording = dState.getBoolean("is_recording");
        Camera c = new Camera(diskSize, maxSize, isRecording, dID, dLabel, h);
        d = c;
        break;

      case "Lightbulb":
        boolean isBulbOn = dState.getBoolean("is_on");
        Lightbulb l = new Lightbulb(isBulbOn, dID, dLabel, h);
        d = l;
        break;

      case "SmartPlug":
        boolean isPlugOn = dState.getBoolean("is_on");
        SmartPlug s = new SmartPlug(isPlugOn, dID, dLabel, h);
        d = s;
        break;

      case "Thermostat":
        JSONObject jsonTemp = dState.getJSONObject("temp");
        double magnitude = jsonTemp.getDouble("magnitude");
        Unit unit = Unit.valueOf(jsonTemp.getString("unit").toUpperCase());
        Thermostat t = new Thermostat(new Temperature(magnitude, unit), dID, dLabel, h);
        d = t;
        break;

      default:
        // Should not be here. JSONObject passed is invalid
        // TODO Log this error
        return null;
    }

    Status dStatus = Status.valueOf(o.getString("status").toUpperCase());
    d.setStatus(dStatus);

    return d;
  }

  /*
   * @pre label != null
   * @pre status != null
   * @pre assert hub != null
   * @pre id != null
   */
  public Device(UUID id, String label, Status status, Hub hub) {
    assert id != null;
    assert label != null;
    assert status != null;
    assert hub != null;

    // TODO Log device creation

    this.setLabel(label);
    this.status = status;
    this.hub = hub;
    this.id = id;

    try {
      hub.register(this);
    } catch (HubRegistrationException e) {
      //    aMediator.log("Registration Failed : " + e.getMessage(), Level.ERROR, getIdentifier());
    }
  }

  /*
   * @pre label != null
   * @pre status != null
   * @pre assert hub != null
   */
  public Device(String label, Status status, Hub hub) {
    assert label != null;
    assert status != null;
    assert hub != null;

    // TODO Log device creation

    this.setLabel(label);
    this.status = status;
    this.hub = hub;
    this.id = UUID.randomUUID();

    try {
      hub.register(this);
    } catch (HubRegistrationException e) {
      //		aMediator.log("Registration Failed : " + e.getMessage(), Level.ERROR, getIdentifier());
    }
  }

  /*
   * @pre hub != null
   */
  public Device(Hub hub) {
    assert hub != null;

    // TODO Log device creation

    this.setLabel("Default Label");
    this.status = Status.NORMAL;
    this.hub = hub;
    this.id = UUID.randomUUID();

    try {
      hub.register(this);
    } catch (HubRegistrationException e) {
      //			aMediator.log("Registration Failed : " + e.getMessage(), Level.ERROR, getIdentifier());
    }
  }

  public UUID getIdentifier() {
    return this.id;
  }

  public Status getStatus() {
    return this.status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public Hub getHub() {
    return this.getHub();
  }

  public void setLabel(String newLabel) {
    this.label = newLabel;
  }

  public String getLabel() {
    return label;
  }

  /*
   * Should be extended by subClass for a more meaningful result
   */
  public JSONObject getJSON() {
    JSONObject json = new JSONObject();
    json.put("status", this.getStatus());
    json.put("label", this.getLabel());
    json.put("id", Storage.getJsonUUID(this.getIdentifier()));
    return json;
  }
}
