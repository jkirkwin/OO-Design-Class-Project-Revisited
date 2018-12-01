package ca.uvic.seng330.assn3.controller;

import ca.uvic.seng330.assn3.logging.Logging;
import ca.uvic.seng330.assn3.model.devices.Device;
import ca.uvic.seng330.assn3.model.devices.Status;
import java.util.UUID;
import javafx.scene.control.Alert.AlertType;
import org.slf4j.event.Level;

public abstract class DeviceController extends Controller {

  protected UUID id;

  public DeviceController(UUID id) {
    assert id != null;
    this.id = id;
  }

  public void toggleDevice() {
    Device curr = hub.getDevice(id);
    if (curr.getStatus() == Status.ON) {
      curr.setStatus(Status.OFF);
    } else if (curr.getStatus() == Status.OFF) {
      curr.setStatus(Status.ON);
    } else {
      client.alertUser(
          AlertType.ERROR, "Error status", "Device in error state", "Something when wrong!");
      Logging.logWithID("Device in ERROR state.", id, Level.WARN);
    }
    deviceViewSwitch(id);
  }

  private UUID getDeviceID() {
    return this.id;
  }

  public String getStatus() {
    return hub.getDevice(id).getStatus().toString();
  }

  public void changeDeviceLabel(String newLabel) {
    assert id != null;
    assert newLabel != null;
    hub.getDevice(id).setLabel(newLabel);
    deviceViewSwitch(id);
  }
}
