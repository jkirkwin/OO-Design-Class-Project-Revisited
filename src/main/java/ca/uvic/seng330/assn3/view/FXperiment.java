package ca.uvic.seng330.assn3.view;

import java.util.ArrayList;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Labeled;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class FXperiment extends Application implements EventHandler<ActionEvent> {

  ArrayList<Button> hubDevices = new ArrayList<Button>();
  //	HashMap<Device, Button> hubDevices = new HashMap<Device, Button>();

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    logInView(primaryStage);
    // hubView(primaryStage);
  }

  private void logInView(Stage primaryStage) {
    primaryStage.setTitle("LogIn");

    HBox layout = new HBox(10);
    VBox col1 = new VBox(10);
    String[] LogInPrompts = new String[2];
    LogInPrompts[0] = "UserName";
    LogInPrompts[1] = "Password";
    layout.getChildren().add(vList(LogInPrompts, col1, false));
    VBox col2 = new VBox(10);
    String[] LogInOptions = new String[3];
    LogInOptions[0] = "LogIn";
    LogInOptions[1] = "New User";
    LogInOptions[2] = "New Admin";
    layout.getChildren().add(vList(LogInOptions, col2, true));

    //    	  col1.getChildren().get(0);
    //    	  col1.getChildren().get(1);
    //
    //	  col2.getChildren().get(0);
    //	  col2.getChildren().get(1);
    //	  col2.getChildren().get(2);

    Scene scene = new Scene(layout, 300, 250);
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  private void hubView(Stage primaryStage) {
    primaryStage.setTitle("HubView");
    ScrollPane layout = new ScrollPane();

    VBox buttons = new VBox(10);
    String[] deviceNames = new String[16];
    for (int i = 0; i < deviceNames.length; i++) {
      deviceNames[i] = "I am " + i;
    }
    vList(deviceNames, buttons, true);
    layout.setContent(buttons);
    for (Node n : buttons.getChildren()) {
      ((Labeled) n).setText("cheese");
    }

    Scene scene = new Scene(layout, 300, 250);
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  private VBox vList(String[] namesList, VBox col, boolean isButton) {
    for (int i = 0; i < namesList.length; i++) {
      if (isButton) {
        hubDevices.add(i, new Button(namesList[i]));
        hubDevices.get(i).setOnAction(this);
        col.getChildren().add(hubDevices.get(i));
      } else {
        col.getChildren().add(new TextField(namesList[i]));
      }
    }
    return col;
  }

  private Button buttonBuilder() {
    return null;
  }

  private TextField inputBuilder() {
    return null;
  }

  @Override
  public void handle(ActionEvent event) {
    /* TODO:
     * tie buttons to devices of hub in a hashMap or similar
     * find devicetype for the else if statements
     */
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
