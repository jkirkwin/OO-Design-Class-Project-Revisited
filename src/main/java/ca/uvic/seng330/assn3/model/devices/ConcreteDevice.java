package ca.uvic.seng330.assn3.model.devices;

import ca.uvic.seng330.assn2.part1.HubRegistrationException;
import ca.uvic.seng330.assn2.part1.Level;
import ca.uvic.seng330.assn2.part1.Mediator;
import java.util.UUID;

public class ConcreteDevice implements Device {

  private final UUID id;
  private Status status;
  private final Mediator aMediator;

  /**
   * @pre pMediator != null
   * @param pMediator
   */
  public ConcreteDevice(Mediator pMediator) {
    assert pMediator != null;
    this.id = UUID.randomUUID();
    this.status = Status.NORMAL;
    this.aMediator = pMediator;
    try {
      aMediator.register(this);
    } catch (HubRegistrationException e) {
      aMediator.log("Registration Failed : " + e.getMessage(), Level.ERROR, getIdentifier());
    }
  }

  public ConcreteDevice clone() {
    try {
      return (ConcreteDevice) super.clone();
    } catch (CloneNotSupportedException e) {
      aMediator.log("Device Creation Failed : " + e.getMessage(), Level.ERROR, getIdentifier());
      return null;
    }
  }

  public Status getStatus() {
    return this.status;
  }

  /** @pre aStatus != null */
  public void setStatus(Status aStatus) {
    assert aStatus != null;
    this.status = aStatus;
  }

  public UUID getIdentifier() {
    return this.id; // safe since UUID is immutable
  }

  public Mediator getMediator() {
    return this.aMediator;
  }
}
