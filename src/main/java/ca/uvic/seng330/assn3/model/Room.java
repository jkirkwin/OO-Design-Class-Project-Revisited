package ca.uvic.seng330.assn3.model;

import ca.uvic.seng330.assn3.logging.Logging;
import ca.uvic.seng330.assn3.model.devices.Device;
import ca.uvic.seng330.assn3.model.devices.Status;
import ca.uvic.seng330.assn3.model.devices.Temperature.TemperatureOutOfBoundsException;
import ca.uvic.seng330.assn3.model.devices.Thermostat;
import ca.uvic.seng330.assn3.model.storage.Storage;
import ca.uvic.seng330.assn3.model.storage.StorageEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.json.JSONObject;
import org.slf4j.event.Level;

public class Room implements StorageEntity {

  private final UUID id;
  private String label;
  private final Hub hub;
  private ArrayList<Device> occupants = new ArrayList<Device>();

  public Room(String label, Hub h) throws HubRegistrationException {
    this(label, UUID.randomUUID(), h);
  }

  private Room(String label, UUID id, Hub h) throws HubRegistrationException {
    assert label != null;
    assert id != null;
    assert h != null;
    this.id = id;
    this.label = label;
    this.hub = h;
    hub.register(this);
  }

  public String getLabel() {
    return this.label;
  }

  public void setLabel(String newLabel) {
    assert newLabel != null;
    this.label = newLabel;
    Logging.logWithID("Modified room label", getIdentifier(), Level.TRACE);
  }

  public UUID getIdentifier() {
    return this.id;
  }

  public void addRoomDevice(UUID id) {
    assert id != null;
    assert hub.isRegisteredDevice(id);
    Device d = hub.getDevice(id);
    occupants.add(d);
    if (!d.hasRoom() || d.getRoom() != this) {
      d.setRoom(this);
    }
    Logging.logWithID("Added device to room", id, Level.INFO);
  }

  private void setRoomContents() {
    this.occupants = (ArrayList<Device>) hub.getRoomContents(this);
  }

  public void notifyOccupants(IOEEventType event) {
    for (int i = 0; i < occupants.size(); i++) {
      Device curr = occupants.get(i);
      String devType = getDeviceType(curr);
      switch (event) {
        case MOTIONALERT:
          if (devType == "LIGHTBULB") {
            curr.setStatus(Status.ON);
          }
          break;
        case VACANTROOMALERT:
          if (devType == "LIGHTBULB") {
            curr.setStatus(Status.OFF);
          }
          break;
        case AMBIENTTEMP:
          if (devType == "THERMOSTAT") {
            try {
              ((Thermostat) curr).setTemp(((Thermostat) curr).getDefaultTemperature());
            } catch (TemperatureOutOfBoundsException e) {
              Logging.logWithID("Failed to set defualt temp", curr.getIdentifier(), Level.ERROR);
            }
          }
          break;
      }
    }
    Logging.logWithID("Notified roome occupants of event: " + event.toString(), getIdentifier(), Level.INFO);
  }

  protected String getDeviceType(Device d) {
    return d.getClass().getSimpleName().toUpperCase();
  }

  @Override
  public boolean equals(Object other) {
    if (!(other instanceof Room)) {
      return false;
    }
    Room otherRoom = (Room) other;
    return this.label.equals(otherRoom.label) && this.id.equals(otherRoom.id);
  }

  @Override
  public JSONObject getJSON() {
    JSONObject j = new JSONObject();
    j.put("label", this.label);
    j.put("id", Storage.getJsonUUID(id));
    return j;
  }

  public static Room getRoomFromJSON(JSONObject jsonObject, Hub hub)
      throws HubRegistrationException {
    UUID id = Storage.getUUID(jsonObject.getJSONObject("id"));
    String label = jsonObject.getString("label");
    Room rebuilt = new Room(label, id, hub);
    rebuilt.setRoomContents();
    return rebuilt;
  }

  public List<Device> getOccupants() {
    return occupants;
  }

  public void empty() {
    for (Device d : occupants) {
      d.removeRoom();
      occupants.remove(d);
    }
    Logging.logWithID("Room emptied", getIdentifier(), Level.INFO);
  }

  public void removeRoomDevice(Device retiredDevice) {
    assert occupants.contains(retiredDevice);
    assert retiredDevice.hasRoom();
    assert retiredDevice.getRoom() == this;
    retiredDevice.removeRoom();
    occupants.remove(retiredDevice);
    Logging.logWithID("Removed device from room", retiredDevice.getIdentifier(), Level.INFO);
  }
}
