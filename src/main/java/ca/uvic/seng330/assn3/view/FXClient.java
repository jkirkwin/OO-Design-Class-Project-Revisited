package ca.uvic.seng330.assn3.view;

import ca.uvic.seng330.assn3.controller.Controller;

public class FXClient extends Client {

  // instance variables should include common scenes -> login, User Hub View, Device view for each
  // device
  // could use an Enum to keep track of state - that is, which scene is currently displayed
  
  /*
   * @pre c != null
   * @pre v != null
   */
  public FXClient(Controller controller, ViewType viewType) {
    super(controller, viewType);
  }
  
  /* This method allows a controller to tell the model which view it should switch to.
   * It should call the appropriate view-generation code (HubBuilder, for example) to
   * switch the user's view to match the ViewType passed in.
   * @pre viewType != null
   */
  
  @Override
  public void setView(ViewType view) {
    super.setView(view);
    // TODO may need to add a param that lets the controller pass in information related
    // to the specified view. Eg: for creating the hub view, we need a collection of devices
    // and for the admin manage users view we need a collection of users
    
    // This might preclude us from sticking with this client interface if some scenes require 
    // multiple params to build.
    
    // If they only ever require one, we can just add 'Object arg' to the signature
    
    // An alternate way to do this would be to have the client query the controller for information
    // about the model, but this seems like it might be just on the line of violating the MVC pattern
    
    // Another alternative: add 'Object arg' in client interface, and for the FXController we can either 
    // pass in a scene, or a sceneBuilder which has been set up so that a call to build() generates the 
    // correct scene
    
  }
}
