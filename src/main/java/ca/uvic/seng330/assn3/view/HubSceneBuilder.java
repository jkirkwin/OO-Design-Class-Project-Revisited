package ca.uvic.seng330.assn3.view;

import ca.uvic.seng330.assn3.controller.Controller;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class HubSceneBuilder extends SceneBuilder {

  private final boolean isAdmin;

  public HubSceneBuilder(Controller controller, String backText, boolean isAdmin) {
    super(controller, backText);
    this.isAdmin = isAdmin;
  }

  @Override
  protected Node buildSpecifics() {
    // TODO: change stagename to Hubview
    HBox hbox = new HBox(10);

    ScrollPane layout = new ScrollPane();
    layout.setFitToHeight(true);
    layout.setFitToWidth(true);
    layout.setHbarPolicy(ScrollBarPolicy.NEVER);
    layout.setVbarPolicy(ScrollBarPolicy.ALWAYS);
    VBox buttons = new VBox(10);

    String[] deviceNames = new String[16]; // change to get device list
    for (int i = 0; i < deviceNames.length; i++) {
      deviceNames[i] = "I am " + i; // iterate through device list
    }

    vList(deviceNames, buttons, true);
    layout.setContent(buttons);
    hbox.getChildren().add(layout);

    if (isAdmin) {
      VBox adminPanel = new VBox(5);
      Button manageUsers = new Button("manageUsers");
      manageUsers.setOnAction(event -> getController().handleAdminManageUsersClick());
      Button manageDevices = new Button("manageDevices");
      manageDevices.setOnAction(event -> getController().handleAdminManageDevicesClick());
      Button manageNotifications = new Button("manageNotifications");
      manageNotifications.setOnAction(
          event -> getController().handleAdminManageNotificationsClick());

      adminPanel.getChildren().add(manageUsers);
      adminPanel.getChildren().add(manageDevices);
      adminPanel.getChildren().add(manageNotifications);

      hbox.getChildren().add(adminPanel);
    }

    return hbox;
  }
}
