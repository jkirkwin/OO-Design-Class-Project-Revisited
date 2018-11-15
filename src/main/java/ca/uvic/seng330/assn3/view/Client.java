package ca.uvic.seng330.assn3.view;

import ca.uvic.seng330.assn3.controller.Controller;
import ca.uvic.seng330.assn3.view.scenebuilders.SceneBuilder;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class Client {

  private final Stage window;
  private Scene view;
  private Controller controller;

  /*
   * @pre controller != null
   * @pre window != null
   */
  public Client(Stage window) {
    assert window != null;

    this.window = window;
    this.view = null;
  }

  public void setController(Controller controller) {
    this.controller = controller;
  }

  public void setTitle(String title) {
    this.window.setTitle(title);
  }

  /*
   * @pre builder != null
   */
  public void setView(SceneBuilder builder) {
    assert builder != null;
    this.window.setScene(builder.build());

    // TODO unsure if this is necessary to call every time, but it definitely needs to be called at
    // least once,
    //      on the first time we load the login view
    window.show();
  }

  public Controller getController() {
    return this.controller;
  }

  public Scene getView() {
    return this.getView();
  }

  public void close() {
    window.close();
  }

  public void alertUser(AlertType alertType, String headerText, String title, String text) {
    Alert alert = new Alert(alertType);
    alert.setTitle(title);
    alert.setHeaderText(headerText);
    alert.setContentText(text);
    alert.showAndWait();
  }

  public Stage getWindow() {
    return this.window;
  }
}
