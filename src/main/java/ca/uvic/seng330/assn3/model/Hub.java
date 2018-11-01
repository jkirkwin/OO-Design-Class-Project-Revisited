package ca.uvic.seng330.assn3.model;

import java.util.HashMap;
import java.util.UUID;

import ca.uvic.seng330.assn3.model.devices.Device;


public class Hub {
	private final HashMap<UUID, Device> deviceRegistry;

	public Hub() {
		this.deviceRegistry = new HashMap<UUID, Device>();
				
	}
	
	
	  public void register(Device aDevice) throws HubRegistrationException {
		  
	  }

	  public void unregister(Device aDevice) throws HubRegistrationException {
		  
	  }

	  public void log(String msg, UUID id) {
		// TODO  
	  }

	  public void alert(String msg, Device pDevice) throws HubRegistrationException {
		  
	  }
	  
}
