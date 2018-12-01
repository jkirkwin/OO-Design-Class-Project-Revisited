package ca.uvic.seng330.assn3.view;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.LabeledMatchers.hasText;

import ca.uvic.seng330.assn3.model.devices.Camera;
import ca.uvic.seng330.assn3.model.devices.CameraFullException;
import ca.uvic.seng330.assn3.model.devices.Device;
import ca.uvic.seng330.assn3.model.devices.Lightbulb;
import ca.uvic.seng330.assn3.model.devices.SmartPlug;
import ca.uvic.seng330.assn3.model.devices.Status;
import ca.uvic.seng330.assn3.model.devices.Temperature;
import ca.uvic.seng330.assn3.model.devices.Temperature.Unit;
import ca.uvic.seng330.assn3.model.devices.Thermostat;
import org.junit.Test;

public class TestDeviceUsage extends IOTApplicationGUITest {

  /*
   * change label
   * check displayed label isUpdated
   * check model label is updated
   */
  public void doChangeLabelTest(Device d, String newLabel) {
    clickOn("#new_label");
    write(newLabel);
    clickOn("#change_label");
    verifyThat("#current_label", hasText(newLabel));
    assertTrue(newLabel.equals(d.getLabel()));
  }

  public void doToggleTest(Device d) {
    verifyThat("#status_toggle", hasText("ON"));
    assertTrue(d.getStatus().equals(Status.ON));
    clickOn("#status_toggle");
    verifyThat("#status_toggle", hasText("OFF"));
    assertTrue(d.getStatus().equals(Status.OFF));
  }

  @Test
  public void testThermostatUsage() {
    Thermostat t1 = new Thermostat("Thermostat1", hub);
    Thermostat t2 = new Thermostat("Thermostat2", hub);
    doTestThermostatUsage(true, t1);
    GUITestUtilities.backToLogin(this, client);
    doTestThermostatUsage(false, t2);
  }

  public void doTestThermostatUsage(boolean isAdmin, Thermostat t) {
    String label = t.getLabel();
    if (isAdmin) {
      GUITestUtilities.goToAdminHub(this);
    } else {
      GUITestUtilities.goToBasicHub(this);
    }

    // Label
    clickOn("#" + label);
    verifyThat("#current_label", hasText(label));
    doChangeLabelTest(t, "a different label");

    // Check temp displayed is correct
    verifyThat("#current_temp", hasText(t.getTemp().toString()));
    double newMagnitude = 66.2;

    // Change the temperature and verify it updates
    Temperature newTemp = new Temperature(newMagnitude, Unit.FAHRENHEIT);
    clickOn("#temp_field");
    write(Double.toString(newMagnitude));
    clickOn("#" + newTemp.getUnit().toString().toLowerCase());
    clickOn("#set_temp");
    verifyThat("#current_temp", hasText(newTemp.toString()));
    assertTrue(t.getTemp().equals(newTemp));

    // Use change unit toggle
    newTemp.changeUnit(Unit.CELSIUS);
    clickOn("#change_units");
    verifyThat("#current_temp", hasText(newTemp.toString()));
    assertTrue(t.getTemp().equals(newTemp));

    // Set invalid temperature
    double rediculousMagnitude = -100.102;
    newTemp = new Temperature(rediculousMagnitude, Unit.CELSIUS);
    clickOn("#temp_field");
    write(Double.toString(rediculousMagnitude));
    clickOn("#" + newTemp.getUnit().toString().toLowerCase());
    clickOn("#set_temp");
    clickOn("OK");

    doToggleTest(t);
  }

  @Test
  public void testLightbulbUsage() {
    Lightbulb l1 = new Lightbulb("lightbulb1", hub);
    Lightbulb l2 = new Lightbulb("lightbulb2", hub);
    doTestLightbulbUsage(true, l1);
    GUITestUtilities.backToLogin(this, client);
    doTestLightbulbUsage(false, l2);
  }

  public void doTestLightbulbUsage(boolean isAdmin, Lightbulb l) {
    String label = l.getLabel();
    if (isAdmin) {
      GUITestUtilities.goToAdminHub(this);
    } else {
      GUITestUtilities.goToBasicHub(this);
    }

    // Label
    clickOn("#" + label);
    verifyThat("#current_label", hasText(label));
    doChangeLabelTest(l, "a different label");

    // Status
    doToggleTest(l);
  }

  @Test
  public void testSmartPlugUsage() {
    SmartPlug s1 = new SmartPlug("plug1", hub);
    SmartPlug s2 = new SmartPlug("plug2", hub);
    doTestSmartPlugUsage(true, s1);
    GUITestUtilities.backToLogin(this, client);
    doTestSmartPlugUsage(false, s2);
  }

  public void doTestSmartPlugUsage(boolean isAdmin, SmartPlug s) {
    String label = s.getLabel();
    if (isAdmin) {
      GUITestUtilities.goToAdminHub(this);
    } else {
      GUITestUtilities.goToBasicHub(this);
    }

    // Label
    clickOn("#" + label);
    verifyThat("#current_label", hasText(label));
    doChangeLabelTest(s, "a different label");

    // Status
    doToggleTest(s);
  }

  @Test
  public void testCameraUsage() {
    Camera c1 = new Camera("cam1", hub);
    Camera c2 = new Camera("cam2", hub);
    doTestCameraUsage(true, c1);
    GUITestUtilities.backToLogin(this, client);
    doTestCameraUsage(false, c2);
  }

  private void doTestCameraUsage(boolean isAdmin, Camera c) {
    String label = c.getLabel();
    if (isAdmin) {
      GUITestUtilities.goToAdminHub(this);
    } else {
      GUITestUtilities.goToBasicHub(this);
    }

    // Label
    clickOn("#" + label);
    verifyThat("#current_label", hasText(label));
    doChangeLabelTest(c, "a different label");

    // Status
    doToggleTest(c);
  }

  @Test
  public void testCameraStatus() {
    // Toggle status should prevent recording
    Camera c = new Camera("issacamera", hub);
    GUITestUtilities.goToBasicHub(this);
    clickOn("#" + c.getLabel());
    GUITestUtilities.wait(3);
    clickOn("#record");
    verifyThat("#record", hasText("true"));
    clickOn("#status_toggle");
    verifyThat("#record", hasText("false"));
    clickOn("#record");
    verifyThat("#record", hasText("false"));
  }

  @Test
  public void testCameraDiskSpace() {
    // Reset disk space
    String label = "cam";
    Camera c = new Camera(label, hub);
    while (c.currentDiskSize() < 10) {
      try {
        c.record();
      } catch (CameraFullException e) {
        fail();
      }
    }
    GUITestUtilities.goToBasicHub(this);
    clickOn("#" + c.getLabel());
    verifyThat("#empty_disk", hasText(c.currentDiskSize() + "/" + c.maxDiskSize()));
    clickOn("#empty_disk");
    verifyThat("#empty_disk", hasText("0/" + c.maxDiskSize()));
    assertTrue(c.currentDiskSize() == 0);

    // verify diskspace error is shown
    GUITestUtilities.backToLogin(this, client);
    while (c.currentDiskSize() < c.maxDiskSize() - 1) {
      try {
        c.record();
      } catch (CameraFullException e) {
        fail();
      }
    }
    GUITestUtilities.goToAdminHub(this);
    clickOn("#" + c.getLabel());
    verifyThat("#empty_disk", hasText(c.currentDiskSize() + "/" + c.maxDiskSize()));
    clickOn("#record");
    clickOn("#record");
    clickOn("OK");
  }
}
