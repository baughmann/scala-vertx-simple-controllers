package server

import io.vertx.core.http.{HttpServer, HttpServerOptions}
import io.vertx.core.net.JksOptions
import io.vertx.core.{AbstractVerticle, AsyncResult, Vertx}
import io.vertx.ext.web.Router
import sun.misc.ObjectInputFilter.Config
import utilities.Utils


class ServerVerticle(router: Router, vertx: Vertx) extends AbstractVerticle {
  override def start(): Unit = {
    println("  * Setting up Vert.x HTTP Server")

    val serverOptions = new HttpServerOptions()
      if(Server.Config.useSsl) {
        println("  * Configuring SSL")

        serverOptions.setSsl(true)
          .setKeyStoreOptions(new JksOptions()
            .setPath(Server.Config.sslKeystoreName)
            .setPassword(Server.Config.sslKeystorePassword))
      }

    val server = vertx.createHttpServer(serverOptions)

    if (Utils.isPortInUse(Server.Config.port)) {
      println(s"[!] Error! Tried to run on port ${Server.Config.port}, but it is already in use!")
    }

    println("[X] Starting server...")

    server
      .requestHandler(router)
      .listen(Server.Config.port, (res: AsyncResult[HttpServer]) => {
        if (res.succeeded()) {
          println(s"    [X] Server is running on port ${Server.Config.port}")
        }
        if (res.failed()) {
          println("    [!] Failed to start server!")
          throw res.cause()
        }
      })
  }
}
