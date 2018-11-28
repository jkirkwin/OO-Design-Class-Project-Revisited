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

  //  String VideoURL = "http://download.oracle.com/otndocs/products/javafx/oow2010-2.flv";
  String VideoURL = "https://youtu.be/bWr2rZtV0Kk";

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
    labels.setPrefWidth(200);
    Label isRecording = new Label("Recording");
    labels.getChildren().add(isRecording);
    Label diskSize = new Label("Disk Size\nClick to Reset");
    labels.getChildren().add(diskSize);

    VBox actions = new VBox(10);
    actions.setPrefWidth(200);
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

    //        MediaPlayer player = new MediaPlayer(new Media(VideoURL));
    //        player.setAutoPlay(true);
    //        MediaView mediaView = new MediaView(player);
    WebView mediaView = new WebView();
    mediaView.getEngine().load(VideoURL);

    hbox.getChildren().add(new Separator(Orientation.VERTICAL));
    hbox.getChildren().add(labels);
    hbox.getChildren().add(actions);

    specifics.getChildren().add(hbox);
    specifics.getChildren().add(mediaView);
    return specifics;
  }
}
