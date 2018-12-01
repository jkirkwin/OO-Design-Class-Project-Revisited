package ca.uvic.seng330.assn3.view;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import ca.uvic.seng330.assn3.model.devices.Camera;
import ca.uvic.seng330.assn3.model.devices.Device;
import ca.uvic.seng330.assn3.model.devices.Lightbulb;
import ca.uvic.seng330.assn3.model.devices.SmartPlug;
import ca.uvic.seng330.assn3.model.devices.Status;
import ca.uvic.seng330.assn3.model.devices.Thermostat;
import org.junit.Test;
import org.testfx.api.FxRobotException;

public class TestDeviceAdministration extends IOTApplicationGUITest {

  // Tests the device admin tasks possible from admin login -> manage devices

  public void makeDevice(String typeId, String status, String label) {
    clickOn("#" + typeId.toLowerCase());
    clickOn("#label");
    write(label);
    clickOn("#" + status.toLowerCase());
    clickOn("#create");
    clickOn("OK");
  }

  @Test
  public void testCreateCamera() {
    GUITestUtilities.goToCreateDevice(this);
    String label = "a camera";
    Status status = Status.OFF;
    makeDevice("camera", status.toString(), label);
    assertTrue(hub.isLabelUsed(label));
    assertTrue(hub.getDevice(hub.getFirstDeviceID(label)).getStatus().equals(status));
  }

  @Test
  public void testCreateLightBulb() {
    GUITestUtilities.goToCreateDevice(this);
    String label = "a bulb";
    Status status = Status.ON;
    makeDevice("lightbulb", status.toString(), label);
    assertTrue(hub.isLabelUsed(label));
    assertTrue(hub.getDevice(hub.getFirstDeviceID(label)).getStatus().equals(status));
  }

  @Test
  public void testCreateThermostat() {
    GUITestUtilities.goToCreateDevice(this);
    String label = "a stat";
    Status status = Status.OFF;
    makeDevice("thermostat", status.toString(), label);
    assertTrue(hub.isLabelUsed(label));
    assertTrue(hub.getDevice(hub.getFirstDeviceID(label)).getStatus().equals(status));
  }

  @Test
  public void testCreateSmartPlug() {
    GUITestUtilities.goToCreateDevice(this);
    String label = "a plug";
    Status status = Status.ON;
    makeDevice("smartplug", status.toString(), label);
    assertTrue(hub.isLabelUsed(label));
    assertTrue(hub.getDevice(hub.getFirstDeviceID(label)).getStatus().equals(status));
  }

  @Test
  public void testDevicesCreation() {
    String label;
    Status status;
    String[] deviceStrings = {"camera", "lightbulb", "smartplug", "thermostat"};
    GUITestUtilities.goToCreateDevice(this);
    for (int i = 0; i < 4; i++) {
      status = i % 2 == 0 ? Status.ON : Status.OFF;
      label = "a " + deviceStrings[i];
      makeDevice(deviceStrings[i], status.toString(), label);
      assertTrue(hub.isLabelUsed(label));
      assertTrue(hub.getDevice(hub.getFirstDeviceID(label)).getStatus().equals(status));
    }
  }

  @Test
  public void testDeviceDeletion() {
    Device[] devices = new Device[4];
    devices[0] = new Camera(hub);
    devices[1] = new Lightbulb(hub);
    devices[2] = new SmartPlug(hub);
    devices[3] = new Thermostat(hub);
    for (int i = 0; i < devices.length; i++) {
      devices[i].setLabel("label" + i);
    }
    GUITestUtilities.goToManageDevices(this);
    for (int i = 0; i < devices.length; i++) {
      Device d = devices[i];
      clickOn(d.getLabel());
      clickOn("OK");
      try {
        clickOn(d.getLabel());
        fail("Device not removed from management list");
      } catch (FxRobotException e) {
        assertFalse(hub.isRegisteredDevice(d.getIdentifier()));
      }
    }
  }
}
