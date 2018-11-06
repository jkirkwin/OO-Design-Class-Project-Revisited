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

  @SuppressWarnings("unused")
  @Override
  public void start(Stage primaryStage) throws Exception {
    Hub hub = new Hub();
    Stage stage = new Stage();
    stage.setMaxHeight(750);
    stage.setMinHeight(250);
    stage.setMaxWidth(750);
    stage.setMinWidth(250);
    Client client = new Client(stage);
    Controller controller = new Controller(hub, client);
  }
  
}
