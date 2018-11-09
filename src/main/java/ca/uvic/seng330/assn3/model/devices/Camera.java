package ca.uvic.seng330.assn3.model.devices;

import ca.uvic.seng330.assn3.model.Hub;
import ca.uvic.seng330.assn3.model.HubRegistrationException;
import org.json.JSONObject;

public class Camera extends Device {

  static int numCamera = 0;
  private int diskSize;
  private final int maxSize = 50;
  private boolean isRecording;

  public Camera(Hub hub) {
    super("Cam" + numCamera, Status.NORMAL, hub);
    this.isRecording = false;
    this.diskSize = 0;
  }
  
  public Camera( String label, Hub hub) {
    super(label, Status.NORMAL, hub);
    this.isRecording = false;
    this.diskSize = 0;
  }

  public boolean isRecording() {
    return this.isRecording;
  }

  public void record() throws CameraFullException {
    if (this.getStatus() != Status.ERROR) {
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
}
