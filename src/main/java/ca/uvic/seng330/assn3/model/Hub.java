package ca.uvic.seng330.assn3.model;

import ca.uvic.seng330.assn3.model.devices.Device;
import java.util.HashMap;
import java.util.UUID;

public class Hub {
  private final HashMap<UUID, Device> deviceRegistry;
  private HashMap<UUID, UserAccount> userAccountRegistry;

  public Hub() {
    this.deviceRegistry = new HashMap<UUID, Device>();
    this.userAccountRegistry = new HashMap<UUID, UserAccount>();
  }

  public void register(Device newDevice) throws HubRegistrationException {
    if (!deviceRegistry.containsKey(newDevice.getIdentifier())) {
      deviceRegistry.put(newDevice.getIdentifier(), newDevice);
    } else {
      throw new HubRegistrationException("Device already registered.");
    }
  }

  public void unregister(Device retiredDevice) throws HubRegistrationException {
    if (retiredDevice == null || deviceRegistry.isEmpty()) {
      throw new HubRegistrationException("No device passed.");
    }
    if (deviceRegistry.containsKey(retiredDevice.getIdentifier())) {
      deviceRegistry.remove(retiredDevice.getIdentifier());
    } else {
      throw new HubRegistrationException("Device does not exist.");
    }
  }

  public void register(UserAccount newAccount, boolean isAdmin) {
    try {
      registerNew(newAccount, isAdmin);
    } catch (HubRegistrationException e) {
      // TODO: Logging and Alerts
    }
  }

  private void registerNew(UserAccount newAdd, boolean isAdmin) throws HubRegistrationException {
    if (newAdd == null) {
      throw new HubRegistrationException("Nothing passed");
    }
    if (!userAccountRegistry.containsKey(newAdd.getIdentifier())) {
      if (isAdmin) {
        userAccountRegistry.put(newAdd.getIdentifier(), newAdd);
      } else {
        userAccountRegistry.put(newAdd.getIdentifier(), newAdd);
      }
    }
  }

  public void unregister(UserAccount killedAccount) throws HubRegistrationException {
    if (killedAccount == null
        || userAccountRegistry.isEmpty()
        || !userAccountRegistry.containsKey(killedAccount.getIdentifier())) {
      throw new HubRegistrationException("Account does not exist.");
    }
    try {
      unregisterRetired(killedAccount, killedAccount.isAdmin());
    } catch (HubRegistrationException e) {
      // TODO:  Logging and Alerts
    }
  }

  private void unregisterRetired(UserAccount killedAccount, boolean isAdmin)
      throws HubRegistrationException {
    if (isAdmin) {
      // TODO: ensure killedAccount is not the Default Admin Account
      userAccountRegistry.remove(killedAccount.getIdentifier());
    } else {
      userAccountRegistry.remove(killedAccount.getIdentifier());
    }
  }

  public void log(String msg, UUID id) {
    // TODO
  }

  public void alert(String msg, Device pDevice)
      throws HubRegistrationException {} // should be moved to controller
}
