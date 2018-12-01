package ca.uvic.seng330.assn3.view.scenebuilders.devicebuilders;

import ca.uvic.seng330.assn3.controller.SmartPlugController;
import java.util.UUID;
import javafx.scene.Node;
import javafx.scene.layout.HBox;

public class SmartPlugSceneBuilder extends DeviceSceneBuilder {

  public SmartPlugSceneBuilder(SmartPlugController controller, String backText, UUID id) {
    super(controller, backText, id);
  }

  @Override
  public SmartPlugController getController() {
    return (SmartPlugController) super.getController();
  }

  @Override
  protected Node buildSpecifics() {
    HBox hbox = new HBox();
    hbox.setMinSize(100, 100);
    return hbox; 
  }
}
