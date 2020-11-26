package server

import io.vertx.core.http.HttpServer
import io.vertx.core.{AbstractVerticle, AsyncResult, Vertx}
import io.vertx.ext.web.Router
import utilities.Utils


class ServerVerticle(port: Int, router: Router, vertx: Vertx) extends AbstractVerticle {
  override def start(): Unit = {
    println("  * Setting up Vert.x HTTP Server")
    val server = vertx.createHttpServer()
    println("[X] Starting server...")
    if (Utils.isPortInUse(port)) {
      println(s"[!] Error! Tried to run on port $port, but it is already in use!")
    }

    //    // we need a body gandler to recieve a body
    //    router.post("/json").handler(BodyHandler.create())
    //      .handler { context =>
    //        val request = JsonUtil.fromJson[SomeRequest](context.getBodyAsString)
    //        println("i see", request.name)
    //        context.response.setStatusCode(200).end("Posted")
    //      }
    //
    //    // but we cannot have a body handler when we want to recieve uploads...
    //    router.post("/upload")
    //      .handler { context =>
    //        context.request.setExpectMultipart(true)
    //        context.request.uploadHandler { upload =>
    //          upload.handler { buffer =>
    //            // send chunk to backend
    //            println("buffer size", buffer.length())
    //          }
    //          upload.endHandler { _ =>
    //            // upload successful
    //            println("done uploading")
    //            context.response.setStatusCode(200).end("Uploaded")
    //          }
    //          upload.exceptionHandler { e =>
    //            // handle the exception
    //            println("exception thrown", e)
    //          }
    //        }
    //      }


    server
      .requestHandler(router)
      .listen(port, (res: AsyncResult[HttpServer]) => {
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
