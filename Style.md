# A style planning document to record the development of our design decisions and ideas

## Planning & Concepts

### User Management 
User interface, implemented by a BasicUser class, AdminUser extends BasicUser
Each user object should store the User's username, password, UUID?, and a list of all the devices (or their UUID's) that the user has access to.

### Devices
Devices should be designated PUBLIC or ADMIN_ONLY. This designation dictates the behaviour of the model when new users are added. Newly added users
are given access to PUBLIC devices automatically but no ADMIN devices. This can be modified by AdminUsers in the admin view

## Testing

Unit test ideas:
* Test that view has no imports from model
* Test that model has no imports from controller or from view

## Model 

### Startup and Shutdown
For now, we will spin up a "blank" model each time - that is, no devices will be automatically associated and only two users (one admin and one regular user) will be available for use. Once the code base has been developed further, we will save the state of the model at shutdown() into text files containing JSON representations of the devices, users, and any other stateful objects. These files will be parsed during startup() to re-create all these objects as they were.

See https://stackoverflow.com/questions/26619566/javafx-stage-close-handler for info on how to have safety code that executes when the application terminates. This should allow us to ensure state is saved when the user quits the app. 

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

## Controller

### Active User
* We need to track which user is active -> simply add a User activeUser field in controller. That should give us all the info we need.

### ControllerObjects
We could have a DeviceController Class with specific subtypes for each type of device. Within the controller we could have one DeviceController object for each device present in the model. This was suggested in class by a student and Neil seemed to be on board.

# Questions

## Design

### Questions
* Who can make devices?
* Is the first User automatically an Admin?
* How is a User made an Admin?
* Can Admin Kill Users?
* Where does error checking happen?
* Input checking?
* Logging HOW!?
* scrolling?
* Devices extend Cloneable?
* When creating user accounts, does there need to be a verification step if the new account is to be an Admin?
* How should we parameterize the dynamic creation of scenes? Controll flow goes: controller calls setView(viewType); withing setView(), the client creates and populates a new scene. See FXClient.java for context and extra notes.

### Logistic Questions
* JAVAFX vs Spring?
* Gradle HOW?? -> Go to Neil's Office Hours
* Is Travis working 100?
* Logging where?
* CheckStyle working?