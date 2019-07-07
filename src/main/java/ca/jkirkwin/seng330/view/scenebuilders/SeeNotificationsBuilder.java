package ca.jkirkwin.seng330.view.scenebuilders;

import ca.jkirkwin.seng330.controller.Controller;
import ca.jkirkwin.seng330.controller.HubController;
import java.util.Iterator;
import java.util.Stack;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.Separator;
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
    layout.setMinViewportHeight(100);
    layout.setMaxHeight(500);

    VBox rows = new VBox(20);
    Stack<JSONObject> notifications = new HubController().getNotifications();
    VBox message;
    JSONObject curr;
    Label label;
    while (!notifications.isEmpty()) {
      message = new VBox(10);
      curr = notifications.pop();
      Iterator<String> keyIterator = curr.keys();
      while (keyIterator.hasNext()) {
        String key = keyIterator.next();
        label = new Label(key + " : " + curr.get(key).toString());
        message.getChildren().add(label);
      }
      rows.getChildren().add(message);
      if (!notifications.isEmpty()) {
        Separator s = new Separator();
        rows.getChildren().add(s);
      }
    }

    layout.setContent(rows);
    return layout;
  }
}
