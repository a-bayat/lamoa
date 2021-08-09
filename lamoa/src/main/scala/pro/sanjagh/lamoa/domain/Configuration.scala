package pro.sanjagh.lamoa.domain

import com.typesafe.config.Config

object Configuration {

  import com.typesafe.config.ConfigFactory
  val get: Config = ConfigFactory.load("application.conf").getConfig("lamoa")

}
