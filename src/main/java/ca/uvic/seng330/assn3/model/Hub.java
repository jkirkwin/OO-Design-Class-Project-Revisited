package ca.uvic.seng330.assn3.model;

import ca.uvic.seng330.assn3.model.devices.Device;
import java.util.HashMap;
import java.util.NoSuchElementException;
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

  public void register(UserAccount newAccount, AccessLevel accessLevel) {
    try {
      registerNew(newAccount, accessLevel);
    } catch (HubRegistrationException e) {
      // TODO: Logging and Alerts
    }
  }

  private void registerNew(UserAccount newAccount, AccessLevel accessLevel)
      throws HubRegistrationException {
    if (newAccount == null) {
      throw new HubRegistrationException("Nothing passed");
    }
    if (!userAccountRegistry.containsKey(newAccount.getIdentifier())) {
      switch (accessLevel) {
        case ADMIN:
          userAccountRegistry.put(newAccount.getIdentifier(), newAccount);
          break;
        case BASIC:
          userAccountRegistry.put(newAccount.getIdentifier(), newAccount);
          break;
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
      unregisterRetired(killedAccount);
    } catch (HubRegistrationException e) {
      // TODO:  Logging and Alerts
    }
  }

  private void unregisterRetired(UserAccount killedAccount) throws HubRegistrationException {
    switch (killedAccount.getAccessLevel()) {
      case ADMIN:
        userAccountRegistry.remove(killedAccount.getIdentifier());
        break;
      case BASIC:
        userAccountRegistry.remove(killedAccount.getIdentifier());
        break;
    }
  }

  public void log(String msg, UUID id) {
    // TODO
  }

  public void alert(String msg, Device pDevice) throws HubRegistrationException {
    // TODO should be moved to controller
  } 
  
  public UserAccount getUser(String username, String password) throws NoSuchElementException {
    if(!isUser(username)) {
      throw new NoSuchElementException("No user with username " + username);
    }
    for(UserAccount user : userAccountRegistry.values()) {
      if(user.getUsername().equals(username)) {
        if(user.getPassword().equals(password)) {
          return user;
        } else {
          /* No two users are allowed to have the same user name, 
           * so we can safely exit without checking the rest of 
           * the userAccounts for a match
           */
          break;
        }
      }
    }
    throw new NoSuchElementException("Incorrect password");            
  }
  
  public boolean isUser(String username) {
    for(UserAccount user : userAccountRegistry.values()) {      
      if(user.getUsername().equals(username)) {
        return true;
      }
    }
    return false;
  }
  
  public void startup() {
    // TODO
    /*
     * Populate deviceRegistry and userRegistry from storage files
     */
  }
  
  public void shutdown() {
 // TODO
    /*
     * Populate storage files with JSON representations of device/user registries
     */
  }
  
  
}
