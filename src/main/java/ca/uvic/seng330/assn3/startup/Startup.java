package ca.uvic.seng330.assn3.startup;

import ca.uvic.seng330.assn3.controller.Controller;
import ca.uvic.seng330.assn3.model.Hub;
import ca.uvic.seng330.assn3.view.Client;
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
    hub = new Hub();
    primaryStage.setMaxHeight(750);
    primaryStage.setMinHeight(250);
    primaryStage.setMaxWidth(750);
    primaryStage.setMinWidth(250);
    client = new Client(primaryStage);
    controller = new Controller(hub, client);
  }
}
