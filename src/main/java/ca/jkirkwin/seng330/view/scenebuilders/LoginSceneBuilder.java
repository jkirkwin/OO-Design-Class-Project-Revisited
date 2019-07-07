package ca.jkirkwin.seng330.view.scenebuilders;

import ca.jkirkwin.seng330.controller.LoginController;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class LoginSceneBuilder extends SceneBuilder {

  public LoginSceneBuilder(LoginController controller, String backText) {
    super(controller, backText);
  }

  @Override
  public LoginController getController() {
    return (LoginController) super.getController();
  }

  @Override
  protected Node buildSpecifics() {
    HBox hbox = new HBox(5);

    VBox textFields = new VBox(5);
    TextField username = new TextField();
    username.setId("username");
    username.setPromptText("username");
    TextField password = new TextField();
    password.setId("password");
    password.setPromptText("password");
    textFields.getChildren().add(username);
    textFields.getChildren().add(password);

    VBox buttons = new VBox(5);
    Button login = new Button("Login");
    login.setId("login");
    login.setOnAction(
        event -> getController().handleLoginClick(username.getText(), password.getText()));
    Button user = new Button("New User");
    user.setId("new_user");
    user.setOnAction(
        event -> getController().handleNewUser(username.getText(), password.getText()));
    Button admin = new Button("New Admin");
    admin.setId("new_admin");
    admin.setOnAction(
        event -> getController().handleNewAdmin(username.getText(), password.getText()));

    buttons.getChildren().add(login);
    buttons.getChildren().add(user);
    buttons.getChildren().add(admin);

    hbox.getChildren().add(textFields);
    hbox.getChildren().add(buttons);

    return hbox;
  }
}
