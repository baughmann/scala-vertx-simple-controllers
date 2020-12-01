package models

import java.nio.file.{Files, Paths}

import com.typesafe.config.ConfigFactory

class Configuration(
                     val host: String,
                     val port: Int,
                     val jwtSecretKey: String,
                     val useSsl: Boolean,
                     val sslKeystoreName: String = null,
                     val sslKeystorePassword: String = null
                   )

object Configuration {
  def apply(): Configuration = {
    println("[X] Reading configuration...")
    val environment = sys.env.get("ENVIRONMENT").orElse(Option("default")).get
    val configFileExists =
      Files.exists(Paths.get(s"src/main/resources/$environment.conf"))
    val configFile =
      if (configFileExists) environment else "default"
    println(s"  * Looking for configuration file '$configFile.conf'")
    val config =
      ConfigFactory.load(configFile)

    println("  * Constructing configuration")
    // address stuff & cors stuff
    val hasCorsHost = config.hasPath("config.server.cors.host")
    val host: String = if (hasCorsHost) config.getString("config.server.cors.host") else "*"
    val port: Int = config.getInt("config.server.port")
    // authentication stuff
    val jwtSecretKey = config.getString("config.server.authentication.secretKey")
    // ssl stuff
    val useSsl = config.hasPath("config.server.ssl.useSsl") && config.getBoolean("config.server.ssl.useSsl")
    val sslKeystoreName = if (useSsl) config.getString("config.server.ssl.keystore") else null
    val sslKeystorePassword = if (useSsl) config.getString("config.server.ssl.keystorePassword") else null

    new Configuration(
      host = host,
      port = port,
      jwtSecretKey = jwtSecretKey,
      useSsl = useSsl,
      sslKeystoreName = sslKeystoreName,
      sslKeystorePassword = sslKeystorePassword
    )
  }
}


