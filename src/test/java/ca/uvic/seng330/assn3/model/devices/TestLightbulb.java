package ca.uvic.seng330.assn3.model.devices;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import ca.uvic.seng330.assn3.IOTUnitTest;
import ca.uvic.seng330.assn3.model.Hub;
import org.junit.Before;
import org.junit.Test;

public class TestLightbulb extends IOTUnitTest {

  private Hub h;

  @Before
  public void setup() {
    h = new Hub();
  }

  @Test
  public void testInitialState() {
    Lightbulb a = new Lightbulb(h);
    Lightbulb b = new Lightbulb("a label", h);

    assertTrue(a.getHub() == h);
    assertTrue(a.getStatus().equals(Status.ON));
    assertTrue(a.isOn());
    assertTrue(a.getLabel().equals("Default Label"));
    assertTrue(b.getHub() == h);
    assertTrue(b.getStatus().equals(Status.ON));
    assertTrue(b.getLabel().equals("a label"));
    assertTrue(b.isOn());
  }

  @Test
  public void testToggle() {
    Lightbulb a = new Lightbulb(h);
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
