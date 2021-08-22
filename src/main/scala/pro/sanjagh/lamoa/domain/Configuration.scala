package pro.sanjagh.lamoa.domain

import com.typesafe.config.Config
import pro.sanjagh.lamoa.model.Proxy

import scala.util.Try

object Configuration {

  import com.typesafe.config.ConfigFactory
  val get: Config = ConfigFactory.load("application.conf").getConfig("lamoa")

  lazy val language = get.getString("language")
  private[domain] def getSubtitleUrl: String = {
    Option(
      Configuration.get.getConfig("server.subtitle").getString("address")
    ).get
  }

  private[domain] def getTimeout: Int = {
    Try(Configuration.get.getConfig("server.subtitle").getInt("timeout"))
      .getOrElse(5000)
  }

  def connection: Proxy = {
    val proxyConf = Configuration.get.getConfig("config").getConfig("proxy")
    Proxy(proxyConf)
  }

  def getIpV4Regex: String = {
    Option(Configuration.get.getConfig("config").getString("ip.v4_pattern")).get
  }

  def getIpV6Regex: String = {
    Option(Configuration.get.getConfig("config").getString("ip.v6_pattern")).get
  }
}
