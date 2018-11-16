package ca.uvic.seng330.assn3.view.scenebuilders;

import ca.uvic.seng330.assn3.controller.Controller;
import ca.uvic.seng330.assn3.controller.DeviceType;
import java.util.ArrayList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class CreateDeviceBuilder extends SceneBuilder {

  DeviceType chosenDevice;

  public CreateDeviceBuilder(Controller controller, String backText) {
    super(controller, backText);
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
    // TODO remove the arraylist and instead use the builtin DeviceType.values() array
    ArrayList<DeviceType> deviceTypes = getController().getDeviceTypes();
    VBox typesForScroll = new VBox();
    final ToggleGroup upperGroup = new ToggleGroup();

    RadioButton button;
    for (int i = 0; i < deviceTypes.size(); i++) {
      String typeName = deviceTypes.get(i).toString();
      button = new RadioButton(typeName);
      button.setId(typeName.toLowerCase());
      System.out.println(typeName.toLowerCase()); 
      button.setToggleGroup(upperGroup);
      button.setUserData(deviceTypes.get(i));
      typesForScroll.getChildren().add(button);
    }
    upperGroup.getToggles().get(0).setSelected(true);
    topHalf.getChildren().add(typesForScroll);
    vbox.getChildren().add(topHalf);

    vbox.getChildren().add(new Label("=============================="));

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
