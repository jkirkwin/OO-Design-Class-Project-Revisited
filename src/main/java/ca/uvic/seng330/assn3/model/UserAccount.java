package ca.uvic.seng330.assn3.model;

import java.util.UUID;

public class UserAccount {

  private boolean admin;
  private Hub hub;
  private final UUID id = UUID.randomUUID();

  public UserAccount(Hub h, boolean isAdmin) {
    this.hub = h;
    this.admin = isAdmin;
  }

  public UUID getIdentifier() {
    return id;
  }

  public boolean isAdmin() {
    return admin;
  }
}
