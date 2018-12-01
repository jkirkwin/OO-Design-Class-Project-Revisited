package ca.uvic.seng330.assn3.view;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import ca.uvic.seng330.assn3.model.AccessLevel;
import ca.uvic.seng330.assn3.model.UserAccount;
import ca.uvic.seng330.assn3.model.devices.Camera;
import ca.uvic.seng330.assn3.model.devices.Thermostat;
import org.junit.Before;
import org.junit.Test;

public class TestManageUsers extends IOTApplicationGUITest {

  private UserAccount user1;
  private Camera c;
  private Thermostat t;

  @Before
  public void setup() {
    user1 = new UserAccount(hub, AccessLevel.BASIC, "user1", "pw");
    c = new Camera("cam", hub);
    t = new Thermostat("therm", hub);
  }

  @Test
  public void testDeleteUser() {
    GUITestUtilities.goToManageUsers(this);
    assertTrue(hub.isRegisteredUserAccount(user1.getIdentifier()));
    clickOn("#delete_" + user1.getUsername());
    clickOn("OK");
    assertFalse(hub.isRegisteredUserAccount(user1.getIdentifier()));
  }

  @Test
  public void testModifyVisibility() {
    user1.blackList(c.getIdentifier());
    assertTrue(hub.isRegisteredUserAccount(user1.getIdentifier()));
    assertTrue(hub.isRegisteredDevice(c.getIdentifier()));
    assertTrue(hub.isRegisteredDevice(t.getIdentifier()));

    GUITestUtilities.goToManageUsers(this);
    clickOn(user1.getUsername());
    assertTrue(user1.isBlacklisted(c.getIdentifier()));
    assertFalse(user1.isBlacklisted(t.getIdentifier()));

    clickOn("#" + t.getLabel());
    clickOn("#" + c.getLabel());
    assertFalse(user1.isBlacklisted(c.getIdentifier()));
    assertTrue(user1.isBlacklisted(t.getIdentifier()));
  }
}
