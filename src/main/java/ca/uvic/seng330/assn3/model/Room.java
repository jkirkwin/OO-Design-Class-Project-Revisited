package ca.uvic.seng330.assn3.model;

import ca.uvic.seng330.assn3.model.storage.Storage;
import ca.uvic.seng330.assn3.model.storage.StorageEntity;
import java.util.UUID;
import org.json.JSONObject;

public class Room implements StorageEntity {

  private final UUID id;
  private String label;
  private final Hub hub;

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
    hub.addRoom(this);
  }

  public String getLabel() {
    return this.label;
  }

  public void setLabel(String newLabel) {
    assert newLabel != null;
    this.label = newLabel;
  }

  public UUID getID() {
    return this.id;
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
    return new Room(label, id, hub);
  }
}
