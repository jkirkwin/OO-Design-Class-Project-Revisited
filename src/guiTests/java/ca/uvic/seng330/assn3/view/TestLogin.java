package ca.uvic.seng330.assn3.view;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import ca.uvic.seng330.assn3.controller.Controller;
import ca.uvic.seng330.assn3.model.Hub;
import javafx.stage.Stage;

public class TestLogin extends ApplicationTest {
  
  @SuppressWarnings("unused")
  @Override
  public void start(Stage primaryStage) {
    Hub hub = new Hub();
    primaryStage.setMaxHeight(750);
    primaryStage.setMinHeight(250);
    primaryStage.setMaxWidth(750);
    primaryStage.setMinWidth(250);
    Client client = new Client(primaryStage);
    Controller controller = new Controller(hub, client);
  }
  
  @Test
  public void testAdminLogin() {
    // TODO 
    assertTrue(false);
  }

  @Test
  public void testBasicLogin() {
    // TODO 
    assertTrue(false);
  }
  
  @Test
  public void testNewAdminUser() {
    // TODO 
    assertTrue(false);
  }

  @Test
  public void testNewBasicUser() {
    // TODO 
    assertTrue(false);
  }
}