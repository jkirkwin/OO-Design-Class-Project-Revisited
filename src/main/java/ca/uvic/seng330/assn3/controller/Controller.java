package ca.uvic.seng330.assn3.controller;

import java.util.Stack;

import ca.uvic.seng330.assn3.model.Hub;
import ca.uvic.seng330.assn3.model.UserAccount;
import ca.uvic.seng330.assn3.view.Client;
import ca.uvic.seng330.assn3.view.LoginSceneBuilder;

public class Controller {

  private final Hub hub;
  private final Client client;
  private UserAccount activeUser;
  private final Stack<ViewType> views;
  
  /*
   * @pre hub != null
   * @pre client != null
   */
  public Controller(Hub hub, Client client) {
    assert hub != null;
    assert client != null;

    this.activeUser = null;
    this.client = client;
    this.client.setController(this);
    this.hub = hub;
    this.views = new Stack<ViewType>();
    
    // Load and display login screen
    views.push(ViewType.LOGIN);
    client.setTitle("Login");
    client.setView(new LoginSceneBuilder(this, "Close"));
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
      // TODO close window
    } else if (views.peek() == ViewType.HUB_BASIC || views.peek() == ViewType.HUB_ADMIN) {
      // TODO log out
    } else {
      // TODO go back
    }    
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
