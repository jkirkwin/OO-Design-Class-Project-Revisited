package ca.uvic.seng330.assn3.view;

import javafx.scene.Scene;

public abstract class HubSceneBuilder extends SceneBuilder {

  protected Scene buildTemplate() {
    // TODO generate a scene with things common to all hub-views
    // such things include: an exit button
    //                      a back button
    // the scene its self (obviously)
    // possibly a title?

    //    GridPane root = new GridPane();
    //
    //    Scene s = new Scene(root);

    return null;
  }

  /*
   *  includes the functions to be used to handle button presses?
   *  alternatively, we could have one thick function in controller that delegates
   *  to a variety of smaller handler functions based on where the user is in the app
   *
   *  for example, in the initial view the controller will change view to display the device
   *  view for the device corresponding to the button pressed. In the admin editing hub view,
   *  a different handler function will be called from this thick mega-handler that displays
   *  the admin console's device editing view for the device corresponding to the button pressed.
   */
  @Override
  public abstract void buildSpecifics(Scene s);
}
