package ca.uvic.seng330.assn3.model.devices;

public class Temperature {

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
      return (Temperature) super.clone();
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
}
