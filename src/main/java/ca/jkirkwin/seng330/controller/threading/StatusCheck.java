package ca.jkirkwin.seng330.controller.threading;

import ca.jkirkwin.seng330.controller.Controller;
import ca.jkirkwin.seng330.logging.Logging;
import ca.jkirkwin.seng330.model.Hub;
import ca.jkirkwin.seng330.model.devices.Device;
import java.util.UUID;
import org.slf4j.event.Level;

public class StatusCheck extends PropertyCheck {

  public StatusCheck(Controller controller, UUID id, Object[] returnWrapper) {
    super(controller, id, returnWrapper);
  }

  @Override
  public void run() {
    // Query the model to get the status of the device, store that value in wrapper position 0;
    Hub hub = getController().getHub();
    Device d = hub.getDevice(getId());
    if (d == null) {
      Logging.log("failed to retrieve device status. no such uuid in hub.", Level.ERROR);
    } else {
      getReturnWrapper()[0] = d.getStatus();
    }
  }
}
