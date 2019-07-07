package ca.jkirkwin.seng330.view;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import ca.jkirkwin.seng330.model.HubRegistrationException;
import ca.jkirkwin.seng330.model.Room;
import ca.jkirkwin.seng330.model.devices.Device;
import ca.jkirkwin.seng330.model.devices.Lightbulb;
import ca.jkirkwin.seng330.model.devices.SmartPlug;
import java.util.UUID;
import org.junit.Test;

public class TestRoomManagement extends IOTApplicationGUITest {

  @Test
  public void testCreateRoom() {
    String label = "label123";
    GUITestUtilities.goToManageRooms(this);
    clickOn("#room_label");
    write(label);
    clickOn("#create_room");

    assertTrue(this.hub.isLabelUsed(label));
    UUID id = this.hub.getFirstRoomID(label);
    assertTrue(this.hub.isRegisteredRoom(id));
  }

  @Test
  public void testDeleteRoom() {
    String label = "room90210";
    Room r = null;
    try {
      r = new Room(label, this.hub);
    } catch (HubRegistrationException e) {
      fail();
    }
    GUITestUtilities.goToManageRooms(this);
    assertTrue(this.hub.isRegisteredRoom(r.getIdentifier()));
    clickOn("#delete_" + label);
    assertFalse(this.hub.isRegisteredRoom(r.getIdentifier()));
    clickOn("OK");
  }

  @Test
  public void testDeviceAllocation() {
    Room r = null;
    try {
      r = new Room("room888", this.hub);
    } catch (HubRegistrationException e) {
      fail();
    }
    Device d1 = new Lightbulb("d1", this.hub);
    Device d2 = new SmartPlug("d2", this.hub);
    r.addRoomDevice(d1.getIdentifier());
    GUITestUtilities.goToManageRooms(this);
    clickOn("#" + r.getLabel());
    assertTrue(r.isOccupant(d1.getIdentifier()));
    assertFalse(r.isOccupant(d2.getIdentifier()));

    // remove d1, add d2
    clickOn("#" + d1.getLabel());
    clickOn("#" + d2.getLabel());
    assertFalse(r.isOccupant(d1.getIdentifier()));
    assertTrue(r.isOccupant(d2.getIdentifier()));
  }
}
