package ca.uvic.seng330.assn3.view.scenebuilders;

import ca.uvic.seng330.assn3.controller.HubController;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
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
    HBox hbox = new HBox(5);

    ScrollPane layout = new ScrollPane();
    layout.setFitToHeight(true);
    layout.setFitToWidth(true);
    layout.setHbarPolicy(ScrollBarPolicy.NEVER);
    layout.setVbarPolicy(ScrollBarPolicy.ALWAYS);
    layout.setMaxHeight(540);

    VBox buttons = new VBox(5);
    layout.setContent(hubDeviceList(buttons));

    Button StartUpDown = new Button("ShutDown All");
    StartUpDown.setOnAction(event -> getController().allOff());

    Button userNotifications = new Button("Notifications");
    userNotifications.setOnAction(event -> getController().notificationView());

    VBox hubView = new VBox(5);
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
      Button manageRooms = new Button("Manage Rooms");
      manageRooms.setOnAction(event -> getController().handleAdminManageRoomsClick());
      manageRooms.setId("manage_rooms");

      ScrollPane logScrollPane = new ScrollPane();
      logScrollPane.setFitToHeight(true);
      logScrollPane.setFitToWidth(true);
      logScrollPane.setVbarPolicy(ScrollBarPolicy.ALWAYS);
      logScrollPane.setPrefViewportWidth(600);
      logScrollPane.setContent(logs());
      logScrollPane.setMaxHeight(500);

      HBox upperAdminPanel = new HBox(5);
      upperAdminPanel.getChildren().add(manageUsers);
      upperAdminPanel.getChildren().add(manageDevices);
      upperAdminPanel.getChildren().add(manageRooms);

      adminPanel.getChildren().add(upperAdminPanel);
      adminPanel.getChildren().add(new Label("Recent System Logs"));
      adminPanel.getChildren().add(logScrollPane);

      hbox.getChildren().add(divider);
      hubView = new VBox(10);
      hubView.getChildren().add(new Label("Admin Panel"));
      hubView.getChildren().add(adminPanel);
      hbox.getChildren().add(hubView);
    }

    hbox.setId("specifics");
    return hbox;
  }

  private VBox logs() {
    VBox logsBox = new VBox(5);
    Scanner logsScanner;
    try {
      logsScanner =
          new Scanner(
              new File("src" + File.separator + "logging" + File.separator + "historical.log"));

      for (int i = 0; logsScanner.hasNext() && i < 100; i++) {
        String line = null;
        String rawLine = logsScanner.nextLine();
        try {
          line = rawLine.substring(27);
          line = line.substring(0, 6) + line.substring(44);
        } catch (Exception e) {
          line = rawLine;
        }
        logsBox.getChildren().add(new Label(line));
      }
      logsScanner.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    return logsBox;
  }
}
