package ca.jkirkwin.seng330.startup;

import ca.jkirkwin.seng330.controller.Controller;
import ca.jkirkwin.seng330.controller.LoginController;
import ca.jkirkwin.seng330.controller.ViewType;
import ca.jkirkwin.seng330.logging.Logging;
import ca.jkirkwin.seng330.model.Hub;
import ca.jkirkwin.seng330.view.Client;
import javafx.application.Application;
import javafx.stage.Stage;

public class Startup extends Application {
  public static void main(String[] args) {
    launch();
  }

  private Controller controller;
  private Client client;
  private Hub hub;

  @SuppressWarnings("unused")
  @Override
  public void start(Stage primaryStage) throws Exception {
    Logging.init();

    hub = new Hub();
    hub.startup();

    primaryStage.setMinHeight(250);
    primaryStage.setMinWidth(250);
    client = new Client(primaryStage);
    controller = new LoginController();
    controller.init(client, hub);
    client.setController(controller);
    client.getWindow().setOnCloseRequest(event -> controller.exitApplication());
    client.setView(controller.findBuilder(ViewType.LOGIN));
  }
}
