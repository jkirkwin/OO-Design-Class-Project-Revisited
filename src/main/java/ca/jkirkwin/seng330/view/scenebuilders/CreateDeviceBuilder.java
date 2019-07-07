package ca.jkirkwin.seng330.view.scenebuilders;

import ca.jkirkwin.seng330.controller.DeviceType;
import ca.jkirkwin.seng330.controller.ManageDevicesController;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class CreateDeviceBuilder extends SceneBuilder {

  DeviceType chosenDevice;

  public CreateDeviceBuilder(ManageDevicesController controller, String backText) {
    super(controller, backText);
  }

  @Override
  public ManageDevicesController getController() {
    return (ManageDevicesController) super.getController();
  }

  @Override
  protected Node buildSpecifics() {
    VBox vbox = new VBox(10);

    HBox topHalf = new HBox(10);
    topHalf.getChildren().add(new Label("Device Type"));

    ScrollPane layout = new ScrollPane();
    layout.setFitToHeight(true);
    layout.setFitToWidth(true);
    layout.setHbarPolicy(ScrollBarPolicy.NEVER);
    layout.setVbarPolicy(ScrollBarPolicy.ALWAYS);

    // TODO: review import DeviceType from controller
    VBox typesForScroll = new VBox();
    final ToggleGroup upperGroup = new ToggleGroup();

    RadioButton button;
    for (DeviceType type : DeviceType.values()) {
      String typeName = type.toString();
      button = new RadioButton(typeName);
      button.setId(typeName.toLowerCase());
      button.setToggleGroup(upperGroup);
      button.setUserData(type);
      typesForScroll.getChildren().add(button);
    }
    upperGroup.getToggles().get(0).setSelected(true);
    topHalf.getChildren().add(typesForScroll);
    vbox.getChildren().add(topHalf);

    vbox.getChildren().add(new Separator());

    HBox lowerHalf = new HBox(10);

    VBox lowerLeft = new VBox(10);
    lowerLeft.getChildren().add(new Label("Device Status"));
    TextField customLabel = new TextField();
    customLabel.setId("label");
    customLabel.setPromptText("Label");
    lowerLeft.getChildren().add(customLabel);
    lowerHalf.getChildren().add(lowerLeft);

    VBox lowerMiddle = new VBox(10);
    final ToggleGroup lowerGroup = new ToggleGroup();

    RadioButton statusButton = new RadioButton("ON");
    statusButton.setId("on");
    statusButton.setToggleGroup(lowerGroup);
    statusButton.setUserData(true);
    statusButton.setSelected(true);
    lowerMiddle.getChildren().add(statusButton);

    statusButton = new RadioButton("OFF");
    statusButton.setId("off");
    statusButton.setToggleGroup(lowerGroup);
    statusButton.setUserData(false);
    lowerMiddle.getChildren().add(statusButton);

    Button lowerRight = new Button("Create Device");
    lowerRight.setId("create");
    lowerRight.setOnAction(
        event ->
            getController()
                .handleNewDeviceClick(
                    (DeviceType) upperGroup.getSelectedToggle().getUserData(),
                    (boolean) lowerGroup.getSelectedToggle().getUserData(),
                    customLabel.getText()));

    lowerHalf.getChildren().add(lowerMiddle);
    lowerHalf.getChildren().add(lowerRight);

    vbox.getChildren().add(lowerHalf);
    return vbox;
  }
}
