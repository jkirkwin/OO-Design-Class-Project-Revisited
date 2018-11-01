package ca.uvic.seng330.assn3.model.devices;

import ca.uvic.seng330.assn3.model.Hub;
import ca.uvic.seng330.assn3.model.HubRegistrationException;

public class Camera extends Device {

  private int diskSize;
  private final int maxSize = 50;
  private boolean isRecording;

  public Camera(Hub hub) {
    super(hub);
    this.isRecording = false;
    this.diskSize = 0;
//    getMediator().log("Created Camera.", Level.INFO, getIdentifier());
  }

  public boolean isRecording() {
    return this.isRecording;
  }

  public void record() throws CameraFullException {
    if (this.isRecording) {
      this.diskSize++;
//      getMediator().log("Increasing Disk Size. Turning camera off.", Level.INFO, getIdentifier());
//    } else {
//      getMediator().log("Turning camera on.", Level.INFO, getIdentifier());
    }

    if (this.diskSize >= maxSize) {
      this.setStatus(Status.ERROR);
//      getMediator().log("Camera full", Level.WARN, getIdentifier());
      try {
        getHub().alert("Camera Full", this);
      } catch (HubRegistrationException e) {
//    	  getHub().log("Failed to Alert Hub. Camera has been unregistered", Level.WARN, getIdentifier());
      }
      throw new CameraFullException();
    }
    this.isRecording = !this.isRecording;
  }
}
