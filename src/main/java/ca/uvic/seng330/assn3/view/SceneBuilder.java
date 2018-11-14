package ca.uvic.seng330.assn3.view;

import ca.uvic.seng330.assn3.controller.Controller;
import java.util.ArrayList;
import java.util.UUID;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public abstract class SceneBuilder {

  private String backText;
  private Controller controller;

  /*
   * @pre controller != null
   * @pre backText != null
   */
  public SceneBuilder(Controller controller, String backText) {
    assert controller != null;
    assert backText != null;
    this.backText = backText;
    this.controller = controller;
  }

  public Scene build() {
    GridPane grid = new GridPane();
    grid.add(this.buildCommon(), 0, 0);

    // this is the template part: buildSpecifics is the 'hook' method implemented by the concrete
    // subclasses of this one
    grid.add(this.buildSpecifics(), 1, 1);
    Scene s = new Scene(grid);
    return s;
  }

  // The only thing common to all of our scenes is a back button
  protected Node buildCommon() {
    Button backButton = new Button();
    backButton.setText(this.backText);
    backButton.setOnAction(
        new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent event) {
            getController().handleBackClick();
          }
        });
    return backButton;
  }

  protected abstract Node buildSpecifics();

  /*
   * @pre content != null
   */
  protected static ScrollPane createScrollPane(Node content) {
    assert content != null;
    ScrollPane s = new ScrollPane();
    s.setContent(content);
    return s;
  }

  /* Gets a List of UUIDs from controller.
   *  builds
   * @pre col != null
   */
  protected VBox hubDeviceList(VBox col) {
    assert col != null;
    ArrayList<UUID> deviceList = getController().getDeviceIDList();
    for (int i = 0; i < deviceList.size(); i++) {
      Button button = new Button(getController().getLabel(deviceList.get(i)));
      button.setUserData(deviceList.get(i));
      button.setOnAction(
          event -> getController().handleDeviceViewClick((UUID) button.getUserData()));
      col.getChildren().add(button);
    }
    return col;
  }

  protected VBox hubUsersList(VBox col) {
    assert col != null;
    ArrayList<UUID> userList = getController().getAccountIDList();
    for (int i = 0; i < userList.size(); i++) {
      Button button = new Button(getController().getLabel(userList.get(i)));
      button.setUserData(userList.get(i));
      button.setOnAction(event -> getController().handleUserViewClick((UUID) button.getUserData()));
      col.getChildren().add(button);
    }
    return col;
  }

  /*
   * @pre col != null
   * @pre deleteList != null
   */
  protected VBox hubDeleteList(VBox col, ArrayList<UUID> deleteList) {
    assert col != null;
    assert deleteList != null;
    for (int i = 0; i < deleteList.size(); i++) {
      Button button = new Button(getController().getLabel(deleteList.get(i)));
      button.setUserData(deleteList.get(i));
      button.setOnAction(event -> getController().handleDeleteClick((UUID) button.getUserData()));
      col.getChildren().add(button);
    }
    return col;
  }

  protected Controller getController() {
    return controller;
  }
}
