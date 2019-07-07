package ca.jkirkwin.seng330.view.scenebuilders.devicebuilders;

import ca.jkirkwin.seng330.controller.LightbulbController;
import java.util.UUID;
import javafx.scene.Node;
import javafx.scene.layout.HBox;

public class LightbulbSceneBuilder extends DeviceSceneBuilder {

  public LightbulbSceneBuilder(LightbulbController controller, String backText, UUID id) {
    super(controller, backText, id);
  }

  @Override
  public LightbulbController getController() {
    return (LightbulbController) super.getController();
  }

  @Override
  protected Node buildSpecifics() {
    HBox hbox = new HBox();
    hbox.setMinSize(100, 100);
    return hbox;
  }
}
