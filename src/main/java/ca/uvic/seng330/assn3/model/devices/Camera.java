package ca.uvic.seng330.assn3.model.devices;

import ca.uvic.seng330.assn2.part1.HubRegistrationException;
import ca.uvic.seng330.assn2.part1.Level;
import ca.uvic.seng330.assn2.part1.Mediator;

public class Camera extends ConcreteDevice implements Device {

  private int diskSize;
  private final int maxSize = 50;
  private boolean isRecording;

  /**
   * @pre aMediator
   * @param aMediator
   */
  public Camera(Mediator aMediator) {
    super(aMediator);
    this.isRecording = false;
    this.diskSize = 0;
    getMediator().log("Created Camera.", Level.INFO, getIdentifier());
  }

  public boolean isRecording() {
    return this.isRecording;
  }

  public void record() throws CameraFullException {
    if (this.isRecording) {
      this.diskSize++;
      getMediator().log("Increasing Disk Size. Turning camera off.", Level.INFO, getIdentifier());
    } else {
      getMediator().log("Turning camera on.", Level.INFO, getIdentifier());
    }

    if (this.diskSize >= maxSize) {
      this.setStatus(Status.ERROR);
      getMediator().log("Camera full", Level.WARN, getIdentifier());
      try {
        getMediator().alert("Camera Full", this);
      } catch (HubRegistrationException e) {
        getMediator()
            .log("Failed to Alert Hub. Camera has been unregistered", Level.WARN, getIdentifier());
      }
      throw new CameraFullException();
    }
    this.isRecording = !this.isRecording;
  }
}
