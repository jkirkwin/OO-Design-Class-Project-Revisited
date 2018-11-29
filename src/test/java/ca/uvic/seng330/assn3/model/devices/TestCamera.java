package ca.uvic.seng330.assn3.model.devices;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import ca.uvic.seng330.assn3.model.Hub;
import java.lang.reflect.Field;
import org.junit.Before;
import org.junit.Test;
import ca.uvic.seng330.assn3.IOTUnitTest;

public class TestCamera extends IOTUnitTest {
  private Hub h;

  @Before
  public void setup() {
    h = new Hub();
  }

  @Test
  public void testInitialState() {
    Camera a = new Camera(h);
    Camera b = new Camera("a label", h);

    assertTrue(a.getHub() == h);
    assertTrue(a.getStatus().equals(Status.ON));
    assertFalse(a.isRecording());
    assertTrue(a.getLabel().equals("Default Label"));
    assertTrue(a.currentDiskSize() == 0);
    assertTrue(b.getHub() == h);
    assertTrue(b.getStatus().equals(Status.ON));
    assertFalse(b.isRecording());
    assertTrue(b.getLabel().equals("a label"));
    assertTrue(b.currentDiskSize() == 0);
  }

  @Test
  public void testToggleStatus() {
    Camera a = new Camera(h);
    assertTrue(a.getStatus().equals(Status.ON));
    assertFalse(a.isRecording());
    a.setStatus(Status.OFF);
    assertTrue(a.getStatus().equals(Status.OFF));
    assertFalse(a.isRecording());
    a.setStatus(Status.ON);
    assertTrue(a.getStatus().equals(Status.ON));
    assertFalse(a.isRecording());

    Camera b = new Camera(h);
    try {
      b.record();
    } catch (CameraFullException e) {
      fail("unable to record");
    }
    assertTrue(b.getStatus().equals(Status.ON));
    assertTrue(b.isRecording());
    b.setStatus(Status.OFF);
    assertTrue(b.getStatus().equals(Status.OFF));
    assertFalse(b.isRecording());
    b.setStatus(Status.ON);
    assertTrue(b.getStatus().equals(Status.ON));
    assertFalse(b.isRecording());
  }

  @Test
  public void testCameraRecord() {
    Camera c = new Camera(h);
    assertFalse(c.isRecording());
    try {
      int initialSize = c.currentDiskSize();
      c.record();
      assertTrue(c.isRecording());
      assertTrue(initialSize == c.currentDiskSize());
      c.record();
      assertFalse(c.isRecording());
      assertTrue(initialSize + 1 == c.currentDiskSize());
    } catch (CameraFullException e) {
      fail("Camera unexpectedly full");
    }
  }

  @Test
  public void testThrowsCameraFullException() {
    // Camera should only throw the exception when the diskSize is too large and attempting to
    // *start* recording.
    Field diskSize = null;
    Camera c = new Camera(h);
    try {
      c.record();
    } catch (CameraFullException e1) {
      fail("Exception unexpected on init");
    }
    assertTrue(c.isRecording());
    try {
      diskSize = Camera.class.getDeclaredField("diskSize");
      diskSize.setAccessible(true);
      diskSize.set(c, 1000);
    } catch (NoSuchFieldException
        | SecurityException
        | IllegalArgumentException
        | IllegalAccessException e) {
      fail("unable to find or modify diskSize");
    }

    try {
      c.record();
      assertFalse(c.isRecording());
      c.record();
      fail("No exception thrown");
    } catch (CameraFullException e) {

    } catch (Exception e) {
      fail("Unexpected exception thrown");
    }
  }
}
