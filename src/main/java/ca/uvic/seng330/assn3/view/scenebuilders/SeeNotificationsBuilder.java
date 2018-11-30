package ca.uvic.seng330.assn3.view.scenebuilders;

import ca.uvic.seng330.assn3.controller.Controller;
import ca.uvic.seng330.assn3.controller.HubController;
import java.util.Stack;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.json.JSONObject;

public class SeeNotificationsBuilder extends SceneBuilder {

  public SeeNotificationsBuilder(Controller controller, String backText) {
    super(controller, backText);
  }

  @Override
  protected Node buildSpecifics() {
    ScrollPane layout = new ScrollPane();
    layout.setFitToHeight(true);
    layout.setFitToWidth(true);
    layout.setHbarPolicy(ScrollBarPolicy.ALWAYS);
    layout.setVbarPolicy(ScrollBarPolicy.ALWAYS);
    layout.setPrefViewportWidth(500);
    layout.setPrefViewportHeight(100);

    VBox rows = new VBox(10);
    Stack<JSONObject> notifications = new HubController().getNotifications();
    HBox message;
    JSONObject curr;
    Label date;
    Label msg;
    while (!notifications.isEmpty()) {
      message = new HBox();
      curr = notifications.pop();

      date = new Label(curr.get("sent_at").toString());
      msg = new Label(curr.get("payload").toString());

      message.getChildren().add(date);
      message.getChildren().add(msg);
      rows.getChildren().add(message);
    }

    layout.setContent(rows);
    return layout;
  }
}
