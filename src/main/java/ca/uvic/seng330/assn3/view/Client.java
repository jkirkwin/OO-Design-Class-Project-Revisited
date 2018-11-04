package ca.uvic.seng330.assn3.view;

import java.util.Observable;

import ca.uvic.seng330.assn3.controller.Controller;

public abstract class Client extends Observable {
  
  private Controller controller;
  
  public Client(Controller controller) {
    this.controller = controller;
  }
}
