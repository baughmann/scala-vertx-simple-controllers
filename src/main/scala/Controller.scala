import io.vertx.core.{Handler, Vertx}
import io.vertx.ext.web.{Router, RoutingContext}

class Controller(vertx: Vertx, val rootPath: String) {
  val router: Router = Router.router(vertx)

  def post(path: String)(handler: Handler[RoutingContext]): Controller = {
    router.post(path).handler(handler)
    this
  }

  def get(path: String)(handler: Handler[RoutingContext]): Controller = {
    router.get(path).handler(handler)
    this
  }
}
