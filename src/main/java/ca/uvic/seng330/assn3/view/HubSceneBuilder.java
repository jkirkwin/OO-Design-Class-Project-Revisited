package ca.uvic.seng330.assn3.view;

import ca.uvic.seng330.assn3.controller.Controller;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.VBox;

public class HubSceneBuilder extends SceneBuilder {

  public HubSceneBuilder(Controller controller, String backText) {
    super(controller, backText);
  }

  @Override
  protected Node buildSpecifics() {
    // TODO: change stagename to Hubview
    ScrollPane layout = new ScrollPane();
    layout.setFitToHeight(true);
    layout.setFitToWidth(true);
    layout.setHbarPolicy(ScrollBarPolicy.NEVER);
    layout.setVbarPolicy(ScrollBarPolicy.ALWAYS);
    VBox buttons = new VBox(10);

    String[] deviceNames = new String[16]; // change to get device list
    for (int i = 0; i < deviceNames.length; i++) {
      deviceNames[i] = "I am " + i; // iterate through device list
    }

    vList(deviceNames, buttons, true);
    layout.setContent(buttons);
    //    for (Node n : buttons.getChildren()) {
    //      ((Labeled) n).setText("cheese");
    //    }

    return layout;
  }
}
