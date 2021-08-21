package pro.sanjagh.lamoa.domain
import sttp.client3.{HttpURLConnectionBackend, Identity, Response, SttpBackend, SttpBackendOptions}
import sttp.client3.quick._
import sttp.model.Uri
import pro.sanjagh.lamoa.model.Proxy
import Configuration._
import pro.sanjagh.lamoa.model.ProxyType._
import sttp.client3

import java.io.InputStream
import scala.concurrent.duration._

object BaseConnection {

  private[domain] def get(url: String): String = {
    quickRequest
      .get(uri"$url")
      .send(getBackend)
      .body
  }

  private[domain] def post(url: String, param: Map[String, String]): String = {
    quickRequest
      .body(param)
      .post(uri"$url")
      .send(getBackend)
      .body
  }

   def download(url: String):Either[String, Array[Byte]] = {
    quickRequest
     .get(uri"$url")
     .response(asByteArray).send(getBackend).body
  }

  private def getBackend: SttpBackend[Identity, Any] = {
    val proxy = Configuration.connection

    val proxyType = proxy.proxyType match {
      case Http =>
        if (proxy.username.trim.isEmpty)
          SttpBackendOptions.httpProxy(proxy.host, proxy.port)
        else
          SttpBackendOptions.httpProxy(
            proxy.host,
            proxy.port,
            proxy.username,
            proxy.password
          )
      case Https =>
        if (proxy.username.trim.isEmpty)
          SttpBackendOptions.httpProxy(proxy.host, proxy.port)
        else
          SttpBackendOptions.httpProxy(
            proxy.host,
            proxy.port,
            proxy.username,
            proxy.password
          )
      case Socks =>
        if (proxy.username.trim.isEmpty)
          SttpBackendOptions.socksProxy(proxy.host, proxy.port)
        else {
          SttpBackendOptions.socksProxy(
            proxy.host,
            proxy.port,
            proxy.username,
            proxy.password
          )
        }
      case _ => SttpBackendOptions.Default
    }

    HttpURLConnectionBackend(
      proxyType.connectionTimeout(Configuration.getTimeout.millisecond)
    )
  }
}
