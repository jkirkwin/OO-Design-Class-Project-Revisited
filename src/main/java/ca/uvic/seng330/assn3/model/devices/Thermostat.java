package ca.uvic.seng330.assn3.model.devices;

import ca.uvic.seng330.assn3.model.Hub;
import ca.uvic.seng330.assn3.model.devices.Temperature.TemperatureOutofBoundsException;
import ca.uvic.seng330.assn3.model.devices.Temperature.Unit;
import org.json.JSONObject;

public class Thermostat extends Device {

  private static final Temperature MIN_CELSIUS = new Temperature(-10.0, Unit.CELSIUS);
  private static final Temperature MAX_CELSIUS = new Temperature(40.0, Unit.CELSIUS);
  private static final Temperature MIN_FAHRENHEIT = new Temperature(0.0, Unit.FAHRENHEIT);
  private static final Temperature MAX_FAHRENHEIT = new Temperature(110.0, Unit.FAHRENHEIT);

  static int numTherm = 0;
  private Temperature temp;

  public Thermostat(Hub hub) {
    super("Thermostat" + numTherm, Status.NORMAL, hub);
    this.temp = null;
  }
  
  public Thermostat(String label, Hub hub) {
    super(label, Status.NORMAL, hub);
    this.temp = null;
  }

  /**
   * @pre pTemp != null
   * @param pTemp
   * @return
   */
  private boolean isValidTemp(Temperature pTemp) {
    assert pTemp != null;
    double pMagnitude = pTemp.getMagnitude();
    switch (pTemp.getUnit()) {
      case CELSIUS:
        return MIN_CELSIUS.getMagnitude() <= pMagnitude && pMagnitude <= MAX_CELSIUS.getMagnitude();
      case FAHRENHEIT:
        return MIN_FAHRENHEIT.getMagnitude() <= pMagnitude
            && pMagnitude <= MAX_FAHRENHEIT.getMagnitude();
      default:
        return false;
    }
  }

  public Temperature getTemp() {
    return this.temp.clone();
  }

  /** @pre pTemp != null */
  public void setTemp(Temperature temp) throws TemperatureOutofBoundsException {
    assert temp != null;
    // TODO   getHub().log("Setting temp to " + pTemp, Level.INFO, getIdentifier());
    if (!isValidTemp(temp)) {
      //   TODO   getHub().log("invalid temperature given", Level.ERROR, getIdentifier());
      throw (temp.new TemperatureOutofBoundsException());
    }
    this.temp = temp.clone();
  }

  @Override
  public JSONObject getJSON() {
    JSONObject json = super.getJSON();
    json.put("device_type", "Thermostat");
    JSONObject state = new JSONObject();
    state.put("temp", this.getTemp().getJSON());
    return json;
  }
}
