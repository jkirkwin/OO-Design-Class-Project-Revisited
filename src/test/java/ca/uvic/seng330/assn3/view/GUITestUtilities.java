package ca.uvic.seng330.assn3.view;

import ca.uvic.seng330.assn3.model.storage.Storage;
import ca.uvic.seng330.assn3.model.storage.TestStorage;
import ca.uvic.seng330.assn3.startup.Startup;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import org.testfx.framework.junit.ApplicationTest;

public class GUITestUtilities {
  // A collection of utilities to make UI testing easier

  private static int adminSuffix = 0;
  private static int basicSuffix = 0;

  public static Field getAccessibleField(Startup app, String fieldName) {
    Field f = null;
    try {
      f = app.getClass().getDeclaredField(fieldName);
      f.setAccessible(true);
    } catch (NoSuchFieldException | SecurityException e) {
      e.printStackTrace();
    }
    return f;
  }

  public static void wait(int seconds) {
    try {
      Thread.sleep(seconds * 1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public static void deleteState()
      throws NoSuchMethodException, SecurityException, IllegalAccessException,
          IllegalArgumentException, InvocationTargetException, NoSuchFieldException {
    Field storageDirPath = Storage.class.getDeclaredField("storageDirPath");
    storageDirPath.setAccessible(true);
    File storageDir = new File((String) storageDirPath.get(null));
    if (storageDir.exists()) {
      TestStorage.deleteDir(storageDir);
    }
  }

  public static void makeBasicUser(ApplicationTest test, String username, String password) {
    test.clickOn("#username");
    test.write(username);
    test.clickOn("#password");
    test.write(password);
    test.clickOn("#new_user");
    test.clickOn("OK");
  }

  public static void makeAdminUser(ApplicationTest test, String username, String password) {
    test.clickOn("#username");
    test.write(username);
    test.clickOn("#password");
    test.write(password);
    test.clickOn("#new_admin");

    try {
      test.clickOn("OK");
    } catch (Exception e) {
      // TODO Throwing exception when run from cmd line with `gradle test` but not when run in
      // eclipse
      // supposedly there is not okay button, if gradle is to be believed
      // when watching it run, I don't see the app launch as it does for the other GUI tests
      // (TestLogin and TestDeviceUsage)
      System.out.println("The exception was thrown!!!");
    }
  }

  public static void login(ApplicationTest test, String username, String password) {
    test.clickOn("#username");
    test.write(username);
    test.clickOn("#password");
    test.write(password);
    test.clickOn("#login");
  }

  public static void goToAdminHub(ApplicationTest test) {
    makeAdminUser(test, "admin" + adminSuffix++, "admin");
    test.clickOn("#login");
  }

  public static void goToBasicHub(ApplicationTest test) {
    makeBasicUser(test, "basic" + basicSuffix++, "basic");
    test.clickOn("#login");
  }

  public static void goToManageUsers(ApplicationTest test) {
    goToAdminHub(test);
    test.clickOn("#manage_users");
  }

  public static void goToManageDevices(ApplicationTest test) {
    goToAdminHub(test);
    test.clickOn("#manage_devices");
  }

  public static void goToManageNotifications(ApplicationTest test) {
    goToAdminHub(test);
    test.clickOn("#manage_notifications");
  }

  public static void goToCreateDevice(ApplicationTest test) {
    goToManageDevices(test);
    test.clickOn("#new_device");
  }

  public static void backToLogin(ApplicationTest test, Client client) {
    while (!client.getWindow().getTitle().equals("LOGIN")) {
      System.out.println(client.getWindow().getTitle());
      test.clickOn("#back");
    }
  }
}
