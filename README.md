# Vert.x Web Scala Controllers

### Introduction
I've always wanted a simple way to add my own layer of abstraction on top of Vert.x.

### Overview
The `controllers.Controller` class allows exposes a fluent-style API for adding routes, and the `Server` object exposes a fluent API for adding controllers to a server.

### Key functionality Breakdown
#### `controllers.Controller.scala`

Using `new controllers.Controller(Server.vertx, "/someRootRoute")` one can create a controller and then simple call `.get(routePath) { context => someImplementation() }` to build the controller.

#### `Server.scala`

The `Server` object is a singleton that is used to `.addController` and `start()`

#### `server.ServerVerticle.scala`

This class is what actually does the work of conforming what what Vert.x expects to see (i.e. registering sub-routers, starting the server, etc.)

### Examples

As seen in `Main.scala`, we can create a controller like so:
```
val myController = new controllers.Controller(Server.vertx, "/users")
    .post("/create") { context =>
        // do stuff
        context.response.setStatusCode(200).end("User created.)
    }
    .get("/all") { context =>
        // do some stuff
        context.response.setStatusCode(200).end(usersJson)
    }

Server.addController(myController)
      .addController(someOtherController)
      .start()
```

### Body Handling

We can also specify that we wish to handle parsing the body ourselves, as we can see in the `UploadController`:
```
post("/upload", parseBody = false) {
    // ...
}
```
You may wish to do this if you want to stream a file upload's contents directory to external storage somewhere and not store it directly on the local server.

### Authentication

I added a basic example of JWT bearer authentication, as seen in the `UsersController`. By adding `requireAuthentication = true` to a route,
we can require that a valid JWT bearer token be present in any the HTTP headers for a given route.