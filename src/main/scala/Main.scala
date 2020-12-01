import controllers.{UploadsController, UsersController}
import io.vertx.core.Vertx
import server.Server

object Main extends App {
  implicit val vertx: Vertx = Server.vertx

  Server.useCors()
    .useSessionLocalStorage()
    .addController(new UsersController)
    .addController(new UploadsController)
    .start()
}
