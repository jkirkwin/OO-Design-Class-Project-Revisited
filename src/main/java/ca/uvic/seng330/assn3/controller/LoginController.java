package ca.uvic.seng330.assn3.controller;

import ca.uvic.seng330.assn3.model.AccessLevel;
import ca.uvic.seng330.assn3.model.Hub;
import ca.uvic.seng330.assn3.model.UserAccount;
import ca.uvic.seng330.assn3.view.Client;
import javafx.scene.control.Alert.AlertType;

public class LoginController {
  /*
   * @pre username != null
   * @pre password != null
   */
  public static void handleLoginClick(
      Controller controller, Hub hub, Client client, String username, String password) {
    assert username != null;
    assert password != null;
    if (hub.isUser(username)) {
      System.out.println("Logged in"); // Testing
      controller.activeUser = hub.getUser(username, password);
      if (controller.activeUser.isAdmin()) {
        client.setView(controller.findBuilder(ViewType.HUB_ADMIN));
      } else {
        client.setView(controller.findBuilder(ViewType.HUB_BASIC));
      }
    } else {
      client.alertUser(
          AlertType.INFORMATION,
          "Failure",
          "Account does not exist",
          "Username \"" + username + "\" is not in use. Please try a different one.");
    }
  }

  /*
   * @pre username != null
   * @pre password != null
   */
  public static void handleNewUser(Hub hub, Client client, String username, String password) {
    assert username != null;
    assert password != null;
    if (acceptableInputs(client, username, password)) {
      if (!hub.isUser(username)) {
        hub.register(new UserAccount(hub, AccessLevel.BASIC, username, password));
        client.alertUser(AlertType.INFORMATION, "Success", "Success", "User Account Created.");
      } else {
        client.alertUser(
            AlertType.INFORMATION,
            "Failure",
            "Username Unavailable",
            "Username \"" + username + "\" is already in use. Please try a different one.");
      }
    }
  }

  /*
   * @pre username != null
   * @pre password != null
   */
  public static void handleNewAdmin(Hub hub, Client client, String username, String password) {
    assert username != null;
    assert password != null;
    if (acceptableInputs(client, username, password)) {
      if (!hub.isUser(username)) {
        hub.register(new UserAccount(hub, AccessLevel.ADMIN, username, password));
        client.alertUser(AlertType.INFORMATION, "Success", "Success", "Admin Account Created.");
      } else {
        client.alertUser(
            AlertType.INFORMATION,
            "Failure",
            "Username Unavailable",
            "Username \"" + username + "\" is already in use. Please try a different one.");
      }
    }
  }

  /*
   * @pre username != null
   * @pre password != null
   */
  private static boolean acceptableInputs(Client client, String username, String password) {
    assert username != null;
    assert password != null;
    if (username.toLowerCase().equals("username") || username.isEmpty()) {
      client.alertUser(
          AlertType.INFORMATION,
          "Failure",
          "Username Unaccepted",
          "Username is not allowed to be \"username\" or left blank. Please try again");
      return false;
    } else if (password.toLowerCase() == "password" || password.isEmpty()) {
      client.alertUser(
          AlertType.INFORMATION,
          "Failure",
          "Password Unaccepted",
          "Password is not allowed to be \"password\" or left blank. Please try again");
      return false;
    }
    return true;
  }
}
