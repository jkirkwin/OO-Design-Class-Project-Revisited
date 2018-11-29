package ca.uvic.seng330.assn3.model.devices;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import ca.uvic.seng330.assn3.IOTUnitTest;
import ca.uvic.seng330.assn3.model.devices.Temperature.Unit;
import org.junit.Test;

public class TestTemperature extends IOTUnitTest {

  @Test
  public void testTempFromString() {
    Temperature oracle1 = new Temperature(2.12, Unit.CELSIUS);
    Temperature oracle2 = new Temperature(10.90, Unit.FAHRENHEIT);
    assertTrue(oracle1.equals(new Temperature(oracle1.toString())));
    assertTrue(oracle2.equals(new Temperature(oracle2.toString())));
  }

  @Test
  public void testTempEquals() {
    Temperature c1 = new Temperature(0.0, Unit.CELSIUS);
    Temperature c2 = new Temperature(0.0, Unit.CELSIUS);
    Temperature f1 = new Temperature(10.0, Unit.FAHRENHEIT);
    Temperature f2 = new Temperature(10.0, Unit.FAHRENHEIT);

    assertTrue(c1.equals(c2));
    assertTrue(f1.equals(f2));
    assertFalse(f1.equals(c1));
  }

  @Test
  public void testTempClone() {
    Temperature t = new Temperature(0.0, Unit.CELSIUS);
    Temperature tClone = null;
    try {
      tClone = t.clone();
      assertTrue(t.getMagnitude() == tClone.getMagnitude());
      assertTrue(t.getUnit() == tClone.getUnit());
    } catch (NullPointerException n) {
      fail("Unable to clone temperature");
    }
  }

  @Test
  public void testTempConversions() {
    Temperature c1 = new Temperature(0.0, Unit.CELSIUS);
    Temperature f1 = new Temperature(32.0, Unit.FAHRENHEIT);

    Temperature c2 = Temperature.convertTemp(f1, Unit.CELSIUS);
    Temperature f2 = Temperature.convertTemp(c1, Unit.FAHRENHEIT);

    assertTrue(c1.getMagnitude() == c2.getMagnitude());
    assertTrue(f1.getMagnitude() == f2.getMagnitude());
    assertTrue(f1.getMagnitude() == Temperature.convertTemp(f1, Unit.FAHRENHEIT).getMagnitude());
    assertTrue(c1.getMagnitude() == Temperature.convertTemp(c1, Unit.CELSIUS).getMagnitude());
  }
}
