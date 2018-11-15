package ca.uvic.seng330.assn3.view.scenebuilders.devicebuilders;

import ca.uvic.seng330.assn3.controller.Controller;
import ca.uvic.seng330.assn3.model.devices.Temperature.Unit;
import java.util.ArrayList;
import java.util.UUID;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ThermostatSceneBuilder extends DeviceSceneBuilder {

  double magnitude = 0;

  public ThermostatSceneBuilder(Controller controller, String backText, UUID id) {
    super(controller, backText, id);
  }

  @Override
  protected Node buildSpecifics() {
    VBox specifics = new VBox(10);

    HBox currTempActions = new HBox(10);
    Label currTemp = new Label("Current Temp -->");
    String currentTemp =
        String.valueOf(getController().getThermostatTempMag(deviceID))
            + " degrees "
            + getController().getThermostatTempType(deviceID);
    if (getController().getThermostatTempType(deviceID) == "CELSIUS") {
      currentTemp += "        ";
    }
    Label displayTemp = new Label(currentTemp);
    Button switchTempType = new Button("Change Degrees");
    switchTempType.setOnAction(event -> getController().changeThermostatDegreeType(deviceID));
    currTempActions.getChildren().add(currTemp);
    currTempActions.getChildren().add(displayTemp);
    currTempActions.getChildren().add(switchTempType);

    HBox newTempActions = new HBox(10);
    Label newTemp = new Label("Create new Temp");
    TextField tempNum = new TextField();
    tempNum.setPromptText("New Temperature");

    ArrayList<Unit> degreeTypes = getController().getThermostatDegreeTypes();
    VBox degreeChoice = new VBox(10);
    final ToggleGroup degrees = new ToggleGroup();
    RadioButton button;
    for (int i = 0; i < degreeTypes.size(); i++) {
      button = new RadioButton(degreeTypes.get(i).toString());
      button.setToggleGroup(degrees);
      button.setUserData(degreeTypes.get(i));
      degreeChoice.getChildren().add(button);
    }
    degrees.getToggles().get(0).setSelected(true);
    newTempActions.getChildren().add(newTemp);
    newTempActions.getChildren().add(tempNum);
    newTempActions.getChildren().add(degreeChoice);

    Button createNewTemp = new Button("Set New Temperature");
    createNewTemp.setOnAction(
        event ->
            getController()
                .constructTemp(
                    deviceID, tempNum.getText(), (Unit) degrees.getSelectedToggle().getUserData()));

    specifics.getChildren().add(currTempActions);
    specifics.getChildren().add(newTempActions);
    specifics.getChildren().add(createNewTemp);
    return specifics;
  }
}
