name := "scala-vertx-controllers"

version := "0.1"

scalaVersion := "2.13.3"


lazy val VertxVersion = "3.9.1"

libraryDependencies ++= Seq(
  // logging
  "org.slf4j" % "slf4j-api" % "1.7.5",
  "org.slf4j" % "slf4j-simple" % "1.7.5",
  // web server stuff
  "io.vertx" % "vertx-core" % VertxVersion,
  "io.vertx" % "vertx-web" % VertxVersion,
  "io.vertx" % "vertx-codegen" % VertxVersion,
)