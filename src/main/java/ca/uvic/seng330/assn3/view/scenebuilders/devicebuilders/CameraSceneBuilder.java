package ca.uvic.seng330.assn3.view.scenebuilders.devicebuilders;

import ca.uvic.seng330.assn3.controller.CameraController;
import java.util.UUID;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;

public class CameraSceneBuilder extends DeviceSceneBuilder {

  String VideoURL = "https://youtu.be/1tUeqG0hsDQ";

  public CameraSceneBuilder(CameraController controller, String backText, UUID id) {
    super(controller, backText, id);
  }

  @Override
  public CameraController getController() {
    return (CameraController) super.getController();
  }

  @Override
  protected Node buildSpecifics() {
    VBox specifics = new VBox(10);
    HBox hbox = new HBox(10);

    VBox labels = new VBox(10);
    labels.setPrefWidth(700);
    Label isRecording = new Label("Recording");
    labels.getChildren().add(isRecording);
    Label diskSize = new Label("Disk Size\nClick to Reset");
    labels.getChildren().add(diskSize);

    VBox actions = new VBox(10);
    actions.setPrefWidth(700);
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

    //    MediaPlayer player = new MediaPlayer(new Media(VideoURL));
    //    player.setAutoPlay(true);
    //    MediaView mediaView = new MediaView(player);
    WebView mediaView = new WebView();
    mediaView.getEngine().load(VideoURL);

    hbox.getChildren().add(new Separator(Orientation.VERTICAL));
    hbox.getChildren().add(labels);
    hbox.getChildren().add(actions);
    hbox.getChildren().add(mediaView);

    specifics.getChildren().add(hbox);
    return specifics;
  }
}
