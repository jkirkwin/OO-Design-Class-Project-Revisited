package ca.uvic.seng330.assn3.controller;

import ca.uvic.seng330.assn3.model.Hub;
import ca.uvic.seng330.assn3.view.Client;

public class Controller {

  private final Hub hub;
  private final Client client;
  
  /*
   * @pre hub != null
   * @pre client != null
   */
  public Controller(Hub hub, Client client) {
    assert hub != null;
    assert client != null;

    this.client = client;
    this.hub = hub;
  }
  
  public void update() {
    // TODO
    // this is the mega-handler to be used to delegate action to 
    // the appropriate function to update the view and/or model
  
    switch(client.getViewType()) {
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
    case LOGIN:
    
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
