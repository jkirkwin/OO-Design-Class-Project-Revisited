package ca.uvic.seng330.assn3.view;

import ca.uvic.seng330.assn3.controller.Controller;
import java.util.UUID;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public abstract class DeviceSceneBuilder extends SceneBuilder {

  public DeviceSceneBuilder(Controller controller, String backText) {
    super(controller, backText);
    // TODO Auto-generated constructor stub
  }

  protected HBox basicBuild(UUID id) {
    VBox vbox = new VBox(20);
    vbox.getChildren().add(new Label(getController().getLabel(id)));

    HBox hbox = new HBox(30);
    hbox.getChildren().add(new Label("Device Status -->"));
    Button toggle = new Button(getController().getStatus(id));
    toggle.setOnAction(event -> getController().toggleDevice(id));
    hbox.getChildren().add(toggle);

    vbox.getChildren().add(hbox);
    hbox = new HBox(20);
    hbox.getChildren().add(vbox);
    return hbox;
  }
}
