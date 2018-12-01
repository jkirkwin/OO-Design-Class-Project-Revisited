package ca.uvic.seng330.assn3.model.devices;

import ca.uvic.seng330.assn3.logging.Logging;
import ca.uvic.seng330.assn3.model.Hub;
import ca.uvic.seng330.assn3.model.HubRegistrationException;
import ca.uvic.seng330.assn3.model.Room;
import ca.uvic.seng330.assn3.model.devices.Temperature.Unit;
import ca.uvic.seng330.assn3.model.storage.Storage;
import ca.uvic.seng330.assn3.model.storage.StorageEntity;
import java.util.UUID;
import org.json.JSONObject;
import org.slf4j.event.Level;

public abstract class Device implements StorageEntity {
  private final UUID id;
  private String label;
  protected Status status;
  private Hub hub;
  private Room room; // The room this device is associated with

  /*
   * Returns the Device instance corresponding to the JSONObject
   * @pre o is a non-null well-formed JSONObject representation of an
   * instance of a subclass of Device
   * @pre h != null
   */
  public static Device getDeviceFromJSON(JSONObject o, Hub h) {
    assert o != null;
    assert h != null;

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
        d = new Lightbulb(dID, dLabel, h);
        break;

      case "SmartPlug":
        d = new SmartPlug(dID, dLabel, h);
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
        Logging.log(
            "Failed to create Device from JSON. Likely passed invalid JSONObject.", Level.ERROR);
        return null;
    }
    Status dStatus = Status.valueOf(o.getString("status").toUpperCase());
    d.setStatus(dStatus);
    Object JSONroomId = o.get("room_id");
    if (JSONroomId.equals(JSONObject.NULL)) {
      d.room = null;
    } else {
      UUID roomId = Storage.getUUID((JSONObject) JSONroomId);
      d.room = h.getRoomByRoomID(roomId);
    }
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

    this.setLabel(label);
    this.status = status;
    this.hub = hub;
    this.id = id;
    this.room = null;

    try {
      hub.register(this);
    } catch (HubRegistrationException e) {
      Logging.logWithID("Registration Failed : " + e.getMessage(), getIdentifier(), Level.ERROR);
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

    this.setLabel(label);
    this.status = status;
    this.hub = hub;
    this.id = UUID.randomUUID();
    this.room = null;

    try {
      hub.register(this);
    } catch (HubRegistrationException e) {
      Logging.logWithID("Registration Failed : " + e.getMessage(), getIdentifier(), Level.ERROR);
    }
  }

  /*
   * @pre hub != null
   */
  public Device(Hub hub) {
    assert hub != null;

    this.setLabel("Default Label");
    this.status = Status.ON;
    this.hub = hub;
    this.id = UUID.randomUUID();
    this.room = null;

    try {
      hub.register(this);
    } catch (HubRegistrationException e) {
      Logging.logWithID("Registration Failed : " + e.getMessage(), getIdentifier(), Level.ERROR);
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
    hub.notification(this.getLabel() + " is now " + status.toString(), this.getIdentifier());
    Logging.logWithID("Device Status set to "+status.toString(), this.getIdentifier(), Level.INFO);
  }

  public Hub getHub() {
    return this.hub;
  }

  public void setLabel(String newLabel) {
    assert newLabel != null;
    this.label = newLabel;
    Logging.logWithID("Device Label set to "+newLabel, this.getIdentifier(), Level.INFO);
  }

  public String getLabel() {
    return label;
  }

  public Room getRoom() {
    return this.room;
  }

  public boolean hasRoom() {
    return this.room != null;
  }

  public void removeRoom() {
    this.room = null;
  }

  public void setRoom(Room r) {
    assert r != null;
    this.room = r;
    Logging.logWithID("Device Room set to "+r.getLabel(), this.getIdentifier(), Level.INFO);
  }

  /*
   * Should be extended by subClass for a more meaningful result
   */
  public JSONObject getJSON() {
    JSONObject json = new JSONObject();
    json.put("status", this.getStatus().toString());
    json.put("label", this.getLabel());
    json.put("id", Storage.getJsonUUID(this.getIdentifier()));
    if (hasRoom()) {
      json.put("room_id", Storage.getJsonUUID(this.room.getIdentifier()));
    } else {
      json.put("room_id", JSONObject.NULL);
    }
    return json;
  }
}
