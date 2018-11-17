package ca.uvic.seng330.assn3.view;

import java.lang.reflect.InvocationTargetException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import static org.junit.Assert.assertTrue;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.LabeledMatchers.hasText;

import ca.uvic.seng330.assn3.controller.Controller;
import ca.uvic.seng330.assn3.model.Hub;
import ca.uvic.seng330.assn3.model.devices.Device;
import ca.uvic.seng330.assn3.model.devices.Lightbulb;
import ca.uvic.seng330.assn3.model.devices.SmartPlug;
import ca.uvic.seng330.assn3.model.devices.Status;
import ca.uvic.seng330.assn3.model.devices.Temperature;
import ca.uvic.seng330.assn3.model.devices.Temperature.Unit;
import ca.uvic.seng330.assn3.model.devices.Thermostat;
import ca.uvic.seng330.assn3.startup.Startup;
import javafx.stage.Stage;

public class TestDeviceUsage extends ApplicationTest {
  
  // TODO Add camera usage tests
  
  Hub hub;
  Controller controller;
  Client client;
  Startup app;
  
  @SuppressWarnings("unused")
  @Override
  public void start(Stage primaryStage) throws Exception {
    this.app = new Startup();
    this.app.start(primaryStage);
    this.hub = (Hub) GUITestUtilities.getAccessibleField(app, "hub").get(app);
    this.client = (Client) GUITestUtilities.getAccessibleField(app, "client").get(app);
    this.controller = (Controller) GUITestUtilities.getAccessibleField(app, "controller").get(app);
  }
  
  @Before
  @After
  public void setup() {
    try {
      GUITestUtilities.deleteState();
    } catch (NoSuchMethodException
        | SecurityException
        | IllegalAccessException
        | IllegalArgumentException
        | InvocationTargetException
        | NoSuchFieldException e) {
      e.printStackTrace();
    }
  }
  
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
    if(isAdmin) {
      GUITestUtilities.goToAdminHub(this);      
    } else {
      GUITestUtilities.goToBasicHub(this);
    }
    
    // Label
    clickOn(label);
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
    if(isAdmin) {
      GUITestUtilities.goToAdminHub(this);      
    } else {
      GUITestUtilities.goToBasicHub(this);
    }
    
    // Label
    clickOn(label);
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
    if(isAdmin) {
      GUITestUtilities.goToAdminHub(this);      
    } else {
      GUITestUtilities.goToBasicHub(this);
    }
    
    // Label
    clickOn(label);
    verifyThat("#current_label", hasText(label));
    doChangeLabelTest(s, "a different label");

    // Status
    doToggleTest(s);
  }  
}
