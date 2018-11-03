package ca.uvic.seng330.assn3.view;

import javafx.scene.Scene;

public abstract class DeviceSceneBuilder extends SceneBuilder {

  @Override
  public Scene build() {
    // TODO Auto-generated method stub
    return null;
  }

  protected Scene buildTemplate() {
    // TODO
    return null;
  }
  
  public abstract void buildSpecifics(Scene s);  
}
