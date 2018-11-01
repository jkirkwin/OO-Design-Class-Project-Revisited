package ca.uvic.seng330.assn3.model.devices;

import ca.uvic.seng330.assn2.part1.Level;
import ca.uvic.seng330.assn2.part1.Mediator;

public class Lightbulb extends ConcreteDevice implements Device {

  private boolean isOn;

  /**
   * @pre aMediator != null
   * @param aMediator
   */
  public Lightbulb(Mediator aMediator) {
    super(aMediator);
    this.isOn = false;
    getMediator().log("Created new Lightbulb", Level.INFO, getIdentifier());
  }

  public boolean getCondition() {
    return this.isOn;
  }

  public void toggle() {
    if (this.isOn) {
      getMediator().log("Turning Lightbulb Off", Level.INFO, getIdentifier());
    } else {
      getMediator().log("Turning Lightbulb On", Level.INFO, getIdentifier());
    }

    this.isOn = !this.isOn;
  }
}
