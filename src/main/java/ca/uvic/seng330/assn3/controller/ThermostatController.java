package ca.uvic.seng330.assn3.controller;

import ca.uvic.seng330.assn3.model.devices.Temperature;
import ca.uvic.seng330.assn3.model.devices.Temperature.TemperatureOutOfBoundsException;
import ca.uvic.seng330.assn3.model.devices.Temperature.Unit;
import ca.uvic.seng330.assn3.model.devices.Thermostat;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.UUID;
import javafx.scene.control.Alert.AlertType;

public class ThermostatController extends DeviceController {

  public ThermostatController(UUID id) {
    super(id);
  }

  public void setThermostatTemp(UUID id, double magnitude, Object degreeType) {
    assert id != null;
    assert degreeType != null;
    try {
      ((Thermostat) hub.getDevice(id)).setTemp(new Temperature(magnitude, (Unit) degreeType));
    } catch (TemperatureOutOfBoundsException e) {
      client.alertUser(
          AlertType.ERROR,
          "Invalid Temperature",
          "Invalid Temperature",
          "Try something more reasonable...");
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
    } catch (TemperatureOutOfBoundsException e) {
      client.alertUser(
          AlertType.ERROR,
          "Temp Converted",
          "New " + thermostat.getTemp().toString(),
          thermostat.toString() + " cannot take temp oustide bounds");
    }
    deviceViewSwitch(id);
  }

  public void constructTemp(UUID id, String newTempMag, Unit degree) {
    try {
      setThermostatTemp(id, Double.parseDouble(newTempMag), degree);
    } catch (NumberFormatException e) {
      // TODO: alert to missing textfield
      assert false;
    }
  }

  // TODO Remove and instead use Unit.values()
  public ArrayList<Unit> getThermostatDegreeTypes() {
    ArrayList<Unit> degreeType = new ArrayList<Unit>();
    EnumSet.allOf(Unit.class).forEach(type -> degreeType.add(type));
    return degreeType;
  }
}
