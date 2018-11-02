package ca.uvic.seng330.assn3.model.devices;

import ca.uvic.seng330.assn3.model.Hub;

public class SmartPlug extends Device {

  private boolean isOn;

  public SmartPlug(Hub hub) {
    super(hub);
    this.isOn = false;
    //    getMediator().log("Created new SmartPlug", Level.INFO, getIdentifier());
  }

  public boolean isOn() {
    return this.isOn;
  }

  public void toggle() {
    //    if (this.isOn) {
    //      getMediator().log("Turning SmartPlug Off", Level.INFO, getIdentifier());
    //    } else {
    //      getMediator().log("Turning SmartPlug On", Level.INFO, getIdentifier());
    //    }
    this.isOn = !this.isOn;
  }
}
