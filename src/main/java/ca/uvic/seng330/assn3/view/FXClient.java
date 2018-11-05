package ca.uvic.seng330.assn3.view;

import ca.uvic.seng330.assn3.controller.Controller;
import javafx.stage.Stage;

public class FXClient extends Client {

  private final Stage stage;

  /*
   * @pre c != null
   * @pre v != null
   */
  public FXClient(Controller controller, Stage stage) {
    super(controller);
    this.stage = stage;
  }

  /*
   * @pre builder != null
   */
  @Override
  public void setView(SceneBuilder builder) {
    assert builder != null;
    this.stage.setScene(builder.build());
  }
}
