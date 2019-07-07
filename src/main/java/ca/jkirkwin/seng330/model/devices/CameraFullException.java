package ca.jkirkwin.seng330.model.devices;

@SuppressWarnings("serial")
public class CameraFullException extends Exception {

  public CameraFullException() {
    super();
  }

  public CameraFullException(String message) {
    super(message);
  }
}
