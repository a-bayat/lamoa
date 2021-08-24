package pro.sanjagh.lamoa.domain

import com.typesafe.config.Config
import pro.sanjagh.lamoa.model.Proxy
import com.typesafe.config.ConfigFactory

import scala.util.Try
import scala.util.matching.Regex

object Configuration {

  private lazy val config: Config =
    ConfigFactory.load("application.conf").getConfig("lamoa")

  lazy val language: String = Option(
    config.getConfig("config").getString("language")
  ).get

  lazy val getSubtitleUrl: String = Option(
    config.getConfig("server.subtitle").getString("address")
  ).get

  lazy val getImdb_url: String = Option(
    config.getConfig("server.validator").getString("address")
  ).get

  lazy val getTimeout: Int =
    Try(config.getConfig("server.subtitle").getInt("timeout"))
      .getOrElse(5000)

  lazy val connection: Proxy = Proxy(
    config.getConfig("config").getConfig("proxy")
  )

  lazy val getIpV4Regex: String = Option(
    config.getConfig("config").getString("ip.v4_pattern")
  ).get

  lazy val getIpV6Regex: String = Option(
    config.getConfig("config").getString("ip.v6_pattern")
  ).get

  lazy val getMovieNameSplitterPattern: Regex = Option(
    config.getConfig("config").getString("movie_name_split_pattern").r
  ).get
}
