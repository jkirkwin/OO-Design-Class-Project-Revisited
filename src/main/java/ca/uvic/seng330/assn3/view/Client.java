package ca.uvic.seng330.assn3.view;

import ca.uvic.seng330.assn3.controller.Controller;

public abstract class Client {

  private final Controller controller;
  
  /*
   * @pre c != null
   * @pre v != null
   */
  public Client(Controller c) {
    assert c != null;
    this.controller = c;
  }

  public abstract void setView(SceneBuilder builder);
}