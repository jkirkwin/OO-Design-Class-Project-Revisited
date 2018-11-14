package ca.uvic.seng330.assn3.view;

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

  UUID deviceID;
  double magnitude = 0;

  public ThermostatSceneBuilder(Controller controller, String backText, UUID id) {
    super(controller, backText);
    this.deviceID = id;
  }

  @Override
  protected Node buildSpecifics() {
    HBox basics = basicBuild(deviceID);

    VBox specifics = new VBox(10);
    HBox hbox = new HBox(10);

    VBox labels = new VBox(20);
    Label currTemp = new Label("Current Temp");
    labels.getChildren().add(currTemp);
    Label newTemp = new Label("Create new Temp");
    labels.getChildren().add(newTemp);

    VBox actions = new VBox(10);
    Button displayTemp =
        new Button(
            String.valueOf(getController().getThermostatTempMag(deviceID))
                + " degrees "
                + getController().getThermostatTempType(deviceID));
    actions.getChildren().add(displayTemp);

    HBox tempMaker = new HBox(10);
    TextField tempNum = new TextField();
    tempNum.setPromptText("New Temperature");

    //    TODO: tempNum should only take numbers!!!
    //
    //    Pattern pattern = Pattern.compile("\\d*|\\d+\\,\\d*");
    //    TextFormatter formatter = new TextFormatter((UnaryOperator<TextFormatter.Change>) change
    // -> {
    //        return pattern.matcher(change.getControlNewText()).matches() ? change : null;
    //    });
    //
    //    tempNum.setTextFormatter(formatter);

    // TODO: review importing unit enum
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

    tempMaker.getChildren().add(tempNum);
    tempMaker.getChildren().add(degreeChoice);
    actions.getChildren().add(tempMaker);

    Button createNewTemp = new Button("Set New Temperature");
    // TODO: new temp isnt being set right
    try {
      this.magnitude = Double.parseDouble(tempNum.getText());
    } catch (NumberFormatException err) {
      // TODO: error and alert of invalid input
    }
    createNewTemp.setOnAction(
        event ->
            getController()
                .setThermostatTemp(deviceID, magnitude, degrees.getSelectedToggle().getUserData()));

    actions.getChildren().add(createNewTemp);

    hbox.getChildren().add(labels);
    hbox.getChildren().add(actions);
    specifics.getChildren().add(hbox);
    basics.getChildren().add(specifics);

    return basics;
  }
}
