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

We can also specify that we wish to handle parsing the body ourselves, 
as we can see in the `UploadController`:
```
post("/upload", parseBody = false) {
    // ...
}
```
You may wish to do this if you want to stream a file upload's contents 
directory to external storage somewhere and not store it directly on 
the local server.

## CORS
Without a `config.cors.host` value set in the configuration file, the default 
CORS allowed origins is `*` (meaning, any origin). However, the value in
`default.conf` is `http://localhost:3000`. You may have noticed that the server 
itself starts on port *5000* according to `default.conf`. 

What's with the port mismatch, you ask? Well, this allows the client and the server
to operate on different URLs. For example, you may have a React JS website
hosted at `https://mysite.com`, and an API hosted at `https://api.mysite.com`. 
In this case, you can simply enter `https://api.mysite.com` as the `config.cors.host`
to allow it to talk to the server.

Also, port 3000 is the default port used to serve local React JS applications
made with `create-react-app`.

### Authentication

I added a basic example of JWT bearer authentication, as seen in the 
`UsersController`. By adding `requireAuthentication = true` to a route,
we can require that a valid JWT bearer token be present in any the HTTP 
headers for a given route.

### Configuration
The server looks for configuration files inside the `src/main/resources` directory.

Configuration files are loaded depending on the `ENVIRONMENT` system environment 
variable. If this variable is not set, then `default.conf` is loaded. 
Otherwise, the server looks for the environment variable's value, 
plus `.conf` inside the `resources` directory.

Reading of the configuration file and creation of the configuration class is done
via the `Configuration` class and it's companion object. The configuration is
made available to the project through the use of the global singleton object 
`Server` and is available as `Server.Config`.

### SSL / TLS
The server looks for a PKCS12 keystore in the root directory, based on the 
`config.server.ssl.keystore` value in the configuration file, and uses it to 
secure HTTPS connections. To generate your own keystore, run a command 
like this using Java Keytool:

Configuration SSL is done in `ServerVerticle` via `HttpServerOptions`.

```
keytool -genkey \
  -alias test \
  -keyalg RSA \
  -keystore server-keystore.jks \
  -storetype PKCS12 \
  -keysize 2048 \
  -validity 360 \
  -dname CN=localhost \
  -keypass secret \
  -storepass secret 
```