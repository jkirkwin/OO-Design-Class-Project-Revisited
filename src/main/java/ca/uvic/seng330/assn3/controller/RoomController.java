package ca.uvic.seng330.assn3.controller;

import ca.uvic.seng330.assn3.model.HubRegistrationException;
import ca.uvic.seng330.assn3.model.Room;
import ca.uvic.seng330.assn3.model.devices.Device;
import ca.uvic.seng330.assn3.view.scenebuilders.ManageRoomOccupantsBuilder;
import java.util.ArrayList;
import java.util.UUID;

public class RoomController extends Controller {

  public void handleRoomsAssignmentClick(UUID userData) {
    client.setTitle("MANAGE_ROOMS_DEVICES");
    client.setView(new ManageRoomOccupantsBuilder(this, "Back", userData));
  }

  public ArrayList<UUID> getRoomIDs() {
    return this.getHub().getRoomsIds();
  }

  public String isRoomOccupant(UUID roomId, UUID deviceId) {
    if (this.getHub().getRoomContents(roomId).contains(this.getHub().getDevice(deviceId))) {
      return "in Room";
    } else {
      return "not in Room";
    }
  }

  public void makeNewRoom(String roomLabel) {
    try {
      Room fresh = new Room(roomLabel, this.getHub());
    } catch (HubRegistrationException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    this.refresh();
  }

  public void deviceInRoomToggle(UUID roomId, UUID deviceId) {
    Room currRoom = this.getHub().getRoomByRoomID(roomId);
    Device currDevice = this.getHub().getDevice(deviceId);
    if (currRoom.getOccupants().contains(currDevice)) {
      currRoom.removeRoomDevice(currDevice);
    } else {
      currRoom.addRoomDevice(deviceId);
    }
    this.handleRoomsAssignmentClick(roomId);
  }
}
