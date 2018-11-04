package ca.uvic.seng330.assn3.view;

import ca.uvic.seng330.assn3.controller.Controller;

public class FXClient extends Client {

  // instance variables should include common scenes -> login, User Hub View, Device view for each
  // device
  // could use an Enum to keep track of state - that is, which scene is currently displayed
  
  /*
   * @pre c != null
   * @pre v != null
   */
  public FXClient(Controller controller, ViewType viewType) {
    super(controller, viewType);
  }
}
