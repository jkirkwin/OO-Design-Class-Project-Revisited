package ca.uvic.seng330.assn3.view;

import java.lang.reflect.InvocationTargetException;

import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import ca.uvic.seng330.assn3.controller.Controller;
import ca.uvic.seng330.assn3.model.Hub;
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
  
  @Test
  public void testCreateCamera() {
    //TODO
    // create a camera and ensure it is registered to the hub
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
    GUITestUtilities.goToCreateDevice(this);
    GUITestUtilities.wait(2);
    try {
      clickOn("Camera"); 
    } catch(Exception e) {
      System.out.println("camera"); 
    }
    try {
      clickOn("Lightbulb");      
    } catch(Exception e) {
      System.out.println("lb");       
    }
    try {
      clickOn("SmartPlug");      
    } catch(Exception e) {
      System.out.println("sp"); 
    }
    try {
      clickOn("Thermostat");      
    } catch(Exception e) {
      System.out.println("therm"); 
    }      
  }
}
