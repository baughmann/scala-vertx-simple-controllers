name := "scala-vertx-controllers"

version := "0.1"

scalaVersion := "2.13.3"


lazy val VertxVersion = "3.9.1"
lazy val Slf4j_Version = "1.7.5"
lazy val FasterXml_Version = "2.11.1"

libraryDependencies ++= Seq(
  // logging
  "org.slf4j" % "slf4j-api" % Slf4j_Version,
  "org.slf4j" % "slf4j-simple" % Slf4j_Version,
  // web server stuff
  "io.vertx" % "vertx-core" % VertxVersion,
  "io.vertx" % "vertx-web" % VertxVersion,
  "io.vertx" % "vertx-codegen" % VertxVersion,
  "io.vertx" % "vertx-auth-jwt" % VertxVersion,
  // type serializeation/de-serialization
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % FasterXml_Version,
  "com.fasterxml.jackson.core" % "jackson-databind" % FasterXml_Version,
  // configuration
  "com.typesafe" % "config" % "1.4.0",
)