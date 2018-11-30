package ca.uvic.seng330.assn3.view.scenebuilders;

import ca.uvic.seng330.assn3.controller.Controller;
import ca.uvic.seng330.assn3.controller.RoomController;
import java.util.ArrayList;
import java.util.UUID;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.VBox;

public class ManageRoomOccupantsBuilder extends SceneBuilder {

  private final UUID currRoom;

  public ManageRoomOccupantsBuilder(Controller controller, String backText, UUID currRoom) {
    super(controller, backText);
    this.currRoom = currRoom;
  }

  @Override
  public RoomController getController() {
    return (RoomController) super.getController();
  }

  @Override
  protected Node buildSpecifics() {

    ScrollPane layout = new ScrollPane();
    layout.setFitToHeight(true);
    layout.setFitToWidth(true);
    layout.setHbarPolicy(ScrollBarPolicy.NEVER);
    layout.setVbarPolicy(ScrollBarPolicy.ALWAYS);

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
                  + getController().isRoomOccupant(this.currRoom, deviceList.get(i)));
      button.setId(getController().getLabel(deviceList.get(i)));
      button.setUserData(deviceList.get(i));
      button.setOnAction(
          event -> getController().deviceInRoomToggle(this.currRoom, (UUID) button.getUserData()));
      col.getChildren().add(button);
    }
    return col;
  }
}
