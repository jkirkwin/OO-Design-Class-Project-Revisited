package ca.jkirkwin.seng330.controller.threading;

import ca.jkirkwin.seng330.controller.Controller;
import java.util.UUID;

public abstract class PropertyCheck implements Runnable {

  private UUID id;
  private Controller controller;
  private Object[] returnWrapper;

  public PropertyCheck(Controller controller, UUID id, Object[] returnWrapper) {
    assert id != null;
    assert controller != null;
    assert returnWrapper != null;
    this.id = id;
    this.controller = controller;
    this.returnWrapper = returnWrapper;
  }

  public Controller getController() {
    return controller;
  }

  public UUID getId() {
    return id;
  }

  public Object[] getReturnWrapper() {
    return returnWrapper;
  }
}
