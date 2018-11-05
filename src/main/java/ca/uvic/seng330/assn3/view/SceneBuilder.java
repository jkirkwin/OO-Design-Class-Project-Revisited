package ca.uvic.seng330.assn3.view;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;

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
}
