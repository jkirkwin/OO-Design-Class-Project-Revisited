package ca.uvic.seng330.assn3.model.devices;

import ca.uvic.seng330.assn3.model.Hub;
import ca.uvic.seng330.assn3.model.devices.Temperature.TemperatureOutofBoundsException;
import ca.uvic.seng330.assn3.model.devices.Temperature.Unit;
import java.util.UUID;
import org.json.JSONObject;

public class Thermostat extends Device {

  private static final Temperature MIN_CELSIUS = new Temperature(-10.0, Unit.CELSIUS);
  private static final Temperature MAX_CELSIUS = new Temperature(40.0, Unit.CELSIUS);
  private static final Temperature MIN_FAHRENHEIT = new Temperature(0.0, Unit.FAHRENHEIT);
  private static final Temperature MAX_FAHRENHEIT = new Temperature(110.0, Unit.FAHRENHEIT);

  private Temperature temp;

  public Thermostat(Hub hub) {
    super(hub);
    this.temp = new Temperature(15.0, Unit.CELSIUS);
  }

  public Thermostat(String label, Hub hub) {
    super(label, Status.ON, hub);
    this.temp = new Temperature(15.0, Unit.CELSIUS);
  }

  protected Thermostat(Temperature temp, UUID id, String label, Hub hub) {
    super(id, label, Status.ON, hub);
    if (temp == null) {
      // TODO: temp shouldnt be null
      this.temp = temp;
    } else {
      this.temp = temp.clone();
    }
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
    if (this.temp == null) {
      return null;
    } else {
      return this.temp.clone();
    }
  }

  /** @pre pTemp != null */
  public void setTemp(Temperature temp) throws TemperatureOutofBoundsException {
    assert temp != null;
    // TODO log changing temp
    if (!isValidTemp(temp)) {
      // TODO log invalid temp
      throw (temp.new TemperatureOutofBoundsException());
    }
    this.temp = temp.clone();
  }

  @Override
  public JSONObject getJSON() {
    JSONObject json = super.getJSON();
    json.put("device_type", "Thermostat");
    JSONObject state = new JSONObject();
    if (this.getTemp() == null) {
      state.put("temp", JSONObject.NULL);
    } else {
      state.put("temp", this.getTemp().getJSON());
    }
    json.put("state", state);
    return json;
  }
}
