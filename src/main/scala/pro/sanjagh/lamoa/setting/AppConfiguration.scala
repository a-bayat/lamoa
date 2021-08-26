package pro.sanjagh.lamoa.setting

import com.typesafe.config.{Config, ConfigFactory}
import pro.sanjagh.lamoa.setting.UserConfiguration.config

import scala.util.Try
import scala.util.matching.Regex

/**
 * This Config file related to application settings which read setting from application.conf
 */
object AppConfiguration extends Configuration {

  val config: Config = ConfigFactory.load("application.conf").getConfig("lamoa")

  lazy val getTimeout: Int = Try(config.getInt("timeout")).getOrElse(5000)

  lazy val getSubtitleUrl: String = Option(
    config.getConfig("server.subtitle").getString("address")
  ).get

  lazy val getImdb_url: String = Option(
    config.getConfig("server.validator").getString("address")
  ).get

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