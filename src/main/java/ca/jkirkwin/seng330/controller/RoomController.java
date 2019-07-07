package ca.jkirkwin.seng330.controller;

import ca.jkirkwin.seng330.logging.Logging;
import ca.jkirkwin.seng330.model.HubRegistrationException;
import ca.jkirkwin.seng330.model.Room;
import ca.jkirkwin.seng330.model.devices.Device;
import ca.jkirkwin.seng330.view.scenebuilders.ManageRoomOccupantsBuilder;
import java.util.ArrayList;
import java.util.UUID;
import org.slf4j.event.Level;

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
    assert roomLabel != null;
    String uniqueLabel = getUniqueDeviceLabel(roomLabel);
    try {
      Room fresh = new Room(uniqueLabel, this.getHub());
    } catch (HubRegistrationException e) {
      Logging.log("Room " + roomLabel + " wasn't registered to Hub.", Level.WARN);
    }
    Logging.log("Room " + roomLabel + " has been registered to Hub.", Level.INFO);
    this.refresh();
  }

  public void deviceInRoomToggle(UUID roomId, UUID deviceId) {
    Room currRoom = this.getHub().getRoomByRoomID(roomId);
    Device currDevice = this.getHub().getDevice(deviceId);
    if (currRoom.getOccupants().contains(currDevice)) {
      currRoom.removeRoomDevice(currDevice);
      Logging.log(
          "Device " + currRoom.getLabel() + " removed from Room " + currDevice.getLabel(),
          Level.INFO);
    } else {
      currRoom.addRoomDevice(deviceId);
      Logging.log(
          "Device " + currRoom.getLabel() + " added to Room " + currDevice.getLabel(), Level.INFO);
    }
    this.handleRoomsAssignmentClick(roomId);
  }
}
