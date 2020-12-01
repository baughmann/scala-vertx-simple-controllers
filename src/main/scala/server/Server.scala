package server

import java.util

import controllers.Controller
import io.vertx.core.Vertx
import io.vertx.ext.auth.PubSecKeyOptions
import io.vertx.ext.auth.jwt.{JWTAuth, JWTAuthOptions}
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler._
import io.vertx.ext.web.sstore.LocalSessionStore
import models.Configuration

object Server {
  val Config: Configuration = Configuration()
  val vertx: Vertx = Vertx.vertx()
  val router: Router = Router.router(vertx)
  router.route().handler(LoggerHandler.create(LoggerFormat.DEFAULT))

  // TODO: You would probably wish to use an asymmetric key instead of something like this
  private val authConfig = new JWTAuthOptions()
    .addPubSecKey(new PubSecKeyOptions().setAlgorithm("HS256")
      // according to the VertX Gitter chat, these methods are marked as `deprecated` with no alternatives listed in the Javadocs
      //  maybe a mix-up due to the prepping for 4.0 release?
      .setPublicKey("keyboard cat")
      .setSymmetric(true))
  val authProvider: JWTAuth = JWTAuth.create(vertx, authConfig)
  val authHandler: AuthHandler = JWTAuthHandler.create(authProvider)


  def useCors(allowedHeaders: util.HashSet[String] = new util.HashSet()): Server.type = {
    allowedHeaders.add("Content-Type")
    println("  * Configuring CORS")

    router.route()
      .handler(CorsHandler
        .create(Config.host)
        .allowedHeaders(allowedHeaders))

    this
  }

  def useSessionLocalStorage(): Server.type = {
    router.route().handler(SessionHandler.create(LocalSessionStore.create(vertx)))

    this
  }

  def addController(controller: Controller): Server.type = {
    router.mountSubRouter(controller.basePath, controller.router)

    this
  }

  def start(): Server.type = {
    val verticle = new ServerVerticle(router, vertx)
    verticle.start()

    this
  }
}
