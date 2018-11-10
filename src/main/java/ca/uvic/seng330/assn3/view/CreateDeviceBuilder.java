package ca.uvic.seng330.assn3.view;

import ca.uvic.seng330.assn3.controller.Controller;
import ca.uvic.seng330.assn3.controller.DeviceType;
import java.util.ArrayList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class CreateDeviceBuilder extends SceneBuilder {

  public CreateDeviceBuilder(Controller controller, String backText) {
    super(controller, backText);
  }

  @Override
  protected Node buildSpecifics() {
    HBox hbox = new HBox(10);

    hbox.getChildren().add(new Label("Device Type"));

    ScrollPane layout = new ScrollPane();
    layout.setFitToHeight(true);
    layout.setFitToWidth(true);
    layout.setHbarPolicy(ScrollBarPolicy.NEVER);
    layout.setVbarPolicy(ScrollBarPolicy.ALWAYS);

    // TODO: review import DeviceType from controller
    ArrayList<DeviceType> deviceTypes = getController().getDeviceTypes();
    VBox vbox = new VBox();
    RadioButton button;
    for (int i = 0; i < deviceTypes.size(); i++) {
      button = new RadioButton(deviceTypes.get(i).toString());
      button.setUserData(deviceTypes.get(i));
      vbox.getChildren().add(button);
    }
    hbox.getChildren().add(vbox);

    return hbox;
  }
}
