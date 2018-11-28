package ca.uvic.seng330.assn3.view.scenebuilders.devicebuilders;

import ca.uvic.seng330.assn3.controller.LightbulbController;
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
    return new HBox(); // TODO Add specific fomratting for lightbulb here
  }
}
