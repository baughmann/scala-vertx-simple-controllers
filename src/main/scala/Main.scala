import io.vertx.ext.web.RoutingContext

object Main extends App {
  val userController = new Controller(Server.vertx, "/users")
    .get("/") { context: RoutingContext =>
      context.response().setStatusCode(200).end("A list of users")
    }
    .post("/signup") { context =>
      context.response().setStatusCode(200).end("Signup")
    }

  val postsController = new Controller(Server.vertx, "/posts")
    .get("/top") { context: RoutingContext =>
      context.response().setStatusCode(200).end("A list of popular posts")
    }
    .post("/add") { context =>
      context.response().setStatusCode(200).end("Post added")
    }

  Server.addController(userController).addController(postsController)

  Server.start()
}
