# Changes Since A3 Submission

### Controller Refactor
* Broke controller up into many different sub-controller, each extending Controller. Package structure now mimics that in view (i.e. there is a loose bijection between Controllers and SceneBuilders). Extensibility greatly improved.

### Camera Video Data
* added a WebView to camera showing a YouTube video when recording

### Room Associations
* Create Room class 
* Added a room field to Device. This is an optional feature that allows the user to group devices together. If there is no associated room set the field to null
* Hub manages the addition and deletion of rooms, and can assist with changing room associations as requested by controllers
* Added storage code to allow storage and retrieval of rooms from separate file, and update storage code for device to hold the UUID of the room associated with a device, if such a room exists.
* Added storage test for room recreation
* Wrote tests to ensure that room mechanics within Model work as expected

TODO: 
* Add UI & Controller components to allow modification of rooms

### Device Events
* when an event happens the device will inform HUB which will then call on the appropriate Room which will then adjust the appropriate Devices.

### Expanded Test Suite
* Tests for room association by hub
* Tests for camera
* Tests for Thermostat (All devices now have tests)

### Logging
* Logs saved in src/logging/log.log (my most creatively named addition to this project yet)
* Added logging utility class to be used by the entire application
* Added logging to Non-GUI tests indicating when each class starts
* Added logging to model package

### Notifications & JSONMessaging
* Re-purposed JSONMessaging to simply be a utility to generate notifications to cut down on storage costs, notifications are now exclusively stored/represented as plain JSONObjects
* Allowed for notifications about various things in the model (rooms, devices, users) as well as for generic "plain" notifications that are not tied to any particular model entity.
* notifications go through hub and into the notification stack of any users that don't have the device in their blacklist.
* added notifications from significant actions.

### Room Events
* Added events (camera detecting vacancy/motion, thermostat detecting change in temp) and test cases to illustrate their usage.

### Concurrency
* For test Z2 we added labelCheck and statusCheck threads used when constructing the device config screen (see HubSceneBuilder).