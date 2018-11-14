package ca.uvic.seng330.assn3.controller;

import ca.uvic.seng330.assn3.view.SceneBuilder;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.VBox;

public class SelectDevicesBuilder extends SceneBuilder {

  public SelectDevicesBuilder(Controller controller, String backText) {
    super(controller, backText);
    // TODO Auto-generated constructor stub
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
