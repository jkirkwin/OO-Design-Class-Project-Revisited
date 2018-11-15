package ca.uvic.seng330.assn3.view.scenebuilders.devicebuilders;

import ca.uvic.seng330.assn3.controller.Controller;
import java.util.UUID;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class CameraSceneBuilder extends DeviceSceneBuilder {

  public CameraSceneBuilder(Controller controller, String backText, UUID id) {
    super(controller, backText, id);
  }

  @Override
  protected Node buildSpecifics() {
    VBox specifics = new VBox(10);
    HBox hbox = new HBox(10);

    VBox labels = new VBox(10);
    Label isRecording = new Label("Recording");
    labels.getChildren().add(isRecording);
    Label diskSize = new Label("Disk Size\nClick to Reset");
    labels.getChildren().add(diskSize);

    VBox actions = new VBox(10);
    Button toggleRecording =
        new Button(String.valueOf(getController().getCameraRecording(deviceID)));
    toggleRecording.setOnAction(event -> getController().setCameraRecording(deviceID));
    actions.getChildren().add(toggleRecording);
    Button emptyDisk =
        new Button(
            String.format(
                "%d/%d",
                getController().getCurrCameraDiskSize(deviceID),
                getController().getMaxCameraDiskSize(deviceID)));
    emptyDisk.setOnAction(event -> getController().emptyCameraDiskSize(deviceID));
    actions.getChildren().add(emptyDisk);

    hbox.getChildren().add(labels);
    hbox.getChildren().add(actions);
    specifics.getChildren().add(hbox);
    return specifics;
  }
}
