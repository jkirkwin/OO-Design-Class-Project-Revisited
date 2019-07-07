package ca.jkirkwin.seng330.view.scenebuilders;

import ca.jkirkwin.seng330.controller.Controller;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.VBox;

public class SelectDevicesBuilder extends SceneBuilder {

  public SelectDevicesBuilder(Controller controller, String backText) {
    super(controller, backText);
  }

  @Override
  protected Node buildSpecifics() {

    ScrollPane layout = new ScrollPane();
    layout.setFitToHeight(true);
    layout.setFitToWidth(true);
    layout.setHbarPolicy(ScrollBarPolicy.NEVER);
    layout.setVbarPolicy(ScrollBarPolicy.ALWAYS);

    VBox buttons = new VBox(10);
    layout.setContent(hubDeviceList(buttons));

    return layout;
  }
}
