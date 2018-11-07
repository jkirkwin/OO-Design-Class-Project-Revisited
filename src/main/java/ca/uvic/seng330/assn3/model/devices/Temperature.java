package ca.uvic.seng330.assn3.model.devices;

import org.json.JSONObject;

public class Temperature implements Cloneable {

  @SuppressWarnings("serial")
  public class TemperatureOutofBoundsException extends Exception {
    public TemperatureOutofBoundsException() {
      super();
    }

    public TemperatureOutofBoundsException(String message) {
      super(message);
    }
  }

  private double magnitude;
  private Unit unit;

  public enum Unit {
    CELSIUS,
    FAHRENHEIT;
  }

  /**
   * @pre unit != null
   * @param magnitude
   * @param unit
   */
  public Temperature(double magnitude, Unit unit) {
    assert unit != null;
    this.setMagnitude(magnitude);
    this.setUnit(unit);
  }

  @Override
  public Temperature clone() {
    try {
      Temperature tClone = (Temperature) super.clone();
      tClone.setUnit(this.getUnit());
      tClone.setMagnitude(this.getMagnitude());
      return tClone;
    } catch (CloneNotSupportedException e) {
      return null;
    }
  }

  public Unit getUnit() {
    return unit;
  }

  /**
   * @pre unit != null
   * @param unit
   */
  public void setUnit(Unit unit) {
    // TODO: shouldn't this call convertTemp(this.magnitude, unit)?
    assert unit != null;
    this.unit = unit;
  }

  public double getMagnitude() {
    return magnitude;
  }

  public void setMagnitude(double magnitude) {
    this.magnitude = magnitude;
  }

  public String toString() {
    return magnitude + " degrees " + this.unit.toString();
  }

  /*
   * Returns true if and only if the two instances represent the
   * same temperature using the same unit.
   * @pre other != null
   */
  public boolean equals(Temperature other) {
    return this.magnitude == other.magnitude && this.unit == other.unit;
  }

  /*
   * Returns an equivalent temperature to the one passed in which uses desiredUnit as the Unit.
   * Note: As defined, these two temperature objects are *not* guaranteed to be considered equal.
   * See equals() for more information.
   * @pre temp != null
   * @pre temp.getUnit() != null
   * @pre desiredUnit != null
   */
  public static Temperature convertTemp(Temperature temp, Unit desiredUnit) {
    assert temp != null;
    assert temp.getUnit() != null;
    assert desiredUnit != null;

    Unit tUnit = temp.getUnit();
    if (tUnit == desiredUnit) {
      return temp.clone();
    } else if (temp.getUnit() == Unit.CELSIUS) {
      return new Temperature(celsiusToFahrenheit(temp.getMagnitude()), desiredUnit);
    } else {
      return new Temperature(fahrenheitToCelsius(temp.getMagnitude()), desiredUnit);
    }
  }

  private static double celsiusToFahrenheit(double cMagnitude) {
    return (cMagnitude * 1.8) + 32;
  }

  private static double fahrenheitToCelsius(double fMagnitude) {
    return (fMagnitude - 32) * 5.0 / 9.0;
  }

  public JSONObject getJSON() {
    JSONObject json = new JSONObject();
    json.put("unit", this.getUnit());
    json.put("magnitude", this.getMagnitude());

    return json;
  }
}
