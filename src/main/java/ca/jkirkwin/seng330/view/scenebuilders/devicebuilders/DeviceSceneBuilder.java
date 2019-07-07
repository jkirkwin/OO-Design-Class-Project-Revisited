package ca.jkirkwin.seng330.view.scenebuilders.devicebuilders;

import ca.jkirkwin.seng330.controller.DeviceController;
import ca.jkirkwin.seng330.view.scenebuilders.SceneBuilder;
import java.util.UUID;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public abstract class DeviceSceneBuilder extends SceneBuilder {

  UUID deviceID;

  public DeviceSceneBuilder(DeviceController controller, String backText, UUID id) {
    super(controller, backText);
    this.deviceID = id;
  }

  @Override
  public DeviceController getController() {
    return (DeviceController) super.getController();
  }

  @Override
  protected Node buildCommon() {
    GridPane container = new GridPane();
    container.add(super.buildCommon(), 0, 0);

    VBox vbox = new VBox(20);
    vbox.setMinWidth(200);
    Label currentLabel = new Label(getController().getLabel(deviceID));
    currentLabel.setId("current_label");
    HBox devName = new HBox(10);
    devName.getChildren().add(new Label("Device Label -->"));
    devName.getChildren().add(currentLabel);
    vbox.getChildren().add(devName);

    TextField newLabel = new TextField();
    newLabel.setId("new_label");
    newLabel.setPromptText("New Label");
    vbox.getChildren().add(newLabel);
    Button confirmLabel = new Button("Change Label");
    confirmLabel.setId("change_label");
    confirmLabel.setOnAction(event -> getController().changeDeviceLabel(newLabel.getText()));
    vbox.getChildren().add(confirmLabel);

    HBox hbox = new HBox(30);
    hbox.getChildren().add(new Label("Device Status -->"));
    Button toggle = makeStatusToggle();
    hbox.getChildren().add(toggle);

    vbox.getChildren().add(hbox);
    hbox = new HBox(20);
    hbox.getChildren().add(vbox);

    container.add(hbox, 1, 1);
    return container;
  }

  protected Button makeStatusToggle() {
    Button toggle = new Button(getController().getStatus());
    toggle.setId("status_toggle");
    toggle.setOnAction(event -> getController().toggleDevice());
    return toggle;
  }
}
