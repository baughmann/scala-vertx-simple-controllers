package controllers

import io.vertx.core.Vertx
import models.Upload
import utilities.JsonUtil

// TODO: Since we cannot get other body data besides file uploads, we will need to send metadata in the headers since there is no body parser
//  maybe look into creating a pre-request containing the document count, document metadata, and file size? that way we can better track upload completion
//  and also see when a file fails to upload completely
class UploadsController(implicit val vertx: Vertx) extends Controller(basePath = "/uploads") {
  post("/create", parseBody = false, requireAuthentication = true) { context =>
    context.request.setExpectMultipart(true)

    context.request.uploadHandler { upload =>
      println(s"Filename: ${upload.filename()}")

      upload.handler { buffer =>
        // send chunk to backend
        println("buffer size", buffer.length())
      }
      // this gets called for EACH uploaded file sequentially
      upload.endHandler { _ =>
        // upload successful
        println("done uploading")
        context.response.setStatusCode(200).end("Uploaded")
      }
      upload.exceptionHandler { e =>
        // handle the exception
        println("exception thrown", e)
      }
    }
  }
}
