package ca.uvic.seng330.assn3.controller;

import ca.uvic.seng330.assn3.logging.Logging;
import ca.uvic.seng330.assn3.model.AccessLevel;
import ca.uvic.seng330.assn3.model.UserAccount;
import java.util.NoSuchElementException;

import org.slf4j.event.Level;

import javafx.scene.control.Alert.AlertType;

public class LoginController extends Controller {

  /*
   * @pre username != null
   * @pre password != null
   */
  public void handleLoginClick(String username, String password) {
    assert username != null;
    assert password != null;
    if (hub.isUser(username)) {
      try {
        activeUser = hub.getUser(username, password);
      } catch (NoSuchElementException e) {
        client.alertUser(
            AlertType.INFORMATION,
            "Failure",
            "Incorrect password",
            "The credentials entered are incorrect. Please try a different password or just look at the storage files because security reasons.");
        Logging.log("Failed Login for user "+username, Level.WARN);
        return;
      }
      if (activeUser.isAdmin()) {
        client.setView(findBuilder(ViewType.HUB_ADMIN));
        Logging.log(username+" logged in as Admin.", Level.INFO);
      } else {
        client.setView(findBuilder(ViewType.HUB_BASIC));
        Logging.log(username+" logged in as Basic User.", Level.INFO);
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
  public void handleNewUser(String username, String password) {
    assert username != null;
    assert password != null;
    if (acceptableInputs(username, password)) {
      if (!hub.isUser(username)) {
        new UserAccount(hub, AccessLevel.BASIC, username, password);
        client.alertUser(AlertType.INFORMATION, "Success", "Success", "User Account Created.");
        Logging.log("Basic User "+username+" created.", Level.INFO);
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
  public void handleNewAdmin(String username, String password) {
    assert username != null;
    assert password != null;
    if (acceptableInputs(username, password)) {
      if (!hub.isUser(username)) {
        new UserAccount(hub, AccessLevel.ADMIN, username, password);
        client.alertUser(AlertType.INFORMATION, "Success", "Success", "Admin Account Created.");
        Logging.log("Admin User "+username+" created.", Level.INFO);
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
  private boolean acceptableInputs(String username, String password) {
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
