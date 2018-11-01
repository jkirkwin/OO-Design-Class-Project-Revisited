package ca.uvic.seng330.assn3.model.devices;

import java.util.UUID;

import ca.uvic.seng330.assn3.model.Hub;
import ca.uvic.seng330.assn3.model.HubRegistrationException;

public abstract class Device {
  private final UUID id;
  private String label;
  private Status status;
  private Hub hub;
	
  /*
   * @pre label != null
   * @pre status != null
   * @pre assert hub != null
   */
  public Device(String label, Status status, Hub hub) {
	assert label != null;
	assert status != null;
	assert hub != null;
	  
	this.label = label;
	this.status = status;
	this.hub = hub;
	this.id = UUID.randomUUID();
	
	try {
		hub.register(this);
	} catch (HubRegistrationException e) {
//		aMediator.log("Registration Failed : " + e.getMessage(), Level.ERROR, getIdentifier());
	}		  			 
  }
  
  /*
   * @pre hub != null
   */
  public Device(Hub hub) {
	  assert hub != null;
	  
		this.label = "Default Label";
		this.status = Status.NORMAL;
		this.hub = hub;
		this.id = UUID.randomUUID();
		
		try {
			hub.register(this);
		} catch (HubRegistrationException e) {
//			aMediator.log("Registration Failed : " + e.getMessage(), Level.ERROR, getIdentifier());
		} 
  }
  
  public UUID getIdentifier() {
	return this.id;
  }

  public Status getStatus() {
	  return this.status;
  }

  public void setStatus(Status status) {
	  this.status = status;
  }

  public Hub getHub() {
	  return this.getHub();
  }
}
