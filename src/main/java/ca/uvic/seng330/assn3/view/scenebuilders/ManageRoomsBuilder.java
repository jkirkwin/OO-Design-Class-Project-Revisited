package ca.uvic.seng330.assn3.view.scenebuilders;

import ca.uvic.seng330.assn3.controller.Controller;
import ca.uvic.seng330.assn3.controller.RoomController;
import java.util.ArrayList;
import java.util.UUID;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ManageRoomsBuilder extends SceneBuilder {

  public ManageRoomsBuilder(Controller controller, String backText) {
    super(controller, backText);
  }

  @Override
  public RoomController getController() {
    return (RoomController) super.getController();
  }

  @Override
  protected Node buildSpecifics() {

    VBox total = new VBox(5);
    HBox newRoom = new HBox(5);

    TextField roomName = new TextField();
    roomName.setPromptText("Room Label");
    Button makeRoom = new Button("Create Room");
    makeRoom.setOnAction(event -> getController().makeNewRoom(roomName.getText()));

    newRoom.getChildren().add(roomName);
    newRoom.getChildren().add(makeRoom);

    ScrollPane layout = new ScrollPane();
    layout.setFitToHeight(true);
    layout.setFitToWidth(true);
    layout.setHbarPolicy(ScrollBarPolicy.NEVER);
    layout.setVbarPolicy(ScrollBarPolicy.ALWAYS);

    HBox hbox = new HBox(30);

    VBox viewUsers = hubRoomsList(new VBox(10));
    viewUsers.getChildren().add(0, new Label("View Room\nOccupants"));
    VBox deleteUsers = hubDeleteList(new VBox(10), getController().getRoomIDs());
    deleteUsers.getChildren().add(0, new Label("Delete Room\n\n"));

    hbox.getChildren().add(viewUsers);
    hbox.getChildren().add(deleteUsers);
    layout.setContent(hbox);
    total.getChildren().add(newRoom);
    total.getChildren().add(layout);

    return total;
  }

  private VBox hubRoomsList(VBox col) {
    assert col != null;
    ArrayList<UUID> roomsList = getController().getRoomIDs();
    for (int i = 0; i < roomsList.size(); i++) {
      Button button = new Button(getController().getLabel(roomsList.get(i)));
      button.setUserData(roomsList.get(i));
      button.setOnAction(
          event -> getController().handleRoomsAssignmentClick((UUID) button.getUserData()));
      col.getChildren().add(button);
    }
    return col;
  }
}
