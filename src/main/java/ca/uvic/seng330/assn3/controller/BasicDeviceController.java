package ca.uvic.seng330.assn3.controller;

import java.util.UUID;

import ca.uvic.seng330.assn3.model.Hub;
import ca.uvic.seng330.assn3.model.devices.Device;
import ca.uvic.seng330.assn3.model.devices.Status;
import ca.uvic.seng330.assn3.view.CameraSceneBuilder;
import ca.uvic.seng330.assn3.view.Client;
import ca.uvic.seng330.assn3.view.LightbulbSceneBuilder;
import ca.uvic.seng330.assn3.view.SmartPlugSceneBuilder;
import ca.uvic.seng330.assn3.view.ThermostatSceneBuilder;

public class BasicDeviceController extends Controller {

	public BasicDeviceController(Hub hub, Client client) {
		super(hub, client);
		// TODO Auto-generated constructor stub
	}
	
	/*
	   * @pre uuid != null
	   */
	  public void handleDeviceViewClick(UUID uuid) {
	    assert uuid != null;
	    // TODO: review use of import of Device

	    views.push(ViewType.DEVICE_VIEW);
	    client.setTitle(ViewType.DEVICE_VIEW.toString());
	    deviceViewSwitch(uuid);
	  }
	  
	  /*
	   * Allows the skipping of views.push() etc...
	   */
	  protected void deviceViewSwitch(UUID uuid) {
	    switch (getDeviceType(hub.getDevice(uuid))) {
	      case CAMERA:
	        client.setView(new CameraSceneBuilder(this, "Back", uuid));
	        System.out.println("Camera View");
	        break;
	      case LIGHTBULB:
	        client.setView(new LightbulbSceneBuilder(this, "Back", uuid));
	        System.out.println("Lightbulb View");
	        break;
	      case SMARTPLUG:
	        client.setView(new SmartPlugSceneBuilder(this, "Back", uuid));
	        System.out.println("SmartPlug View");
	        break;
	      case THERMOSTAT:
	        client.setView(new ThermostatSceneBuilder(this, "Back", uuid));
	        System.out.println("Thermostat View");
	        break;
	    }
	  }
	  
	  public void toggleDevice(UUID id) {
		    Device curr = hub.getDevice(id);
		    if (curr.getStatus() == Status.ON) {
		      curr.setStatus(Status.OFF);
		    } else if (curr.getStatus() == Status.OFF) {
		      curr.setStatus(Status.ON);
		    } else {
		      // TODO: alert that device is broken.
		    }
		    deviceViewSwitch(id);
		  }

}
