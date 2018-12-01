package ca.uvic.seng330.assn3.controller;

import ca.uvic.seng330.assn3.logging.Logging;
import ca.uvic.seng330.assn3.model.devices.Camera;
import ca.uvic.seng330.assn3.model.devices.CameraFullException;
import java.util.UUID;

import org.slf4j.event.Level;

public class CameraController extends DeviceController {

  public CameraController(UUID id) {
    super(id);
  }

  public boolean getCameraRecording() {
    assert id != null;
    return ((Camera) hub.getDevice(id)).isRecording();
  }

  public void setCameraRecording() {
    assert id != null;
    try {
      ((Camera) hub.getDevice(id)).record();
    } catch (CameraFullException e) {
    	Logging.log("CameraFullException", Level.WARN);
    }
    deviceViewSwitch(id);
  }

  public int getCurrCameraDiskSize() {
    assert id != null;
    return ((Camera) hub.getDevice(id)).currentDiskSize();
  }

  public int getMaxCameraDiskSize() {
    assert id != null;
    return ((Camera) hub.getDevice(id)).maxDiskSize();
  }

  public void emptyCameraDiskSize() {
    assert id != null;
    ((Camera) hub.getDevice(id)).emptyDisk();
    deviceViewSwitch(id);
  }

  @Override
  public void handleBackClick() {
    super.handleBackClick();
  }
}
