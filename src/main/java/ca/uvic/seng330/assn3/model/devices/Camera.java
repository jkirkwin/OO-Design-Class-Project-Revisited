package ca.uvic.seng330.assn3.model.devices;

import ca.uvic.seng330.assn3.model.Hub;
import ca.uvic.seng330.assn3.model.HubRegistrationException;
import java.util.UUID;
import org.json.JSONObject;

public class Camera extends Device {

  private static int DEFAULT_MAX_SIZE = 50;
  private int diskSize;
  private final int maxSize;
  private boolean isRecording;
  private String videoSource;

  public Camera(Hub hub) {
    super(hub);
    this.isRecording = false;
    this.diskSize = 0;
    this.maxSize = DEFAULT_MAX_SIZE;
  }

  public Camera(String label, Hub hub) {
    super(label, Status.ON, hub);
    this.isRecording = false;
    this.diskSize = 0;
    this.maxSize = DEFAULT_MAX_SIZE;
  }

  protected Camera(int diskSize, int maxSize, boolean isRecording, UUID id, String label, Hub hub) {
    super(id, label, Status.ON, hub);
    this.isRecording = isRecording;
    this.diskSize = diskSize;
    this.maxSize = maxSize;
  }

  public boolean isRecording() {
    return this.isRecording;
  }

  public int currentDiskSize() {
    return this.diskSize;
  }

  public void emptyDisk() {
    if (this.getStatus() == Status.ERROR) {
      this.setStatus(Status.OFF);
    }
    this.diskSize = 0;
  }

  public void record() throws CameraFullException {
    if (this.getStatus() == Status.ON) {
      if (this.isRecording) {
        this.diskSize++;
        // TODO Logging
        //      getMediator().log("Increasing Disk Size. Turning camera off.", Level.INFO,
        // getIdentifier());
        //    } else {
        //      getMediator().log("Turning camera on.", Level.INFO, getIdentifier());
      }

      if (this.diskSize >= maxSize) {
        this.setStatus(Status.ERROR);
        //    TODO  getMediator().log("Camera full", Level.WARN, getIdentifier());
        try {
          getHub().alert("Camera Full", this);
        } catch (HubRegistrationException e) {
          //    TODO	  getHub().log("Failed to Alert Hub. Camera has been unregistered", Level.WARN,
          // getIdentifier());
        }
        throw new CameraFullException();
      }
      this.isRecording = !this.isRecording;
    } else {
      this.isRecording = false;
    }
  }

  @Override
  public JSONObject getJSON() {
    JSONObject json = super.getJSON();
    json.put("device_type", "Camera");
    JSONObject state = new JSONObject();
    state.put("disk_size", this.diskSize);
    state.put("max_size", this.maxSize);
    state.put("is_recording", this.isRecording());
    json.put("state", state);
    return json;
  }

  public int maxDiskSize() {
    return maxSize;
  }

  @Override
  public void setStatus(Status status) {
    if (status == Status.OFF && this.isRecording()) {
      try {
        this.record();
      } catch (CameraFullException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    this.status = status;
  }

  public String getEyes() {
    return this.videoSource;
  }

  public void setEyes(String newEyes) {
    this.videoSource = newEyes;
  }
}
