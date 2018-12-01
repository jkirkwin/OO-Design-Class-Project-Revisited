package ca.uvic.seng330.assn3.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import ca.uvic.seng330.assn3.IOTUnitTest;
import ca.uvic.seng330.assn3.model.devices.Camera;
import ca.uvic.seng330.assn3.model.devices.Device;
import ca.uvic.seng330.assn3.model.devices.Lightbulb;
import ca.uvic.seng330.assn3.model.devices.SmartPlug;
import ca.uvic.seng330.assn3.model.devices.Status;
import ca.uvic.seng330.assn3.model.devices.Thermostat;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
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
  public void testMassStatusChange() {
    for (Device d : devices) {
      assertTrue(d.getStatus().equals(Status.ON));
    }
    devices.get(0).setStatus(Status.OFF);
    devices.get(1).setStatus(Status.ERROR);
    h.massSetStatus(Status.OFF);
    for (Device d : devices) {
      assertTrue(d.getStatus().equals(Status.OFF));
    }
  }

  @Test
  public void testUUIDUnregistration() {
    // Device ID
    for (Device d : devices) {
      try {
        h.unregister(d.getIdentifier());
      } catch (HubRegistrationException e) {
        fail("device deregistration failed");
      }
      assertFalse(h.isRegisteredDevice(d.getIdentifier()));
    }

    // UserAccount ID
    UserAccount a = new UserAccount(h, AccessLevel.ADMIN, "username", "password");
    assertTrue(h.isRegisteredUserAccount(a.getIdentifier()));
    try {
      h.unregister(a.getIdentifier());
    } catch (HubRegistrationException e) {
      fail("account deregistration failed");
    }
    assertFalse(h.isRegisteredUserAccount(a.getIdentifier()));

    // Room ID
    Room r = null;
    try {
      r = new Room("room label", h);
    } catch (HubRegistrationException e1) {
      fail("room registration failed");
    }
    assertTrue(h.isRegisteredRoom(r.getIdentifier()));
    try {
      h.unregister(r.getIdentifier());
    } catch (HubRegistrationException e) {
      fail("room deregistration failed");
    }
    assertFalse(h.isRegisteredRoom(a.getIdentifier()));

    // Invalid ID
    try {
      h.unregister(UUID.randomUUID());
      fail("no exception thrown");
    } catch (HubRegistrationException e) {
    }
  }

  @Test
  public void testShutdown() {
    // TODO
    assertTrue(false);
  }

  @Test
  public void testStartup() {
    // TODO
    assertTrue(false);
  }

  @Test
  public void testUserAccountRegistration() {
    Hub dummyHub = new Hub();
    UserAccount[] accounts = new UserAccount[5];
    for (int i = 0; i < accounts.length; i++) {
      AccessLevel lvl = i % 2 == 0 ? AccessLevel.ADMIN : AccessLevel.BASIC;
      accounts[i] = new UserAccount(dummyHub, lvl, "username" + i, "password" + i);
    }
    for (UserAccount a : accounts) {
      assertTrue(dummyHub.isRegisteredUserAccount(a.getIdentifier()));
      try {
        dummyHub.unregister(a);
      } catch (HubRegistrationException e1) {
        fail("Exception thrown");
      }
      assertFalse(dummyHub.isRegisteredUserAccount(a.getIdentifier()));
      try {
        h.register(a);
      } catch (HubRegistrationException e) {
        fail("unable to register room");
      }
      assertTrue(h.isRegisteredUserAccount(a.getIdentifier()));
      assertTrue(h.getUser(a.getUsername(), a.getPassword()) == a);
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
      assertTrue(h.isLabelUsed(r.getLabel()));
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
      assertTrue(h.getRoomByRoomID(r.getIdentifier()).equals(r));
      for (Device d : devices) {
        d.setRoom(r);
        assertTrue(h.getRoomByDeviceID(d.getIdentifier()).equals(r));
      }
    } catch (HubRegistrationException e) {
      fail("failed to register");
    }
  }

  @Test
  public void testGetLabel() {
    UserAccount a = new UserAccount(h, AccessLevel.BASIC, "username", "password");
    Room r = null;
    try {
      r = new Room("label", h);
    } catch (HubRegistrationException e) {
      fail();
    }
    assertTrue(devices.get(0).getLabel().equals(h.getLabel(devices.get(0).getIdentifier())));
    assertTrue(r.getLabel().equals(h.getLabel(r.getIdentifier())));
    assertTrue(a.getUsername().equals(h.getLabel(a.getIdentifier())));

    try {
      h.getLabel(UUID.randomUUID());
      fail();
    } catch (NoSuchElementException e) {
    }
  }

  @Test
  public void testDeviceAdministration() {
    // TODO
    assertTrue(false);
  }
}
