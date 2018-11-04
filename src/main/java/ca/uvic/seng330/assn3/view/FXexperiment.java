package ca.uvic.seng330.assn3.view;

import java.util.ArrayList;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class FXexperiment extends Application implements EventHandler<ActionEvent> {

  ArrayList<Button> hubDevices = new ArrayList<Button>();
  //	HashMap<Device, Button> hubDevices = new HashMap<Device, Button>();

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    primaryStage.setTitle("HubView");
    VBox layout = new VBox();
    int i = 0;
    while (i < 8) {
      hubDevices.add(i, new Button("I am " + i));
      hubDevices.get(i).setOnAction(this);
      layout.getChildren().add(hubDevices.get(i));
      i++;
    }

    Scene scene = new Scene(layout, 300, 250);
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  @Override
  public void handle(ActionEvent event) {
    Object source = event.getSource();
    if (source == hubDevices.get(0)) {
      hubDevices.get(0).setText("Jaime");
    } else if (source == hubDevices.get(1)) {
      hubDevices.get(1).setText("is");
    } else if (source == hubDevices.get(2)) {
      hubDevices.get(2).setText("a");
    } else if (source == hubDevices.get(3)) {
      hubDevices.get(3).setText("punk.");
    } else if (source == hubDevices.get(4)) {
      hubDevices.get(4).setText("Connor");
    } else if (source == hubDevices.get(5)) {
      hubDevices.get(5).setText("is");
    } else if (source == hubDevices.get(6)) {
      hubDevices.get(6).setText("the");
    } else if (source == hubDevices.get(7)) {
      hubDevices.get(7).setText("WINNER!!");
    }
  }
}
