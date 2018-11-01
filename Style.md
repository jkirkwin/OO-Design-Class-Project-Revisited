# A style planning document to record the development of our design decisions and ideas

## User Management 
User interface, implemented by a BasicUser class, AdminUser extends BasicUser
Each user object should store the User's username, password, UUID?, and a list of all the devices (or their UUID's) that the user has access to.

## Devices
Devices should be designated PUBLIC or ADMIN_ONLY. This designation dictates the behaviour of the model when new users are added. Newly added users
are given access to PUBLIC devices automatically but no ADMIN devices. This can be modified by AdminUsers in the admin view

## Model startup and shutdown
For now, we will spin up a "blank" model each time - that is, no devices will be automatically associated and only two users (one admin and one regular user)
will be available for use. Once the code base has been developed further, we will save the state of the model at shutdown() into text files containing JSON 
representations of the devices, users, and any other stateful objects. These files will be parsed during startup() to re-create all these objects as they were.

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

### Questions
* Who can make devices?
* Is the first User automatically an Admin?
* How is a User made an Admin?
* Can Admin Kill Users?
* Where does error checking happen?
* Input checking?
* Logging HOW!?

### Logistic Questions
* JAVAFX vs Spring?
* Gradle HOW?? -> Go to Neil's Office Hours
* Is Travis working 100?
* Logging where?
* CheckStyle working?
* SPOTLESS!!!!!!!!!!

