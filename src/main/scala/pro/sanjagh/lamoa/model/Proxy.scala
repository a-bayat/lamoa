package pro.sanjagh.lamoa.model

import com.typesafe.config.Config

import scala.util.Try

/** ProxyType is an product algebraic datatype which specifies type of
  * connection
  */
sealed trait ProxyType extends Product with Serializable

object ProxyType {
  case object Http extends ProxyType
  case object Https extends ProxyType
  case object Socks extends ProxyType
  case object Direct extends ProxyType
}

class Proxy(
    val proxyType: ProxyType = ProxyType.Direct,
    val host: String = "",
    val port: Int = 0,
    val username: String = "",
    val password: String = ""
)

object Proxy {

  def apply(proxyConf: Config): Proxy = {
    val proxyTypeValue: String =
      Try(proxyConf.getString("type")).getOrElse("")

    val proxyType = proxyTypeValue match {
      case pt if pt == "http"        => ProxyType.Http
      case pt if pt == "https"       => ProxyType.Https
      case pt if pt.contains("sock") => ProxyType.Socks
      case _                         => ProxyType.Direct
    }

    val host = Try(proxyConf.getString("host")).getOrElse("")
    val port = Try(proxyConf.getInt("port")).getOrElse(0)
    val username = Try(proxyConf.getString("username")).getOrElse("")
    val password = Try(proxyConf.getString("password")).getOrElse("")

    Proxy(proxyType, host, port, username, password)
  }

  def apply(
      proxyType: ProxyType,
      host: String,
      port: Int,
      username: String = "",
      password: String = ""
  ): Proxy = {
    new Proxy(proxyType, host, port, username, password)
  }
}
