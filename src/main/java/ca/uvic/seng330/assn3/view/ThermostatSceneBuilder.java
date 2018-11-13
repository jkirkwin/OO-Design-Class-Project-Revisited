package ca.uvic.seng330.assn3.view;

import ca.uvic.seng330.assn3.controller.Controller;
import java.util.UUID;
import javafx.scene.Node;
import javafx.scene.layout.HBox;

public class ThermostatSceneBuilder extends DeviceSceneBuilder {

  UUID deviceID;

  public ThermostatSceneBuilder(Controller controller, String backText, UUID id) {
    super(controller, backText);
    this.deviceID = id;
  }

  @Override
  protected Node buildSpecifics() {
    HBox basics = basicBuild(deviceID);

    return basics;
  }
}
