package ca.jkirkwin.seng330.controller;

import ca.jkirkwin.seng330.logging.Logging;
import ca.jkirkwin.seng330.model.devices.Temperature;
import ca.jkirkwin.seng330.model.devices.Thermostat;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.UUID;
import javafx.scene.control.Alert.AlertType;
import org.slf4j.event.Level;

public class ThermostatController extends DeviceController {

  public ThermostatController(UUID id) {
    super(id);
  }

  public void setThermostatTemp(UUID id, double magnitude, Object degreeType) {
    assert id != null;
    assert degreeType != null;
    try {
      ((Thermostat) hub.getDevice(id))
          .setTemp(new Temperature(magnitude, (Temperature.Unit) degreeType));
    } catch (Temperature.TemperatureOutOfBoundsException e) {
      client.alertUser(
          AlertType.ERROR,
          "Invalid Temperature",
          "Invalid Temperature",
          "Try something more reasonable...");
      Logging.logWithID(
          "Bad Temperature input: " + magnitude + degreeType.toString(), id, Level.WARN);
    }
    deviceViewSwitch(id);
  }

  public double getThermostatTempMag(UUID id) {
    assert id != null;
    return ((Thermostat) hub.getDevice(id)).getTempMag();
  }

  public String getThermostatTempType(UUID id) {
    assert id != null;
    return String.valueOf(((Thermostat) hub.getDevice(id)).getTempType());
  }

  public void changeThermostatDegreeType(UUID id) {
    Thermostat thermostat = ((Thermostat) hub.getDevice(id));
    // TODO: set to max or min acceptable?
    try {
      thermostat.changeTempUnits();
    } catch (Temperature.TemperatureOutOfBoundsException e) {
      client.alertUser(
          AlertType.ERROR,
          "Temp Converted",
          "New " + thermostat.getTemp().toString(),
          thermostat.toString() + " cannot take temp oustide bounds");
      Logging.logWithID("Bad Temperature conversion", id, Level.WARN);
    }
    deviceViewSwitch(id);
  }

  public void constructTemp(UUID id, String newTempMag, Temperature.Unit degree) {
    try {
      setThermostatTemp(id, Double.parseDouble(newTempMag), degree);
    } catch (NumberFormatException e) {
      // TODO: alert to missing textfield
      Logging.logWithID("NumberFormatException", id, Level.INFO);
      assert false;
    }
  }

  // TODO Remove and instead use Unit.values()
  public ArrayList<Temperature.Unit> getThermostatDegreeTypes() {
    ArrayList<Temperature.Unit> degreeType = new ArrayList<Temperature.Unit>();
    EnumSet.allOf(Temperature.Unit.class).forEach(type -> degreeType.add(type));
    return degreeType;
  }
}
