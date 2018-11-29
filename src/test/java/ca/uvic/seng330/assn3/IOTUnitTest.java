package ca.uvic.seng330.assn3;

import ca.uvic.seng330.assn3.logging.Logging;
import org.junit.Before;
import org.slf4j.event.Level;

public class IOTUnitTest {

  @Before
  public void init() {
    Logging.init();
    Logging.log("RUNNING TEST: " + this.getClass().getSimpleName(), Level.DEBUG);
  }
}
