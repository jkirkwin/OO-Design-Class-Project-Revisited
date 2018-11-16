package ca.uvic.seng330.assn3.view;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.LabeledMatchers.hasText;

import ca.uvic.seng330.assn3.controller.Controller;
import ca.uvic.seng330.assn3.model.AccessLevel;
import ca.uvic.seng330.assn3.model.Hub;
import ca.uvic.seng330.assn3.model.UserAccount;
import ca.uvic.seng330.assn3.startup.Startup;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class TestLogin extends ApplicationTest {
  
  private Hub hub;
  private Controller controller;
  private Client client;
  
  @SuppressWarnings("unused")
  @Override
  public void start(Stage primaryStage) throws Exception {
    this.hub = new Hub();
    primaryStage.setMaxHeight(750);
    primaryStage.setMinHeight(250);
    primaryStage.setMaxWidth(750);
    primaryStage.setMinWidth(250);
    this.client = new Client(primaryStage);
    this.controller = new Controller(hub, client);
  }
  
//   TODO Set up a testing directory so that the state of the model is not deleted when these tests run
  @Before
  public void setup() {
    try {
      GUITestUtilities.deleteState();
    } catch (NoSuchMethodException
        | SecurityException
        | IllegalAccessException
        | IllegalArgumentException
        | InvocationTargetException 
        | NoSuchFieldException e) {
      e.printStackTrace();
    }    
  }

  @After
  public void teardown() {
    try {
      GUITestUtilities.deleteState();
    } catch (NoSuchMethodException
        | SecurityException
        | IllegalAccessException
        | IllegalArgumentException
        | InvocationTargetException 
        | NoSuchFieldException e) {
      e.printStackTrace();
    }
  }
  
  
  @Test
  public void testAdminLogin() throws InterruptedException {
    new UserAccount(hub, AccessLevel.ADMIN, "user", "pw");
    clickOn("username");
    write("user");
    clickOn("password");
    write("pw");    
    clickOn("Login");
    assertEquals(client.getWindow().getTitle(), "HUB_ADMIN");
  }

  @Test
  public void testBasicLogin() {
    new UserAccount(hub, AccessLevel.BASIC, "user", "pw");
    clickOn("username");
    write("user");
    clickOn("password");
    write("pw");    
    clickOn("Login");
    assertEquals(client.getWindow().getTitle(), "HUB_BASIC");
  }
  
  @Test
  public void testNewAdminUser() {
    clickOn("username");
    write("admin");
    clickOn("password");
    write("1");
    clickOn("New Admin");
    clickOn("OK");
    assertTrue(hub.isUser("admin"));
    UserAccount u = hub.getUser("admin", "1");
    assertTrue(u.isAdmin());
    assertTrue(hub.isRegisteredUserAccount(u.getIdentifier()));
  }

  @Test
  public void testNewBasicUser() {
    GUITestUtilities.makeBasicUser(this, "user1", "password1");
    assertTrue(hub.isUser("user1"));
    UserAccount u = hub.getUser("user1", "password1");
    assertFalse(u.isAdmin());
    assertTrue(hub.isRegisteredUserAccount(u.getIdentifier()));
  }
  
  @Test 
  public void gotonotifs() {
    GUITestUtilities.goToManageNotifications(this);
  }
}