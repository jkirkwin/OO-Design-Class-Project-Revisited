# Changes Since A3 Submission

### Controller Refactor
* Broke controller up into many different sub-controller, each extending Controller. Package structure now mimics that in view (i.e. there is a loose bijection between Controllers and SceneBuilders). Extensibility greatly improved.