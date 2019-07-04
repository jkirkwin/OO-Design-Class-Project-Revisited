# A style planning document to record the development of our design decisions and ideas

## Testing

Unit test ideas:
* Test that view has no imports from model
* Test that model has no imports from controller or from view

## Controller

### Active User
* We need to track which user is active -> simply add a User activeUser field in controller. That should give us all the info we need.

### ControllerObjects
We could have a DeviceController Class with specific subtypes for each type of device. Within the controller we could have one DeviceController object for each device present in the model. This was suggested in class by a student and Neil seemed to be on board.


### Devices
Devices should be designated PUBLIC or ADMIN_ONLY. This designation dictates the behaviour of the model when new users are added. Newly added users
are given access to PUBLIC devices automatically but no ADMIN devices. This can be modified by AdminUsers in the admin view

## Model 

### Startup and Shutdown
Startup is called upon Controller instantiation
Shutdown is called on program exit (close button, X button, and force quit)
These methods call the apropriate functions in model.storage.Storage.java to read previously generated JSON files and translate the contents to Java Device and UserAccount objects or convert all Devices and UserAccounts to their JSON representations and save these as appropriate.

## View
LogIn screen prompts for username/password and either accesses or creates a user.

If BasicUser:
* shows a Hub view of devices visible to the user.

If AdminUser:
* shows a Hub view of all devices
* ability to make new devices
* ability to edit device visibility
* ability to kill devices & suspend users

From Hub view can click into devices to see important stats about them and allow for toggling/editing states.

HubBuilder should likely make use of the template pattern. The 'hook' method can be used to fill certain designated portions of the scene.

Similarly, device views can make use of the template pattern where the hook methods fill in all actions specific to the device type.

### Event handling
For now, all events will be handled by the update() function in controller. We will refactor this later so that different buttons are bound to different, more modular handlers. To do this handler assignment, we should use the command pattern. See https://stackoverflow.com/questions/2186931/java-pass-method-as-parameter for deets.

### LogIn
Upon Startup User is presented with LogIn screen. LogIn prompts for Username/Password and has a 'make new Account' option. If Admin User is created prompt for Admin Credentials. Default Admin is Shipped with Application.

### BasicHub
Employs HubBuilder.

After logging in the User is presented with a list of all devices they have access to.

### DeviceView
When a Device is selected from HubView it brings up a DeviceView that has:
* 'Back' - returns to Hub View
* 'ON/OFF' - changes Status of device
* 'Toggle Action' - simple action specific to device
* 'Extra Actions' - device specific action that requires input

DeviceView also has a State Screen for displaying all pertinent data for specific device.
One way to implement this: 

### AdminHub
Employs HubBuilder.

Admin Users are also presented with a dashboard in addition to the BasicHub.
Dashboard allows User to:
* 'Add Device' - goes to DeviceAdder
* 'Edit User Visibility' - Goes to UserEditor
* '-Kill-/Murder' - Goes to KillScreen
Should be accessible (For users logged in with admin credentials) by pressing a button while in the BasicHub view.

### DeviceAdder
After selecting 'Add Device' from AdminHub, User is presented with a list of DeviceTypes known to the System, and a set of fields for constructing said devices. Error checks any user input and has a back button to return to AdminHub.

### UserEditor
Employs HubBuilder.

After selecting 'Edit User Visibility' from AdminHub, User is presented with a list of UsersAccounts. Upon selecting a UserAccount User is Presented with a list of Devices with 'Whitelist' and 'Blacklist' options. UserAccounts store a blacklist of devices not to load for them.

### KillScreen
Employs HubBuilder
After selecting '-Kill-/Murder' from AdminHub, User is presented with a list of UsersAccounts or Devices based on which option was selected. Anything selected will prompt for comfirmation, then destroy the device.

# Questions

## Design

### Questions
* Where does error checking happen?
* Input checking?
* Logging HOW!?
* Devices extend Cloneable?

### Logistic Questions
* Is Travis working 100?
* Logging where?
