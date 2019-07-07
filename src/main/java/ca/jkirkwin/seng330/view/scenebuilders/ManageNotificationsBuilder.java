package ca.jkirkwin.seng330.view.scenebuilders;

import ca.jkirkwin.seng330.controller.Controller;
import java.util.ArrayList;
import java.util.UUID;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.VBox;

public class ManageNotificationsBuilder extends SceneBuilder {

  private final UUID currUserVisibility;

  public ManageNotificationsBuilder(
      Controller controller, String backText, UUID currUserVisibility) {
    super(controller, backText);
    this.currUserVisibility = currUserVisibility;
  }

  @Override
  protected Node buildSpecifics() {

    ScrollPane layout = new ScrollPane();
    layout.setFitToHeight(true);
    layout.setFitToWidth(true);
    layout.setHbarPolicy(ScrollBarPolicy.NEVER);
    layout.setVbarPolicy(ScrollBarPolicy.ALWAYS);
    layout.setMaxHeight(200);
    layout.setMinWidth(300);

    layout.setContent(hubDeviceList(new VBox(10)));

    return layout;
  }

  @Override
  protected VBox hubDeviceList(VBox col) {
    assert col != null;
    ArrayList<UUID> deviceList = getController().getDeviceIDList();
    for (int i = 0; i < deviceList.size(); i++) {
      Button button =
          new Button(
              getController().getLabel(deviceList.get(i))
                  + " - "
                  + getController()
                      .isDeviceBlackListed(this.currUserVisibility, deviceList.get(i)));
      button.setId(getController().getLabel(deviceList.get(i)));
      button.setUserData(deviceList.get(i));
      button.setOnAction(
          event ->
              getController()
                  .blackListToggle(this.currUserVisibility, (UUID) button.getUserData()));
      col.getChildren().add(button);
    }
    return col;
  }
}
