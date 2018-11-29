package ca.uvic.seng330.assn3.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import ca.uvic.seng330.assn3.IOTUnitTest;
import ca.uvic.seng330.assn3.model.devices.Camera;
import ca.uvic.seng330.assn3.model.devices.Device;
import ca.uvic.seng330.assn3.model.devices.Lightbulb;
import ca.uvic.seng330.assn3.model.devices.SmartPlug;
import ca.uvic.seng330.assn3.model.devices.Thermostat;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class TestHub extends IOTUnitTest {

  // TODO
  /*
   * Test Room creation, deletion, switching, etc with various device
   * Test UserAccount creation, deletion, switching, etc with various device
   * Test Device creation, deletion, switching, etc with various device
   * Test startup and shutdown
   */

  private Hub h;
  private List<Device> devices;

  @Before
  public void setup() {
    h = new Hub();
    devices = new ArrayList<Device>();
    for (int i = 0; i < 4; i++) {
      devices.add(new Camera("cam" + i, h));
      devices.add(new Lightbulb("lb" + i, h));
      devices.add(new SmartPlug("plug" + i, h));
      devices.add(new Thermostat("thermo" + i, h));
    }
  }

  @Test
  public void testRoomRegistration() {
    Hub dummyHub = new Hub();
    Room[] rooms = new Room[5];
    try {
      for (int i = 0; i < rooms.length; i++) {
        rooms[i] = new Room("room" + i, dummyHub);
      }
    } catch (HubRegistrationException e) {
      e.printStackTrace();
      fail("Registration failed");
    }
    for (Room r : rooms) {
      assertTrue(dummyHub.isRegisteredRoom(r.getIdentifier()));
      try {
        dummyHub.unregister(r);
      } catch (HubRegistrationException e1) {
        fail("Exception thrown");
      }
      assertFalse(dummyHub.isRegisteredRoom(r.getIdentifier()));
      try {
        h.register(r);
      } catch (HubRegistrationException e) {
        fail("unable to register room");
      }
      assertTrue(h.isRegisteredRoom(r.getIdentifier()));
    }
  }

  @Test
  public void testGetRoomContent() {
    Room[] rooms = new Room[3];
    try {
      for (int i = 0; i < rooms.length; i++) {
        rooms[i] = new Room("room" + i, h);
      }
    } catch (HubRegistrationException e) {
      fail("Unable to register room");
      e.printStackTrace();
    }
    for (Room r : rooms) {
      assertTrue(h.getRoomContents(r).isEmpty());
      assertTrue(h.getRoomContents(r.getIdentifier()).isEmpty());
      for (Device d : devices) {
        r.addRoomDevice(d.getIdentifier());
      }      
    }
    devices.sort((Device d1, Device d2) -> (d1.getIdentifier().compareTo(d2.getIdentifier())));
    List<Device> result = h.getRoomContents(rooms[0]);
    result.sort((Device d1, Device d2) -> (d1.getIdentifier().compareTo(d2.getIdentifier())));
    assertTrue(devices.equals(result));
  }

  @Test
  public void TestGetRoom() {
    try {
      Room r = new Room("room label", h);
      assertTrue(h.getRoomByID(r.getIdentifier()).equals(r));
      for (Device d : devices) {
        d.setRoom(r);
        assertTrue(h.getRoomByDeviceID(d.getIdentifier()).equals(r));
      }
    } catch (HubRegistrationException e) {
      fail("failed to register");
    }
  }

  @Test
  public void testAccountAdministration() {
    // TODO
    assertTrue(false);
  }

  @Test
  public void testDeviceAdministration() {
    // TODO
    assertTrue(false);
  }
}
