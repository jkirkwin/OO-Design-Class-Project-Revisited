package ca.uvic.seng330.assn3.view;

import ca.uvic.seng330.assn3.controller.Controller;
import ca.uvic.seng330.assn3.controller.DeviceType;
import java.util.ArrayList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class CreateDeviceBuilder extends SceneBuilder {

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
    ArrayList<DeviceType> deviceTypes = getController().getDeviceTypes();
    VBox typesForScroll = new VBox();
    RadioButton button;
    for (int i = 0; i < deviceTypes.size(); i++) {
      button = new RadioButton(deviceTypes.get(i).toString());
      button.setUserData(deviceTypes.get(i));
      typesForScroll.getChildren().add(button);
    }
    topHalf.getChildren().add(typesForScroll);
    vbox.getChildren().add(topHalf);

    vbox.getChildren().add(new Label("=============================="));

    HBox lowerHalf = new HBox(10);

    VBox lowerLeft = new VBox(10);
    lowerLeft.getChildren().add(new Label("Device Status"));
    TextField customLabel = new TextField();
    customLabel.setPromptText("Label");
    lowerLeft.getChildren().add(customLabel);
    lowerHalf.getChildren().add(lowerLeft);

    HBox lowerMiddle = new HBox(10);
    // TODO: add radio buttons here
    HBox lowerRight = new HBox(10);
    // TODO: add create device button here

    lowerHalf.getChildren().add(lowerMiddle);
    lowerHalf.getChildren().add(lowerRight);

    vbox.getChildren().add(lowerHalf);

    return vbox;
  }
}
