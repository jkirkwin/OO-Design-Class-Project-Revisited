package ca.uvic.seng330.assn3.model.devices;

import ca.uvic.seng330.assn3.model.Hub;
import ca.uvic.seng330.assn3.model.HubRegistrationException;
import ca.uvic.seng330.assn3.model.storage.StorageEntity;

import java.util.UUID;
import org.json.JSONObject;

public abstract class Device implements StorageEntity {
  private final UUID id;
  private String label;
  private Status status;
  private Hub hub;

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
    // TODO: confirm this is an unnecessary constructor
    assert hub != null;

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

    return json;
  }
}
