import io.vertx.core.Vertx

object Server {
  val vertx: Vertx = Vertx.vertx()
  private val serverVerticle = new ServerVerticle(vertx, 3000, "http://localhost:3000")

  def addController(controller: Controller): Server.type = {
    serverVerticle.router.mountSubRouter(controller.rootPath, controller.router)
    Server
  }

  def start() {
    vertx.deployVerticle(serverVerticle)
  }

  def stop(): Unit = {
    vertx.close()
  }
}