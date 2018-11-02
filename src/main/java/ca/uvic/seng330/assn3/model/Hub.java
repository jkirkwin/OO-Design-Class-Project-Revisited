package ca.uvic.seng330.assn3.model;

import ca.uvic.seng330.assn3.model.devices.Device;
import java.util.HashMap;
import java.util.UUID;

public class Hub {
  private final HashMap<UUID, Device> deviceRegistry;

  public Hub() {
    this.deviceRegistry = new HashMap<UUID, Device>();
  }

  public void register(Device aDevice) throws HubRegistrationException {}

  public void unregister(Device aDevice) throws HubRegistrationException {}

  public void log(String msg, UUID id) {
    // TODO
  }

  public void alert(String msg, Device pDevice) throws HubRegistrationException {}
}
