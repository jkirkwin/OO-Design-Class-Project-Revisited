package ca.uvic.seng330.assn3.model.devices;

import ca.uvic.seng330.assn3.logging.Logging;
import ca.uvic.seng330.assn3.model.Hub;
import ca.uvic.seng330.assn3.model.IOEEventType;
import ca.uvic.seng330.assn3.model.devices.Temperature.TemperatureOutOfBoundsException;
import ca.uvic.seng330.assn3.model.devices.Temperature.Unit;
import java.util.UUID;
import org.json.JSONObject;
import org.slf4j.event.Level;

public class Thermostat extends Device {

  private static final Temperature MIN_CELSIUS = new Temperature(-10.0, Unit.CELSIUS);
  private static final Temperature MAX_CELSIUS = new Temperature(40.0, Unit.CELSIUS);
  private static final Temperature MIN_FAHRENHEIT = new Temperature(0.0, Unit.FAHRENHEIT);
  private static final Temperature MAX_FAHRENHEIT = new Temperature(110.0, Unit.FAHRENHEIT);
  private final double defaultMagnitude = 15.0;
  private final Unit defaultUnit = Unit.CELSIUS;

  private Temperature temp;

  public Temperature getDefaultTemperature() {
    return new Temperature(defaultMagnitude, defaultUnit);
  }

  public Thermostat(Hub hub) {
    super(hub);
    this.temp = new Temperature(defaultMagnitude, defaultUnit);
  }

  public Thermostat(String label, Hub hub) {
    super(label, Status.ON, hub);
    this.temp = new Temperature(defaultMagnitude, defaultUnit);
  }

  protected Thermostat(Temperature temp, UUID id, String label, Hub hub) {
    super(id, label, Status.ON, hub);
    assert temp != null;
    this.temp = temp.clone();
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
    assert this.temp != null;
    return this.temp.clone();
  }

  /** @pre pTemp != null */
  public void setTemp(Temperature temp) throws TemperatureOutOfBoundsException {
    assert temp != null;
    if (this.getStatus() == Status.ON) {
      if (!isValidTemp(temp)) {
        throw (temp.new TemperatureOutOfBoundsException());
      }
      this.temp = temp.clone();
    }
    Logging.logWithID("Modified temperature", getIdentifier(), Level.TRACE);
  }

  public double getTempMag() {
    return this.getTemp().getMagnitude();
  }

  public Unit getTempType() {
    return this.getTemp().getUnit();
  }

  public void ambientTempDetect() {
    this.getHub().notifyRoom(this.getIdentifier(), IOEEventType.AMBIENTTEMP);
  }

  public void changeTempUnits() throws TemperatureOutOfBoundsException {
    assert isValidTemp(this.temp);
    Unit newUnit = this.getTempType().equals(Unit.CELSIUS) ? Unit.FAHRENHEIT : Unit.CELSIUS;
    this.temp.changeUnit(newUnit);
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
