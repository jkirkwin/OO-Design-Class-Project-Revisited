package ca.jkirkwin.seng330;

import ca.jkirkwin.seng330.logging.Logging;
import org.junit.Before;
import org.slf4j.event.Level;

public class IOTUnitTest {

  @Before
  public void init() {
    Logging.init();
    Logging.log("RUNNING TEST: " + this.getClass().getSimpleName(), Level.DEBUG);
  }
}
