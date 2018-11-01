package ca.uvic.seng330.assn3.model.devices;

@SuppressWarnings("serial")
public class CameraFullException extends Exception {

  public CameraFullException() {
    super();
  }

  public CameraFullException(String message) {
    super(message);
  }
}
