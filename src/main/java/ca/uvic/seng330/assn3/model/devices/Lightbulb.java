package ca.uvic.seng330.assn3.model.devices;

import ca.uvic.seng330.assn3.model.Hub;

public class Lightbulb extends Device {

  static int numBulbs = 0;
  private boolean isOn;

  /*
   * @pre hub != null
   */
  public Lightbulb(Hub hub) {
    super("LightBulb" + numBulbs, Status.NORMAL, hub);
    this.isOn = false;
    //    getHub().log("Created new Lightbulb", Level.INFO, getIdentifier());
  }

  public boolean getCondition() {
    return this.isOn;
  }

  public void toggle() {
    //    if (this.isOn) {
    //      getHub().log("Turning Lightbulb Off", Level.INFO, getIdentifier());
    //    } else {
    //      getHub().log("Turning Lightbulb On", Level.INFO, getIdentifier());
    //    }

    this.isOn = !this.isOn;
  }
}
