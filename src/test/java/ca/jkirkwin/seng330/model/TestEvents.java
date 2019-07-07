package ca.jkirkwin.seng330.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import ca.jkirkwin.seng330.IOTUnitTest;
import ca.jkirkwin.seng330.model.devices.Camera;
import ca.jkirkwin.seng330.model.devices.CameraFullException;
import ca.jkirkwin.seng330.model.devices.Lightbulb;
import ca.jkirkwin.seng330.model.devices.SmartPlug;
import ca.jkirkwin.seng330.model.devices.Temperature;
import ca.jkirkwin.seng330.model.devices.Thermostat;
import org.junit.Before;
import org.junit.Test;

public class TestEvents extends IOTUnitTest {

  private Hub h;
  private Room r;
  private Camera c;
  private Lightbulb l1;
  private Lightbulb l2;
  private Thermostat t1;
  private Thermostat t2;
  private SmartPlug s;

  @Before
  public void setup() {
    h = new Hub();
    try {
      r = new Room("room label", h);
    } catch (HubRegistrationException e) {
      fail("setup failed. could not register r to h.");
      e.printStackTrace();
    }
    c = new Camera(h);
    l1 = new Lightbulb(h);
    l2 = new Lightbulb(h);
    t1 = new Thermostat(h);
    t2 = new Thermostat(h);
    try {
      t1.setTemp(t1.getDefaultTemperature());
    } catch (Temperature.TemperatureOutOfBoundsException e) {
      fail("Default temperature out of bounds");
    }
    s = new SmartPlug(h);

    r.addRoomDevice(c.getIdentifier());
    r.addRoomDevice(l2.getIdentifier());
    r.addRoomDevice(l1.getIdentifier());
    r.addRoomDevice(t1.getIdentifier());
    r.addRoomDevice(t2.getIdentifier());
    r.addRoomDevice(s.getIdentifier());
  }

  @Test
  public void testChangeInTemp() {
    Temperature newTemp1;
    Temperature newTemp2;
    try {
      newTemp1 = new Temperature(100, Temperature.Unit.FAHRENHEIT);
      newTemp2 = new Temperature(0, Temperature.Unit.CELSIUS);
      t1.setTemp(newTemp1);
      t2.setTemp(newTemp2);
      assertFalse(newTemp1.equals(t1.getDefaultTemperature()));
      assertFalse(newTemp2.equals(t2.getDefaultTemperature()));
    } catch (Temperature.TemperatureOutOfBoundsException e) {
      fail("invalid temperature");
    }
    t1.ambientTempDetect();
    assertTrue(t1.getDefaultTemperature().equals(t1.getTemp()));
    assertTrue(t2.getDefaultTemperature().equals(t2.getTemp()));
  }

  @Test
  public void testDetectVacancy() {
    l2.toggle();

    // Camera should only be able to recognize activity if it is recording
    assertFalse(c.isRecording());
    assertTrue(l1.isOn());
    assertFalse(l2.isOn());
    c.vacantDetect();
    assertFalse(c.isRecording());
    assertTrue(l1.isOn());
    assertFalse(l2.isOn());

    try {
      c.record();
      assertTrue(c.isRecording());
    } catch (CameraFullException e) {
      fail("unable to record");
    }
    c.vacantDetect();

    // All bulbs in the room should turn off
    assertFalse(l1.isOn());
    assertFalse(l2.isOn());
  }

  @Test
  public void testDetectMotion() {
    l2.toggle();

    // Camera should only be able to recognize activity if it is recording
    assertFalse(c.isRecording());
    assertTrue(l1.isOn());
    assertFalse(l2.isOn());
    c.motionDetect();
    assertFalse(c.isRecording());
    assertTrue(l1.isOn());
    assertFalse(l2.isOn());

    try {
      c.record();
      assertTrue(c.isRecording());
    } catch (CameraFullException e) {
      fail("unable to record");
    }
    c.motionDetect();

    // All bulbs in the room should turn on
    assertTrue(l1.isOn());
    assertTrue(l2.isOn());
  }
}
