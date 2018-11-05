package ca.uvic.seng330.assn3.view;

import java.util.ArrayList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public abstract class SceneBuilder {

  public Scene build() {
    Scene s = buildTemplate();
    buildSpecifics(s);
    return s;
  }

  protected ScrollPane createScrollPane(Node content) {
    ScrollPane s = new ScrollPane();
    s.setContent(content);
    return s;
  }

  protected abstract Scene buildTemplate();

  protected abstract void buildSpecifics(Scene s);

  /*
   * Makes the buttons inside a context.
   * ie. inside a ScrollPane
   * TODO: only creates buttons, does not assign Events.
   */
  protected VBox vList(int numButtons) {
    ArrayList<Button> hubDevices = new ArrayList<Button>();
    VBox vbox = new VBox(5);
    for (int i = 0; i < numButtons; i++) {
      hubDevices.add(i, new Button("I am " + i));
      // hubDevices.get(i).setOnAction(this);
      // TODO: Place call to handler here.
      vbox.getChildren().add(hubDevices.get(i));
    }
    return vbox;
  }
}
