package controllers

import io.vertx.core.{Handler, Vertx}
import io.vertx.ext.web.handler.BodyHandler
import io.vertx.ext.web.{Router, RoutingContext}
import server.Server

class Controller(val basePath: String)(implicit vertx: Vertx) {
  val router: Router = Router.router(vertx)

  def post(path: String, parseBody: Boolean = true, requireAuthentication: Boolean = false)(handler: Handler[RoutingContext]): Controller = {
    val route = router.post(path)

    if (parseBody) {
      route.handler(BodyHandler.create())
    }

    if(requireAuthentication) {
      route.handler(Server.authHandler)
    }

    route.handler(handler)
    this
  }

  def get(path: String, requireAuthentication: Boolean = false)(handler: Handler[RoutingContext]): Controller = {
    val route = router.get(path)

    if(requireAuthentication) {
      route.handler(Server.authHandler)
    }

    route.handler(handler)

    this
  }
}
