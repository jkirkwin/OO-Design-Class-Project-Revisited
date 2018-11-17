package ca.uvic.seng330.assn3.view;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.InvocationTargetException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testfx.api.FxRobotException;
import org.testfx.framework.junit.ApplicationTest;

import ca.uvic.seng330.assn3.controller.Controller;
import ca.uvic.seng330.assn3.model.Hub;
import ca.uvic.seng330.assn3.model.devices.Camera;
import ca.uvic.seng330.assn3.model.devices.Device;
import ca.uvic.seng330.assn3.model.devices.Lightbulb;
import ca.uvic.seng330.assn3.model.devices.SmartPlug;
import ca.uvic.seng330.assn3.model.devices.Status;
import ca.uvic.seng330.assn3.model.devices.Thermostat;
import ca.uvic.seng330.assn3.startup.Startup;
import javafx.stage.Stage;

public class TestDeviceAdministration extends ApplicationTest {
  // Should test the device admin tasks possible from admin login -> manage devices

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
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
  public void makeDevice(String typeId, String status, String label) {
    clickOn("#" + typeId.toLowerCase());
    clickOn("#label");
    write(label);
    clickOn("#" + status.toLowerCase());
    clickOn("#create");
    clickOn("OK");
  }
  
//  @Test
  public void testCreateCamera() {
    String label = "a camera";
    Status status = Status.OFF;
    makeDevice("camera", status.toString(), label);
    assertTrue(hub.isLabelUsed(label));
    assertTrue(hub.getDevice(hub.getFirstID(label)).getStatus().equals(status));
  }

//  @Test
  public void testCreateLightBulb() {
    String label = "a bulb";
    Status status = Status.ON;
    makeDevice("lightbulb", status.toString(), label);
    assertTrue(hub.isLabelUsed(label));
    assertTrue(hub.getDevice(hub.getFirstID(label)).getStatus().equals(status));

  }
  
//  @Test
  public void testCreateThermostat() {
    String label = "a stat";
    Status status = Status.OFF;
    makeDevice("thermostat", status.toString(), label);
    assertTrue(hub.isLabelUsed(label));
    assertTrue(hub.getDevice(hub.getFirstID(label)).getStatus().equals(status));
  }
  
//  @Test
  public void testCreateSmartPlug() {
    String label = "a plug";
    Status status = Status.ON;
    makeDevice("smartplug", status.toString(), label);
    assertTrue(hub.isLabelUsed(label));
    assertTrue(hub.getDevice(hub.getFirstID(label)).getStatus().equals(status));
  }
  
  @Test
  public void testDevicesCreation() {
    String label;
    Status status;
    String[] deviceStrings = new String[controller.getDeviceTypes().size()];
    for(int i = 0; i < 4; i++) {
      deviceStrings[i] = controller.getDeviceTypes().get(i).toString().toLowerCase();
    }
    GUITestUtilities.goToCreateDevice(this);
    for(int i = 0; i < 4; i++) {
      status = i%2 == 0 ? Status.ON : Status.OFF;
      label = "a " + deviceStrings[i];
      makeDevice(deviceStrings[i], status.toString(), label);
      assertTrue(hub.isLabelUsed(label));
      assertTrue(hub.getDevice(hub.getFirstID(label)).getStatus().equals(status));
    }
  }
  
  @Test
  public void testDeviceDeletion() {
    Device [] devices = new Device[4];
    devices[0] = new Camera(hub);
    devices[1] = new Lightbulb(hub);
    devices[2] = new SmartPlug(hub);
    devices[3] = new Thermostat (hub);
    for(int i = 0; i < devices.length; i++) {
      devices[i].setLabel("label" + i);
    }
    GUITestUtilities.goToManageDevices(this);    
    for(int i = 0; i < devices.length; i++) {
      Device d = devices[i];
      clickOn(d.getLabel());
      clickOn("OK");
      try {
        clickOn(d.getLabel());
        fail("Device not removed from management list");
      } catch(FxRobotException e) {
        assertFalse(hub.isRegisteredDevice(d.getIdentifier()));        
      }
    }     
  }
}
