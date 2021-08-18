package pro.sanjagh.lamoa.model

import com.typesafe.config.Config

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
    val host: String,
    val port: Int,
    val username: String = "",
    val password: String = ""
)

object Proxy {
  import pro.sanjagh.lamoa.domain.Configuration._

  def apply(proxyConf: Config): Proxy = {
    val proxyType = proxyConf.getString("type") match {
      case pt if pt == "http"        => ProxyType.Http
      case pt if pt == "https"       => ProxyType.Https
      case pt if pt.contains("sock") => ProxyType.Socks
      case _                         => ProxyType.Direct
    }

    val host = proxyConf.getString("host")
    val port = proxyConf.getInt("port")
    val username = proxyConf.getString("username")
    val password = proxyConf.getString("password")

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
