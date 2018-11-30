package ca.uvic.seng330.assn3.model.devices;

import ca.uvic.seng330.assn3.logging.Logging;
import ca.uvic.seng330.assn3.model.Hub;
import ca.uvic.seng330.assn3.model.IOEEventType;
import java.util.UUID;
import org.json.JSONObject;
import org.slf4j.event.Level;

public class Camera extends Device {

  private static int DEFAULT_MAX_SIZE = 50;
  private int diskSize;
  private final int maxSize;
  private boolean isRecording;

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
    Logging.logWithID("Disk cleared.", getIdentifier(), Level.INFO);
    this.getHub()
        .notification("Camera " + this.getLabel() + "'s disk wiped.", this.getIdentifier());
  }

  public void record() throws CameraFullException {
    if (this.getStatus() == Status.ON) {
      if (this.isRecording) {
        this.diskSize++;
      }
      if (this.diskSize >= maxSize) {
        this.setStatus(Status.ERROR);
        Logging.logWithID("Camera Full.", getIdentifier(), Level.WARN);
        getHub().alert("Camera Full", this); // TODO Do we still need this?
        throw new CameraFullException();
      }
      this.isRecording = !this.isRecording;
      Logging.logWithID(
          "Camera recording " + (isRecording ? "started" : "ended"), getIdentifier(), Level.INFO);
    } else {
      this.isRecording = false;
      Logging.logWithID("Camera turned off. Cannot record.", getIdentifier(), Level.WARN);
    }
  }

  public void motionDetect() {
    if(isRecording) {
      this.getHub().notifyRoom(this.getIdentifier(), IOEEventType.MOTIONALERT);
      this.getHub()
      .notification(
          "Camera " + this.getLabel() + " has detected motion in Room " + this.getRoom(),
          this.getIdentifier());      
    }
  }

  public void vacantDetect() {
    if(isRecording) {
      this.getHub().notifyRoom(this.getIdentifier(), IOEEventType.VACANTROOMALERT);
      this.getHub()
      .notification(
          "Camera " + this.getLabel() + " has detected vacancy in Room " + this.getRoom(),
          this.getIdentifier());      
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
      }
    }
    this.status = status;
    Logging.logWithID("Camera turned " + status.toString(), getIdentifier(), Level.INFO);
    this.getHub()
        .notification("Camera " + this.getLabel() + " has been turned OFF", this.getIdentifier());
  }
}
