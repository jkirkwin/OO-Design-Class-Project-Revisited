package ca.uvic.seng330.assn3.view.scenebuilders.devicebuilders;

import ca.uvic.seng330.assn3.controller.CameraController;
import java.util.UUID;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;

public class CameraSceneBuilder extends DeviceSceneBuilder {

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
    TextField videoSource = new TextField();
    videoSource.setPromptText("New Video Source");
    labels.getChildren().add(videoSource);

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
    Button videoSet = new Button("Set Video Source");
    videoSet.setOnAction(event -> getController().setVideoSource(deviceID, videoSource.getText()));
    actions.getChildren().add(videoSet);

    String VideoURL = getController().getVideoSource(deviceID);
    if (VideoURL == null) VideoURL = "https://youtu.be/1tUeqG0hsDQ";
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
