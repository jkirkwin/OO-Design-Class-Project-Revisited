package ca.uvic.seng330.assn3.model;

import java.util.UUID;

public class UserAccount {

  private AccessLevel accessLevel;
  private Hub hub;
  private final UUID id = UUID.randomUUID();

  public UserAccount(Hub h, AccessLevel isAdmin) {
    this.hub = h;
    this.accessLevel = isAdmin;
  }

  public UUID getIdentifier() {
    return id;
  }

  public boolean isAdmin() {
    return accessLevel == AccessLevel.ADMIN;
  }

  public AccessLevel getAccessLevel() {
    return accessLevel;
  }
}
