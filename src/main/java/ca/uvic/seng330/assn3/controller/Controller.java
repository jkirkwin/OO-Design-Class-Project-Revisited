package ca.uvic.seng330.assn3.controller;

import java.util.Stack;

import ca.uvic.seng330.assn3.model.Hub;
import ca.uvic.seng330.assn3.model.UserAccount;
import ca.uvic.seng330.assn3.view.Client;
import ca.uvic.seng330.assn3.view.LoginSceneBuilder;
import ca.uvic.seng330.assn3.view.SceneBuilder;

public class Controller {

  private final Hub hub;
  private final Client client;
  private UserAccount activeUser;
  private final Stack<ViewType> views;
  private final SceneBuilder loginBuilder;
  
  /*
   * @pre hub != null
   * @pre client != null
   */
  public Controller(Hub hub, Client client) {
    assert hub != null;
    assert client != null;

    this.loginBuilder = new LoginSceneBuilder(this, "Close");
    this.activeUser = null;
    this.client = client;
    this.client.setController(this);
    this.hub = hub;
    this.views = new Stack<ViewType>();
    
    // Load and display login screen
    views.push(ViewType.LOGIN);
    client.setTitle("Login");
    client.setView(loginBuilder);
  }

  public void update(Object arg) {
    // TODO
    // this is the mega-handler to be used to delegate action to
    // the appropriate function to update the view and/or model
    // once we've got this thing functional we can see if there is
    // an easy way to re-factor it into something less god-function-esque.

    // Will need to add some argument that tells us about the button that was pressed/
    // the radio item selected/the text entered in a field.

    // Set active user on whenever an account successfully logs in, and remove it
    // whenever they log out
    
    // Way better idea: have a controller for each style of view we have and hold one 
    //of each in this main controller
    
    // For now we're just going to split up handlers and see how that works out. Later on
    // we can combine some, and/or package them into various classes
  }
  
  public void handleBackClick() {
    if(views.peek() == ViewType.LOGIN) {
      // close window
      client.close();
    } else if (views.peek() == ViewType.HUB_BASIC || views.peek() == ViewType.HUB_ADMIN) {
      // log out
      client.setView(loginBuilder);
      views.pop();
      this.activeUser = null;
    } else {
      views.pop();
      client.setTitle(views.peek().toString());

      // TODO generate appropriate builder based on the ViewType now on the top of the stack
      client.setView(findBuilder(views.peek()));
    }   
    
    System.out.println("Back"); // Test
  }
  
  private SceneBuilder findBuilder(ViewType view) {
    // TODO generate the appropriate SceneBuilder based on for the ViewType
    switch(view) {
      case LOGIN:
        return loginBuilder;
        
      case CREATE_DEVICE:
        break;
        
      case HUB_ADMIN:
        // TODO
        break;
        
      case HUB_BASIC:
        // TODO
        break;

      case MANAGE_DEVICES:
        // TODO
        break;
        
      case MANAGE_NOTIFICATIONS:
        // TODO
        break;
        
      case MANAGE_USERS:
        // TODO
        break;
        
      case SELECT_NOTIFICATIONS:
        // TODO
        break;
        
      default:
        System.out.println("No case in controller.findBuilder() for viewType " + view);
        break;
      
    }
    return null;
  }

  public void handleLoginClick(String username, String password) {
    /* TODO 
     * Validate input
     * Alert if invalid
     * Log in if valid
     */
    System.out.println("Logged in"); // Testing
  } 
  
  public void handleNewUser(String username, String password) {
    /* TODO 
     * Validate input
     * Alert if invalid
     * Create user if valid
     */
    System.out.println("New User"); // Testing
  }
  
  public void handleNewAdmin(String username, String password) {
    /* TODO 
     * Validate input
     * Alert if invalid
     * Create user if valid
     */
    System.out.println("New Admin"); // Testing    
    
  }
}
