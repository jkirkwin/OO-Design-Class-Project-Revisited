# Cleanup

## Misc

*~~Modify the README~~ 
    * ~~To explain what this project was~~
    * ~~where it was left (make a snapshot with commit hash: bb715113fc5363c20a732eec36be0a5dea173e60~~
    * ~~what the purpose of this new repo is~~

* ~~Set the project up for IntelliJ. The following should work:~~
    * ~~Gradle~~
    * ~~Code formatting~~
    * ~~Version control~~

* General cleanup
    * ~~Fix up the build script~~
    * Remove or conglomerate random bits and pieces of documentation
    * ~~Get a travis (or other CI) instance up and running~~

* ~~Set up code format/style checking and auto-fixing~~
    * ~~Integrated with gradle~~

* ~~Refactor package structure~~
    * ~~change from existing to have ca.jkirkwin.seng330.refactor be the top level package~~

## Refactoring

* Standardize the logging location

* Re-think the startup application entry-point

* Refactor the Model to be less terrible
    * Remove redundant methods
    * Make use of libraries for JSON serialization instead of hacky DIY stuff that is there now
    * Fix the gross reflection stuff that happens during testing

* Refactor the Controllers and Views to be less terrible
    * Use threads to prevent UI hangup on an action
    * Add tests for the controllers

* Clean up tests as you go, and add new ones where necessary.
    * Try to fix up the reflection stuff - its gross
    * Use Mockito to mock classes properly
    * ~~Figure out headless testing and allow this option from gradle (should be default testing method)~~
        * Provide a mechanism to disable this through Gradle
