package ca.uvic.seng330.assn3.model.devices;

import ca.uvic.seng330.assn2.part1.Level;
import ca.uvic.seng330.assn2.part1.Mediator;

public class SmartPlug extends ConcreteDevice implements Device {

  private boolean isOn;

  public SmartPlug(Mediator aMediator) {
    super(aMediator);
    this.isOn = false;
    getMediator().log("Created new SmartPlug", Level.INFO, getIdentifier());
  }

  public boolean isOn() {
    return this.isOn;
  }

  public void toggle() {
    if (this.isOn) {
      getMediator().log("Turning SmartPlug Off", Level.INFO, getIdentifier());
    } else {
      getMediator().log("Turning SmartPlug On", Level.INFO, getIdentifier());
    }
    this.isOn = !this.isOn;
  }
}
