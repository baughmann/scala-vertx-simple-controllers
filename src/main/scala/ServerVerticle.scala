import java.util

import io.vertx.core.{AbstractVerticle, AsyncResult, Vertx}
import io.vertx.core.http.HttpServer
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.{BodyHandler, CorsHandler, LoggerFormat, LoggerHandler, SessionHandler}
import io.vertx.ext.web.sstore.LocalSessionStore

class ServerVerticle(vertx: Vertx, private val port: Int, private val host: String) extends AbstractVerticle {
  val router: Router = Router.router(vertx)
  val allowedHeaders = new util.HashSet[String]
  allowedHeaders.add("Content-Type")

  override def start(): Unit = {
    println("  * Setting up Vert.x router")
    val server = vertx.createHttpServer()

    println("  * Setting up request logging")
    router.route().handler(LoggerHandler.create(LoggerFormat.DEFAULT))

    println("  * Configuring CORS and BodyHandler")
    router.route()
      .handler(CorsHandler
        .create(host)
        .allowedHeaders(allowedHeaders))
      .handler(BodyHandler.create())

    router.route().handler(SessionHandler.create(LocalSessionStore.create(vertx)))

    println("[X] Starting server...")
    if (Utils.isPortInUse(port)) {
      println(s"[!] Error! Tried to run on port $port, but it is already in use!")
    }

    server.requestHandler(router)
      .listen(3000, (res: AsyncResult[HttpServer]) => {
        if (res.succeeded()) {
          println(s"    [X] Server is running on port $port")
        }
        if (res.failed()) {
          println("    [!] Failed to start server!")
          throw res.cause()
        }
      })
  }
}
