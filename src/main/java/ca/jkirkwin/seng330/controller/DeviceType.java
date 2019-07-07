package ca.jkirkwin.seng330.controller;

public enum DeviceType {
  CAMERA,
  SMARTPLUG,
  LIGHTBULB,
  THERMOSTAT;

  /*
   * Returns a more user-friendly name.
   * Ex if this is of type THERMOSTAT this method returns "Thermostat"
   */
  public String getEnglishName() {
    return this.toString().charAt(0)
        + this.toString().substring(1, this.toString().length()).toLowerCase();
  }
}
