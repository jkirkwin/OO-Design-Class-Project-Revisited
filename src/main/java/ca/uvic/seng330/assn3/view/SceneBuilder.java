package ca.uvic.seng330.assn3.view;

import java.util.ArrayList;

import ca.uvic.seng330.assn3.controller.Controller;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public abstract class SceneBuilder {

  private String backText;
  private Controller controller;
  
  public SceneBuilder(Controller controller, String backText) {
    this.backText = backText;
    this.controller = controller;
  }
  
  public Scene build() {
    GridPane grid = new GridPane();
    grid.add(this.buildCommon(), 0, 0);
    
    
    // this is the template part: buildSpecifics is the 'hook' method implemented by the concrete subclasses of this one
    grid.add(this.buildSpecifics(), 1, 1); 
    Scene s = new Scene(grid);
    return s;
  }
  
  // The only thing common to all of our scenes is a back button
  protected Node buildCommon() {
    Button backButton = new Button();
    backButton.setText(this.backText);
    backButton.setOnAction(new EventHandler<ActionEvent>(){
      @Override
      public void handle(ActionEvent event) {
        getController().handleBackClick();
      }
    });
    return backButton;
  }

  protected abstract Node buildSpecifics();

  protected static ScrollPane createScrollPane(Node content) {
    ScrollPane s = new ScrollPane();
    s.setContent(content);
    return s;
  }

  /*
   * Makes the buttons inside a context.
   * ie. inside a ScrollPane
   * TODO: only creates buttons, does not assign Events.
   */
  protected static VBox vList(int numButtons) {
    ArrayList<Button> buttons = new ArrayList<Button>();
    VBox vbox = new VBox(5);
    for (int i = 0; i < numButtons; i++) {
      buttons.add(i, new Button("I am " + i));
      vbox.getChildren().add(buttons.get(i));
    }
    return vbox;
  }

  protected Controller getController() {
    return controller;
  }
}
