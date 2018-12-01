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
  public final String videoURL = "https://studentweb.uvic.ca/~jkirkwin/SENG330/video.html";
  public final String blankURL = "https://studentweb.uvic.ca/~jkirkwin/SENG330/blank.html";

  private WebView webView = new WebView();

  public CameraSceneBuilder(CameraController controller, String backText, UUID id) {
    super(controller, backText, id);
  }

  @Override
  public CameraController getController() {
    return (CameraController) super.getController();
  }

  @Override
  protected Button makeBackButton() {
    Button backButton = super.makeBackButton();
    backButton.setOnAction(
        event -> {
          setVideoURL(blankURL);
          getController().handleBackClick();
        });
    return backButton;
  }

  @Override
  protected Button makeStatusToggle() {
    Button toggle = super.makeStatusToggle();
    toggle.setOnAction(
        event -> {
          getController().toggleDevice();
          String url = getController().getCameraRecording() ? videoURL : blankURL;
          setVideoURL(url);
        });
    return toggle;
  }

  @Override
  protected Node buildSpecifics() {
    VBox specifics = new VBox(10);
    HBox hbox = new HBox(10);
    hbox.setMaxWidth(250);

    VBox labels = new VBox(10);
    labels.setPrefWidth(200);
    Label isRecording = new Label("Recording");
    labels.getChildren().add(isRecording);
    Label diskSize = new Label("Disk Size\nClick to Reset");
    labels.getChildren().add(diskSize);

    VBox actions = new VBox(10);
    actions.setPrefWidth(200);
    Button toggleRecording = new Button(String.valueOf(getController().getCameraRecording()));
    toggleRecording.setOnAction(
        event -> {
          getController().setCameraRecording();
          if (getController().getCameraRecording()) {
            setVideoURL(videoURL);
          }
        });
    toggleRecording.setId("record");
    actions.getChildren().add(toggleRecording);
    Button emptyDisk =
        new Button(
            String.format(
                "%d/%d",
                getController().getCurrCameraDiskSize(), getController().getMaxCameraDiskSize()));
    emptyDisk.setId("empty_disk");
    emptyDisk.setOnAction(
        event -> {
          setVideoURL(blankURL);
          getController().emptyCameraDiskSize();
        });
    actions.getChildren().add(emptyDisk);

    String initialURL = getController().getCameraRecording() ? videoURL : blankURL;
    webView.getEngine().load(initialURL);
    webView.setPrefSize(500, 300);

    hbox.getChildren().add(new Separator(Orientation.VERTICAL));
    hbox.getChildren().add(labels);
    hbox.getChildren().add(actions);

    specifics.getChildren().add(hbox);
    specifics.getChildren().add(webView);
    return specifics;
  }

  private void toggleVideo() {
    String url = isVideoSelected() ? blankURL : videoURL;
    setVideoURL(url);
  }

  private void setVideoURL(String url) {
    this.webView.getEngine().load(url);
  }

  private boolean isVideoSelected() {
    return this.webView.getEngine().getLocation().equals(videoURL);
  }
}
