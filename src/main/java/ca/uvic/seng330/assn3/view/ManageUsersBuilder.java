package ca.uvic.seng330.assn3.view;

import ca.uvic.seng330.assn3.controller.Controller;
import javafx.scene.Node;
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
    // TODO: re-factor out hub and this into new method somewhere
    ScrollPane layout = new ScrollPane();
    layout.setFitToHeight(true);
    layout.setFitToWidth(true);
    layout.setHbarPolicy(ScrollBarPolicy.NEVER);
    layout.setVbarPolicy(ScrollBarPolicy.ALWAYS);

    HBox hbox = new HBox(30);
    VBox viewUsers = hubUsersList(new VBox(10));
    viewUsers.getChildren().add(0, new Label("View User\nVisability"));
    VBox killUsers = hubKillList(new VBox(10), getController().getAccountIDList());
    killUsers.getChildren().add(0, new Label("Kill User\nAccount"));
    hbox.getChildren().add(viewUsers);
    hbox.getChildren().add(killUsers);

    layout.setContent(hbox);

    return layout;
  }
}
