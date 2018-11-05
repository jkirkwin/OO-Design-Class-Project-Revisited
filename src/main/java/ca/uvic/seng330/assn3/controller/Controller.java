package ca.uvic.seng330.assn3.controller;

import ca.uvic.seng330.assn3.model.Hub;
import ca.uvic.seng330.assn3.model.UserAccount;
import ca.uvic.seng330.assn3.view.Client;

public class Controller {

  private final Hub hub;
  private final Client client;
  private UserAccount activeUser;
  
  /*
   * @pre hub != null
   * @pre client != null
   */
  public Controller(Hub hub, Client client) {
    assert hub != null;
    assert client != null;

    this.activeUser = null;
    this.client = client;
    this.hub = hub;
  }
  
  public void update() {
    // TODO
    // this is the mega-handler to be used to delegate action to 
    // the appropriate function to update the view and/or model
    // once we've got this thing functional we can see if there is 
    // an easy way to re-factor it into something less god-function-esque.
    
    // Will need to add some argument that tells us about the button that was pressed/
    // the radio item selected/the text entered in a field.
    
    // Set active user on whenever an account successfully logs in, and remove it 
    // whenever they log out
    switch(client.getView()) {
    case LOGIN:
      break;
      
    case CREATE_DEVICE:
    
      break;
    case CREATE_USER:
    
      break;
    case DEVICE_VIEW:
    
      break;
    case HUB_ADMIN:
    
      break;
    case HUB_BASIC:
    
      break;
    
    case MANAGE_DEVICES:
    
      break;
    case MANAGE_NOTIFICATIONS:
    
      break;
    case MANAGE_USERS:
    
      break;
    case SELECT_NOTIFICATIONS:
    
      break;
    default:
    
      break;
    }
  }
}
