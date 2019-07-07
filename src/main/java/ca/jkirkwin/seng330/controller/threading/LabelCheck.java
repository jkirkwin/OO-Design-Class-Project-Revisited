package ca.jkirkwin.seng330.controller.threading;

import ca.jkirkwin.seng330.controller.Controller;
import ca.jkirkwin.seng330.logging.Logging;
import ca.jkirkwin.seng330.model.Hub;
import java.util.NoSuchElementException;
import java.util.UUID;
import org.slf4j.event.Level;

public class LabelCheck extends PropertyCheck {

  public LabelCheck(Controller controller, UUID id, Object[] returnWrapper) {
    super(controller, id, returnWrapper);
  }

  @Override
  public void run() {
    // Query the model to get the label of the device, store that value in wrapper position 0;
    Hub hub = getController().getHub();
    try {
      String label = hub.getLabel(getId());
      assert label != null;
      getReturnWrapper()[0] = label;
    } catch (NoSuchElementException e) {
      Logging.log("failed to retrieve label. no such id found in hub.", Level.ERROR);
    }
  }
}
