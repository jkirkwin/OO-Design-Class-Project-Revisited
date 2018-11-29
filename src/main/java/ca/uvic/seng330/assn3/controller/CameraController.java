package ca.uvic.seng330.assn3.controller;

import ca.uvic.seng330.assn3.model.devices.Camera;
import ca.uvic.seng330.assn3.model.devices.CameraFullException;
import java.util.UUID;

public class CameraController extends DeviceController {

  public CameraController(UUID id) {
    super(id);
  }

  public boolean getCameraRecording() {
    assert id != null;
    // TODO: review importing devices.camera
    return ((Camera) hub.getDevice(id)).isRecording();
  }

  public void setCameraRecording() {
    assert id != null;
    try {
      ((Camera) hub.getDevice(id)).record();
    } catch (CameraFullException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
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
