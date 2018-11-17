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
  protected Status status;
  private Hub hub;

  /*
   * Returns the Device instance corresponding to the JSONObject
   * @pre o is a non-null well-formed JSONObject representation of an
   * instance of a subclass of Device
   * @pre h != null
   */
  public static Device getDeviceFromJSON(JSONObject o, Hub h) {
    assert o != null;
    assert h != null;

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
        d = new Camera(diskSize, maxSize, isRecording, dID, dLabel, h);
        break;

      case "Lightbulb":
        boolean isBulbOn = dState.getBoolean("is_on");
        d = new Lightbulb(isBulbOn, dID, dLabel, h);
        break;

      case "SmartPlug":
        boolean isPlugOn = dState.getBoolean("is_on");
        d = new SmartPlug(isPlugOn, dID, dLabel, h);
        break;

      case "Thermostat":
        Temperature temp = null;

        if (!dState.isNull("temp")) {
          JSONObject jsonTemp = dState.getJSONObject("temp");
          double magnitude = jsonTemp.getDouble("magnitude");
          Unit unit = jsonTemp.getEnum(Unit.class, "unit");
          temp = new Temperature(magnitude, unit);
        }

        d = new Thermostat(temp, dID, dLabel, h);
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
    this.status = Status.ON;
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
    return this.hub;
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
    json.put("status", this.getStatus().toString());
    json.put("label", this.getLabel());
    json.put("id", Storage.getJsonUUID(this.getIdentifier()));
    return json;
  }
}
