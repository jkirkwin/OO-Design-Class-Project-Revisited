package ca.uvic.seng330.assn3.view;

import ca.uvic.seng330.assn3.controller.Controller;

public abstract class Client {

  private ViewType viewType;
  private Controller controller;
  
  /*
   * @pre c != null
   * @pre v != null
   */
  public Client(Controller c, ViewType v) {
    assert c != null;
    assert v != null;
    this.controller = c;
    this.setViewType(v);
  }

  public ViewType getViewType() {
    return viewType;
  }

  /*
   * @pre viewType != null
   */
  public void setViewType(ViewType viewType) {
    assert viewType != null;
    this.viewType = viewType;
  }
}
