package ca.uvic.seng330.assn3.intro;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
/*
 * Code sample from https://stackoverflow.com/questions/36868391/using-javafx-controller-without-fxml/36873768
 */
public class MVCExample extends Application {

  @Override
  public void start(Stage primaryStage) {
    AdditionModel model = new AdditionModel();
    AdditionController controller = new AdditionController(model);
    AdditionView view = new AdditionView(controller, model);

    Scene scene = new Scene(view.asParent(), 400, 400);
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
