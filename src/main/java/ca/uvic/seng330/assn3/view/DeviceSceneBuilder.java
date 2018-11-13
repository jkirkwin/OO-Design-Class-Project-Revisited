package ca.uvic.seng330.assn3.view;

import ca.uvic.seng330.assn3.controller.Controller;
import java.util.UUID;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public abstract class DeviceSceneBuilder extends SceneBuilder {

  public DeviceSceneBuilder(Controller controller, String backText) {
    super(controller, backText);
    // TODO Auto-generated constructor stub
  }

  private Node basicBuild(UUID id) {
    HBox hbox = new HBox(50);
    Button toggle = new Button("ON/OFF");
    toggle.setOnAction(event -> getController().toggleDevice(id));
    hbox.getChildren().add(toggle);
    return hbox;
  }
}
