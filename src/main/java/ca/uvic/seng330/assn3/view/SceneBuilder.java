package ca.uvic.seng330.assn3.view;

import javafx.scene.Scene;

public abstract class SceneBuilder {

  public Scene build() {
    Scene s = buildTemplate();
    buildSpecifics(s);
    return s;
  }

  protected abstract Scene buildTemplate();

  protected abstract void buildSpecifics(Scene s);
}
