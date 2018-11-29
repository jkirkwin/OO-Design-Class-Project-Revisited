package ca.uvic.seng330.assn3.model.devices;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import ca.uvic.seng330.assn3.IOTUnitTest;
import ca.uvic.seng330.assn3.model.Hub;
import ca.uvic.seng330.assn3.model.devices.Temperature.TemperatureOutOfBoundsException;
import ca.uvic.seng330.assn3.model.devices.Temperature.Unit;
import org.junit.Before;
import org.junit.Test;

public class TestThermostat extends IOTUnitTest {
  private Hub h;
  private Temperature validCelsius;
  private Temperature validFahrenheit;

  // TODO add tests showing room-based events
  // i.e. ambienttempchange for thermostat, and motiondetected/emptyroom events for camera/lightbulb
  
  @Before
  public void setup() {
    h = new Hub();
    validCelsius = new Temperature(20, Unit.CELSIUS);
    validFahrenheit = new Temperature(80, Unit.FAHRENHEIT);
  }

  @Test
  public void testInitialState() {
    Thermostat a = new Thermostat(h);

    assertTrue(a.getHub() == h);
    assertTrue(a.getStatus().equals(Status.ON));
    assertTrue(a.getLabel().equals("Default Label"));
    assertTrue(a.getTemp().equals(a.getDefaultTemperature()));

    Thermostat b = new Thermostat("a label", h);
    assertTrue(b.getHub() == h);
    assertTrue(b.getStatus().equals(Status.ON));
    assertTrue(b.getLabel().equals("a label"));
  }

  @Test
  public void testToggleStatus() {
    Thermostat a = new Thermostat(h);
    try {
      a.setTemp(validCelsius);
    } catch (TemperatureOutOfBoundsException e) {
      fail("unable to set temp");
    }
    assertTrue(a.getStatus().equals(Status.ON));
    a.setStatus(Status.OFF);
    assertTrue(a.getStatus().equals(Status.OFF));
    a.setStatus(Status.ON);
    assertTrue(a.getStatus().equals(Status.ON));
  }

  @Test
  public void testInvalidTemp() {
    Thermostat t = new Thermostat(h);
    try {
      t.setTemp(new Temperature(100, Unit.CELSIUS));
      fail("Rediculous temperature set without exception");
    } catch (TemperatureOutOfBoundsException e) {
    }

    try {
      t.setTemp(new Temperature(150, Unit.FAHRENHEIT));
      fail("Rediculous temperature set without exception");
    } catch (TemperatureOutOfBoundsException e) {
    }

    try {
      t.setTemp(new Temperature(-100, Unit.CELSIUS));
      fail("Rediculous temperature set without exception");
    } catch (TemperatureOutOfBoundsException e) {
    }

    try {
      t.setTemp(new Temperature(-50, Unit.FAHRENHEIT));
      fail("Rediculous temperature set without exception");
    } catch (TemperatureOutOfBoundsException e) {
    }
  }
}
