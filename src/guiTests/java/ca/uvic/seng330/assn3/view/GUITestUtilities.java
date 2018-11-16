package ca.uvic.seng330.assn3.view;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.testfx.framework.junit.ApplicationTest;

import ca.uvic.seng330.assn3.model.storage.Storage;
import ca.uvic.seng330.assn3.model.storage.TestStorage;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class GUITestUtilities {
  // A collection of utilities to make UI testing easier
  
  public static void deleteState() throws NoSuchMethodException, SecurityException, 
      IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException {
    Field storageDirPath = Storage.class.getDeclaredField("storageDirPath");
    storageDirPath.setAccessible(true);
    File storageDir = new File((String) storageDirPath.get(null));
    if(storageDir.exists()) {
      TestStorage.deleteDir(storageDir);      
    }
  }
  
  public static void makeBasicUser(ApplicationTest test, String username, String password) {
    test.clickOn("username");
    test.write(username);
    test.clickOn("password");
    test.write(password);
    test.clickOn("New User");
    test.clickOn("OK");
  }

  public static void makeAdminUser(ApplicationTest test, String username, String password) {
    test.clickOn("username");
    test.write(username);
    test.clickOn("password");
    test.write(password);
    test.clickOn("New Admin");
    test.clickOn("OK");
  }
  
  public static void login(ApplicationTest test, String username, String password) {
    test.clickOn("username");
    test.write(username);
    test.clickOn("password");
    test.write(password);
    test.clickOn("Login");
  }
  
  public static void goToAdminHub(ApplicationTest test) {
    makeAdminUser(test, "admin", "admin");
    test.clickOn("Login");
  }
  
  public static void goToBasicHub(ApplicationTest test) {
    makeBasicUser(test, "basic", "basic");
    test.clickOn("Login");
  }
  
  public static void goToManageUsers(ApplicationTest test) {
    goToAdminHub(test);
    test.clickOn("Manage Users");
  }
  public static void goToManageDevices(ApplicationTest test) {
    goToAdminHub(test);
    test.clickOn("Manage Devices");
  }
    
  public static void goToManageNotifications(ApplicationTest test) {
    goToAdminHub(test);
    test.clickOn("Manage Notifications");
  }
}
