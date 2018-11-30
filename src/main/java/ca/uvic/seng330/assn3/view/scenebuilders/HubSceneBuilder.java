package ca.uvic.seng330.assn3.view.scenebuilders;

import ca.uvic.seng330.assn3.controller.HubController;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class HubSceneBuilder extends SceneBuilder {

  private final boolean isAdmin;

  public HubSceneBuilder(HubController controller, String backText, boolean isAdmin) {
    super(controller, backText);
    this.isAdmin = isAdmin;
  }

  @Override
  public HubController getController() {
    return (HubController) super.getController();
  }

  @Override
  protected Node buildSpecifics() {
    HBox hbox = new HBox(10);

    ScrollPane layout = new ScrollPane();
    layout.setFitToHeight(true);
    layout.setFitToWidth(true);
    layout.setHbarPolicy(ScrollBarPolicy.NEVER);
    layout.setVbarPolicy(ScrollBarPolicy.ALWAYS);

    VBox buttons = new VBox(10);
    layout.setContent(hubDeviceList(buttons));

    Button StartUpDown = new Button("ShutDown All");
    StartUpDown.setOnAction(event -> getController().allOff());

    Button userNotifications = new Button("Notifications");
    userNotifications.setOnAction(event -> getController().notificationView());

    VBox hubView = new VBox(10);
    hubView.getChildren().add(new Label("Device Config"));
    hubView.getChildren().add(layout);
    hubView.getChildren().add(StartUpDown);
    hubView.getChildren().add(userNotifications);
    hbox.getChildren().add(hubView);

    if (isAdmin) {
      Separator divider = new Separator(Orientation.VERTICAL);

      VBox adminPanel = new VBox(5);
      adminPanel.setId("admin_panel");
      Button manageUsers = new Button("Manage Users");
      manageUsers.setId("manage_users");
      manageUsers.setOnAction(event -> getController().handleAdminManageUsersClick());
      Button manageDevices = new Button("Manage Devices");
      manageDevices.setOnAction(event -> getController().handleAdminManageDevicesClick());
      manageDevices.setId("manage_devices");
      Button manageNotifications = new Button("Manage Notifications");
      manageNotifications.setId("manage_notifications");
      manageNotifications.setOnAction(
          event -> getController().handleAdminManageNotificationsClick());

      adminPanel.getChildren().add(manageUsers);
      adminPanel.getChildren().add(manageDevices);
      adminPanel.getChildren().add(manageNotifications);

      hbox.getChildren().add(divider);
      hubView = new VBox(10);
      hubView.getChildren().add(new Label("Admin Panel"));
      hubView.getChildren().add(adminPanel);
      hbox.getChildren().add(hubView);
    }

    hbox.setId("specifics");
    return hbox;
  }
}
