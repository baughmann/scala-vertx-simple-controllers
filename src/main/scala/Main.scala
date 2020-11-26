import controllers.{Controller, UploadsController, UsersController}
import io.vertx.core.Vertx
import io.vertx.ext.web.RoutingContext
import server.Server

object Main extends App {
  implicit val vertx: Vertx = Server.vertx

  Server.useCors("http://localhost:5000")
    .useSessionLocalStorage()
    .addController(new UsersController)
    .addController(new UploadsController)
    .start(5000)
}
