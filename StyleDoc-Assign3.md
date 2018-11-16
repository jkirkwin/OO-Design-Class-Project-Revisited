# A style document describing the development of our design decisions



## Model

	The Model has been largely inherited from assignment two. Using Hub as our Primary Entry Point, we can access all Devices and UserAccounts in our system. The Model is completely Independant and does not rely on ANYTHING outside itself.

### Hub

	The Hub is the core (((((Mediator??)))))  of our model. It is responsible for registering and unregistering for all devices and UserAccounts in the system, and for holding Hashmaps of everything currently in the system. It can also access almost any info about the objects it holds. This allows it to stay as the point of contact for Controller.
	While the aspiration is for the Hub to be the only thing able to access that which it holds, it became unwieldly to continue that implementation. In an effort to avoid having a GODFUNCTION of a hub, we decided to implement getUser() and getDevice() so we could be more flexible in Controller.

### UserAccount

	UserAccount is responsible for knowing it's own accessLevel, which Hub it belongs to, it's username and password for verification, and also holds the blacklist of devices it may not see.
	We decided to have the UserAccounts store their own accessLevel and Device Blacklist so as to limit complexity of Hub. 

### Device

	Device is an Abstract Class that is extended by all devices in the system. It holds the Hub it belongs to, its Label, Status and most importantly its UUID.
	We use the UUID to do all important tasks in the system. Every time we want to get or change any device, the UUID is always part of the call. This assures us we can find the specific device we need and that we can't get our deviceTypes confused. 

#### Camera

	Camera extends Device and is responsible for keeping track of if it isRecording, the diskSize for memory storage and if it is full or not.

#### Thermostat
	
	Thermostat extends Device and is responsible for holding the Temperature, and making sure any new Temperatures are within acceptable limits. It can also convert Temperatures from one degreeType to another.

#### Lightbulb & SmartPlug

	Lightbulb & SmartPlug extend Device and are responsible for knowing if they are active or not.

## Controller

	Controller is the middle man for View and Model. It currently has many responsibilities but we are planning to break it down into sub-controllers to make it not be a GODFUNCTION. These plans are described below.
	At this time the Controller holds Model's Hub, and View's Client. It also knows who the ActiveUser is and has a Stack of ViewTypes so as to maintain an order of how the current screen was gotten to.
	We started with the controller just being a bridge from View to Model. But as we gave View more and more functionality, it necessitated Controller to grow. We have made inroads towards splitting the whole thing up by starting to segment the file into chunks described below.

### Handlers

	For every button in our application we need a method to give it functionality. By and large these live in Controller. We keep the handlers in Controller so as to limit control flow complexity in View, and with an eye towards being able to refactor this into using the Observable pattern.
	Putting our Handlers here also allows us to keep View and Model separate. As most of our buttons in View require us to access the Model, keeping large parts of the logic here lets us keep coupling loose.

### Public Utility Functions

	Not all actions in View require us to access Model. Some only require the manipulation or accessing of things in controller: handleBackClick() or refresh() are examples of this.
	Likewise, some functions which do access Model are used by most every ViewBuilder file. These are kept here as well for easy of access and to keep complexity down.

### Specific Use Functions

	Other functions however are very specific. these are things like the Thermostat specific getThermostatTempType() which are only used in a single View class. They are currently kept in Controller for ease of development, but have been segmented off with an eye for refactoring.

### FindBuilder

	The Heart of Controller is FindBuilder(). This is used to maintain the View Stack for going backwards and also for creating the right screen going forward.
	We decided to go with Dynamically generating each screen as we come to it so that:
	* a) we don't need to save state anywhere, therein lowering complexity.
	* b) ensuring that anything displayed at anytime will be accurate to the Model's current State.
	FindBuilder() was echoed with deviceViewSwitch() to keep it clean code and to find the correct view to build for each Device Config Screen.

### Plans for the Refactor

	


## View

### Client

### SceneBuilder

#### LoginView

#### HubView

##### Basic

##### Admin

### DeviceSceneBuilder

#### CameraScene

#### ThermostatScene

#### Lightbulb & SmartPlug

























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
* Who can make devices?
* Can Admin Kill Users?
* Where does error checking happen?
* Input checking?
* Logging HOW!?
* Devices extend Cloneable?
* How should we parameterize the dynamic creation of scenes? Controll flow goes: controller calls setView(viewType); withing setView(), the client creates and populates a new scene. See FXClient.java for context and extra notes.

### Logistic Questions
* Gradle HOW?? -> Go to Neil's Office Hours
* Is Travis working 100?
* Logging where?
* CheckStyle working?
