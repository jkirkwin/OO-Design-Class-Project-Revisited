package ca.uvic.seng330.assn3.controller;

import ca.uvic.seng330.assn3.logging.Logging;
import ca.uvic.seng330.assn3.model.devices.Device;
import ca.uvic.seng330.assn3.model.devices.Status;
import java.util.UUID;

import org.slf4j.event.Level;

public abstract class DeviceController extends Controller {

  // TODO Refactor DeviceSceneBuilder code to not pass UUIDs to the controller methods,
  // since we can now track the id of the current device here
  protected UUID id;

  public DeviceController(UUID id) {
    assert id != null;
    this.id = id;
  }

  // TODO Remove id param
  public void toggleDevice(UUID id) {
    Device curr = hub.getDevice(id);
    if (curr.getStatus() == Status.ON) {
      curr.setStatus(Status.OFF);
    } else if (curr.getStatus() == Status.OFF) {
      curr.setStatus(Status.ON);
    } else {
    	Logging.logWithID("Device in ERROR state.", id, Level.WARN);
    }
    deviceViewSwitch(id);
  }

  private UUID getDeviceID() {
    return this.id;
  }

  // TODO Remove id param
  public String getStatus(UUID id) {
    return hub.getDevice(id).getStatus().toString();
  }

  // TODO Remove id param
  public void changeDeviceLabel(UUID id, String newLabel) {
    assert id != null;
    assert newLabel != null;
    hub.getDevice(id).setLabel(newLabel);
    deviceViewSwitch(id);
  }
}
