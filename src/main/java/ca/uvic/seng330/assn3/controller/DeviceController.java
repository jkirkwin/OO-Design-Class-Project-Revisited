package ca.uvic.seng330.assn3.controller;

import java.util.UUID;

public abstract class DeviceController extends Controller {

  // TODO Refactor DeviceSceneBuilder code to not pass UUIDs to the controller methods,
  // since we can now track the id of the current device here
  private UUID id;

  public DeviceController(UUID id) {
    assert id != null;
    this.id = id;
  }

  private UUID getDeviceID() {
    return this.id;
  }

  public String getStatus(UUID id) {
    return hub.getDevice(id).getStatus().toString();
  }

  public void changeDeviceLabel(UUID id, String newLabel) {
    assert id != null;
    assert newLabel != null;
    hub.getDevice(id).setLabel(newLabel);
    deviceViewSwitch(id);
  }
}
