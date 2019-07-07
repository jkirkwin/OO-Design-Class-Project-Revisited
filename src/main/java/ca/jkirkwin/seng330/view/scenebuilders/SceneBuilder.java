package ca.jkirkwin.seng330.view.scenebuilders;

import ca.jkirkwin.seng330.controller.Controller;
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
    Node common = this.buildCommon();
    grid.add(common, 0, 0);

    // this is the template part: buildSpecifics is the 'hook' method implemented by the concrete
    // subclasses of this one
    Node specifics = this.buildSpecifics();
    specifics.setId("specifics");
    grid.add(specifics, 1, 1);
    grid.setId("root");
    Scene s = new Scene(grid);
    return s;
  }

  // The only thing common to all of our scenes is a back button
  protected Node buildCommon() {
    return makeBackButton();
  }

  protected Button makeBackButton() {
    Button backButton = new Button();
    backButton.setText(this.backText);
    backButton.setId("back");
    backButton.setOnAction(
        new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent event) {
            controller.handleBackClick();
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
   *  builds a list of buttons, one for each device id
   * @pre col != null
   */
  protected VBox hubDeviceList(VBox col) {
    assert col != null;
    ArrayList<UUID> deviceList = controller.getDeviceIDList();
    for (int i = 0; i < deviceList.size(); i++) {

      // Threading for Z2
      //      Object[] statusWrapper = new Object[1];
      //      Object[] labelWrapper = new Object[1];
      //      Thread statusCheck = new Thread(new StatusCheck(getController(), deviceList.get(i),
      // statusWrapper));
      //      Thread labelCheck = new Thread(new LabelCheck(getController(), deviceList.get(i),
      // labelWrapper));
      //      statusCheck.start();
      //      labelCheck.start();
      //      try {
      //        labelCheck.join();
      //        labelCheck.join();
      //      } catch (InterruptedException e) {
      //        Logging.log("Thread interrupted.", Level.ERROR);
      //      }
      //      String label = labelWrapper[0].toString();
      //      String statusStr = statusWrapper[0].toString();

      String label = getController().getLabel(deviceList.get(i));
      String statusStr = getController().devStatus(deviceList.get(i));
      Button button = new Button();
      button.setUserData(deviceList.get(i));
      button.setOnAction(event -> controller.handleDeviceViewClick((UUID) button.getUserData()));
      button.setId(label);
      button.setText(label + " - " + statusStr);

      col.getChildren().add(button);
    }
    return col;
  }

  protected VBox hubUsersList(VBox col) {
    assert col != null;
    ArrayList<UUID> userList = getController().getBasicUserAccountIDs();
    for (int i = 0; i < userList.size(); i++) {
      Button button = new Button(getController().getLabel(userList.get(i)));
      button.setUserData(userList.get(i));
      button.setOnAction(
          event -> getController().handleUsersVisabilityClick((UUID) button.getUserData()));
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

      // More threading removed from submission due to lack of time to test it thoroughly
      //      Object[] labelWrapper = new Object[1];
      //      Thread labelCheck =
      //          new Thread(new LabelCheck(getController(), deleteList.get(i), labelWrapper));
      //      labelCheck.start();
      Button button = new Button();
      button.setUserData(deleteList.get(i));
      button.setOnAction(event -> getController().handleDeleteClick((UUID) button.getUserData()));
      //      try {
      //        labelCheck.join();
      //      } catch (InterruptedException e) {
      //        Logging.log("Failed to retrieve label in separate thread.", Level.ERROR);
      //      }
      //      button.setText(labelWrapper[0].toString());
      String label = getController().getLabel(deleteList.get(i));
      button.setText(label);
      button.setId("delete_" + label);
      col.getChildren().add(button);
    }
    return col;
  }

  protected Controller getController() {
    return controller;
  }
}
