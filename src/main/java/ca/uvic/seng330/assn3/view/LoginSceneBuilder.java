package ca.uvic.seng330.assn3.view;

import ca.uvic.seng330.assn3.controller.Controller;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class LoginSceneBuilder extends SceneBuilder {
  
  public LoginSceneBuilder(Controller controller, String backText) {
    super(controller, backText);
  }

  @Override
  protected Node buildSpecifics() {
    HBox hbox = new HBox(5);
    
    VBox textFields = new VBox(5);
    TextField username = new TextField("username");
    TextField password = new TextField("password");
    textFields.getChildren().add(username);
    textFields.getChildren().add(password);
    
    VBox buttons = new VBox(5);
    Button login = new Button("Login");
    login.setOnAction( event -> getController().handleLoginClick(username.getText(), password.getText()));
    Button user = new Button("New User");
    user.setOnAction( event -> getController().handleNewUser(username.getText(), password.getText()));    
    Button admin = new Button("New Admin");
    admin.setOnAction( event -> getController().handleNewAdmin(username.getText(), password.getText()));
    
    buttons.getChildren().add(login);
    buttons.getChildren().add(user);
    buttons.getChildren().add(admin);
    
    hbox.getChildren().add(textFields);
    hbox.getChildren().add(buttons);
    
    return hbox;
  }
  

  /*
  @Override
  public void handle(ActionEvent event) {
    // call getClient().getController().theAppropriateHandlerFunction()
    // this handler function will depend on the 
    
  }
  */
}

// Idea: create wrappers for buttons to have DeviceButtons, LoginButtons, ManagementButtons etc 
// that contain within them the information needed by the handlers in the controller package
// in the case of LoginButtons, these would extend button but would contain a LoginButtonType
// enum that has values NEW_USER, NEW_ADMIN, and LOGIN to be used by the handler
