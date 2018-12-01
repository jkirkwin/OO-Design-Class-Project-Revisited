package ca.uvic.seng330.assn3.controller;

import javafx.scene.control.Alert.AlertType;

public class ManageDevicesController extends Controller {

  public void handleCreateDeviceClick() {
    client.setView(findBuilder(ViewType.CREATE_DEVICE));
  }

  /*
   * @pre newDevice != null
   */
  public void handleNewDeviceClick(
      DeviceType newDevice, boolean startingState, String customLabel) {
    assert newDevice != null;
    assert customLabel != null;

    String baseLabel = customLabel.equals("") ? newDevice.getEnglishName() : customLabel;
    String uniqueLabel = getUniqueDeviceLabel(baseLabel);

    hub.makeNewDevice(newDevice, startingState, uniqueLabel);

    client.alertUser(
        AlertType.INFORMATION,
        "Device Added",
        "New " + newDevice.toString(),
        newDevice.toString() + " created with label: \"" + uniqueLabel + "\"");

    refresh();
  }

  
}
