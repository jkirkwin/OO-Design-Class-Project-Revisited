package ca.uvic.seng330.assn3.model;

import ca.uvic.seng330.assn3.model.devices.Device;
import ca.uvic.seng330.assn3.model.storage.Storage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

public class Hub {

  private final HashMap<UUID, Device> deviceRegistry;
  private HashMap<UUID, UserAccount> userAccountRegistry;

  public Hub() {
    this.deviceRegistry = new HashMap<UUID, Device>();
    this.userAccountRegistry = new HashMap<UUID, UserAccount>();
  }

  
  /*
   * @pre newDevice != null
   */
  public void register(Device newDevice) throws HubRegistrationException {
    if (!deviceRegistry.containsKey(newDevice.getIdentifier())) {
      deviceRegistry.put(newDevice.getIdentifier(), newDevice);
    } else {
      throw new HubRegistrationException("Device already registered.");
    }
  }
  
  
  /*
   * @pre newAccount != null
   */
  public void register(UserAccount newAccount) {
	    try {
	      registerNew(newAccount);
	    } catch (HubRegistrationException e) {
	      // TODO: Logging and Alerts
	    }
	  }
  
  /*
   * @pre newAccount != null
   */
  private void registerNew(UserAccount newAccount) throws HubRegistrationException {
    assert newAccount != null;
    if (!userAccountRegistry.containsKey(newAccount.getIdentifier())) {
      userAccountRegistry.put(newAccount.getIdentifier(), newAccount);
    } else {
      throw new HubRegistrationException("User already registered");
    }
  }

  public void unregister(UUID murdered) {
	  if(this.deviceRegistry.containsKey(murdered)) {
		  try {
		  unregister(this.deviceRegistry.get(murdered));
		  }catch(HubRegistrationException e) {
			  //TODO: logging & alert
		  }
	  }else if(this.userAccountRegistry.containsKey(murdered)){
		  try {
			  unregister(this.userAccountRegistry.get(murdered));
			  }catch(HubRegistrationException e) {
				//TODO: logging & alert
			  }
	  }	  else {
		  //TODO: alert that nothing corresponds to given UUID
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
    // Or should alert some list of observers in which Controller has registered
  }

  public UserAccount getUser(String username, String password) throws NoSuchElementException {
    if (!isUser(username)) {
      throw new NoSuchElementException("No user with username " + username);
    }
    for (UserAccount user : userAccountRegistry.values()) {
      if (user.getUsername().equals(username)) {
        if (user.getPassword().equals(password)) {
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
    for (UserAccount user : userAccountRegistry.values()) {
      if (user.getUsername().equals(username)) {
        return true;
      }
    }
    return false;
  }

  /*
   * Populate deviceRegistry and userRegistry from storage files
   */
  public void startup() {
    Collection<Device> storedDevices = Storage.getDevices(this);
    Collection<UserAccount> storedAccounts = Storage.getAccounts(this);
    for(Device d : storedDevices) {
      this.deviceRegistry.put(d.getIdentifier(), d);
    }
    for(UserAccount u : storedAccounts) {
      this.userAccountRegistry.put(u.getIdentifier(), u);
    }
  }

  /*
   * Populate storage files with JSON representations of device/user registries
   */
  public void shutdown() {
    Storage.store(this.deviceRegistry.values(), this.userAccountRegistry.values());
  }

  public ArrayList<UUID> getIDList(boolean isDeviceList) {
    ArrayList<UUID> idList = new ArrayList<UUID>();
    if (isDeviceList) {
      for (UUID key : deviceRegistry.keySet()) {
        idList.add(key);
      }
    } else {
      for (UUID key : userAccountRegistry.keySet()) {
        if (!userAccountRegistry.get(key).isAdmin()) {
          idList.add(key);
        }
      }
    }
    return idList;
  }

  public String getLabel(UUID uuid) {
    return deviceRegistry.get(uuid).getLabel();
  }

  public List<UUID> getBlackList(UserAccount user) {
    return userAccountRegistry.get(user.getIdentifier()).getBlackList();
  }

  public Device getDevice(UUID uuid) {
    return deviceRegistry.get(uuid);
  }
}
