package ca.jkirkwin.seng330.view.scenebuilders;

import ca.jkirkwin.seng330.controller.Controller;
import java.util.ArrayList;
import java.util.UUID;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ManageUsersBuilder extends SceneBuilder {

  public ManageUsersBuilder(Controller controller, String backText) {
    super(controller, backText);
  }

  @Override
  protected Node buildSpecifics() {
    ScrollPane layout = new ScrollPane();
    layout.setFitToHeight(true);
    layout.setFitToWidth(true);
    layout.setHbarPolicy(ScrollBarPolicy.NEVER);
    layout.setVbarPolicy(ScrollBarPolicy.ALWAYS);
    layout.setMinSize(300, 100);
    layout.setMaxHeight(300);

    HBox hbox = new HBox(70);
    HBox titles = new HBox(90);

    VBox viewUsers = hubUsersList(new VBox(10));
    titles.getChildren().add(new Label("View User Access"));
    VBox deleteUsers = hubDeleteList(new VBox(10), getController().getBasicUserAccountIDs());
    titles.getChildren().add(new Label("Delete User Account"));

    hbox.getChildren().add(viewUsers);
    hbox.getChildren().add(deleteUsers);
    layout.setContent(hbox);

    VBox total = new VBox();
    total.getChildren().add(titles);
    total.getChildren().add(layout);

    return total;
  }

  @Override
  protected VBox hubUsersList(VBox col) {
    assert col != null;
    ArrayList<UUID> userList = getController().getBasicUserAccountIDs();
    for (int i = 0; i < userList.size(); i++) {
      String username = getController().getLabel(userList.get(i));
      Button button = new Button(username);
      button.setId(username);
      button.setUserData(userList.get(i));
      button.setOnAction(
          event -> getController().handleUsersVisabilityClick((UUID) button.getUserData()));
      col.getChildren().add(button);
    }
    return col;
  }
}
