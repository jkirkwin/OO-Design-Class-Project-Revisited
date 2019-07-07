package ca.jkirkwin.seng330.model.devices;

import ca.jkirkwin.seng330.logging.Logging;
import ca.jkirkwin.seng330.model.Hub;
import ca.jkirkwin.seng330.model.IOEEventType;
import java.util.UUID;
import org.json.JSONObject;
import org.slf4j.event.Level;

public class Thermostat extends Device {

  private static final Temperature MIN_CELSIUS = new Temperature(-10.0, Temperature.Unit.CELSIUS);
  private static final Temperature MAX_CELSIUS = new Temperature(40.0, Temperature.Unit.CELSIUS);
  private static final Temperature MIN_FAHRENHEIT =
      new Temperature(0.0, Temperature.Unit.FAHRENHEIT);
  private static final Temperature MAX_FAHRENHEIT =
      new Temperature(110.0, Temperature.Unit.FAHRENHEIT);
  private final double defaultMagnitude = 15.0;
  private final Temperature.Unit defaultUnit = Temperature.Unit.CELSIUS;

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
  public void setTemp(Temperature temp) throws Temperature.TemperatureOutOfBoundsException {
    assert temp != null;
    if (this.getStatus() == Status.ON) {
      if (!isValidTemp(temp)) {
        throw (temp.new TemperatureOutOfBoundsException());
      }
      this.temp = temp.clone();
    }
    Logging.logWithID("Modified temperature", getIdentifier(), Level.TRACE);
    this.getHub()
        .notification(
            "Thermostat " + this.getLabel() + " has new Temperature " + temp.toString(),
            this.getIdentifier());
  }

  public double getTempMag() {
    return this.getTemp().getMagnitude();
  }

  public Temperature.Unit getTempType() {
    return this.getTemp().getUnit();
  }

  public void ambientTempDetect() {
    this.getHub().notifyRoom(this.getIdentifier(), IOEEventType.AMBIENTTEMP);
    this.getHub()
        .notification(
            "Thermostat " + this.getLabel() + " has detected change in ambient Temperature",
            this.getIdentifier());
  }

  public void changeTempUnits() throws Temperature.TemperatureOutOfBoundsException {
    assert isValidTemp(this.temp);
    Temperature.Unit newUnit =
        this.getTempType().equals(Temperature.Unit.CELSIUS)
            ? Temperature.Unit.FAHRENHEIT
            : Temperature.Unit.CELSIUS;
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
