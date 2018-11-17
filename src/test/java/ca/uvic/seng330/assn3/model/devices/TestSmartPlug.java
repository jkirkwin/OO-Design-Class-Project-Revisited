package ca.uvic.seng330.assn3.model.devices;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import ca.uvic.seng330.assn3.model.Hub;
import org.junit.Before;
import org.junit.Test;

public class TestSmartPlug {

  private Hub h;

  @Before
  public void setup() {
    h = new Hub();
  }

  @Test
  public void testInitialState() {
    SmartPlug a = new SmartPlug(h);
    SmartPlug b = new SmartPlug("a label", h);

    assertTrue(a.getHub() == h);
    assertTrue(a.getStatus().equals(Status.ON));
    assertTrue(a.isOn());
    assertTrue(a.getLabel().equals("Default Label"));
    assertTrue(b.getHub() == h);
    assertTrue(b.getStatus().equals(Status.ON));
    assertTrue(b.isOn());
    assertTrue(b.getLabel().equals("a label"));
  }

  @Test
  public void testToggle() {
    SmartPlug a = new SmartPlug(h);
    assertTrue(a.getStatus().equals(Status.ON));
    assertTrue(a.isOn());
    a.toggle();
    assertTrue(a.getStatus().equals(Status.OFF));
    assertFalse(a.isOn());
    a.toggle();
    assertTrue(a.getStatus().equals(Status.ON));
    assertTrue(a.isOn());
  }
}
