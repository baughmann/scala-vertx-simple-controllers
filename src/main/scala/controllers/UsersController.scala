package controllers

import io.vertx.core.{Vertx, json}
import io.vertx.core.json.JsonObject
import io.vertx.ext.jwt.JWTOptions
import models.{LoginRequest, LoginResponse, User}
import server.Server
import utilities.JsonUtil

class UsersController()(implicit vertx: Vertx) extends Controller(basePath = "/users") {
  post("/login") { context =>
    val request = JsonUtil.fromJson[LoginRequest](context.getBodyAsString)

    if (request.username == "me" && request.password == "password1") {
      val token = Server.authProvider.generateToken(new JsonObject().put("username", request.username), new JWTOptions())
      val response = LoginResponse(token)
      context.response().putHeader("Access-Control-Allow-Origin", "*")

      context.response.setStatusCode(200).end(JsonUtil.toJson(response))
    } else {
      context.response.setStatusCode(401).end(JsonUtil.toJson(new JsonObject().put("error", "Invalid username or password.")))
    }
  }
}
