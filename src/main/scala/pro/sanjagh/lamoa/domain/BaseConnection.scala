package pro.sanjagh.lamoa.domain
import pro.sanjagh.lamoa.model.{Fault, SubsceneConnectionException, SubtitleDownloadException}
import pro.sanjagh.lamoa.setting.{AppConfiguration, UserConfiguration}
import pro.sanjagh.lamoa.model.ProxyType._
import sttp.client3.{HttpURLConnectionBackend, Identity, SttpBackend, SttpBackendOptions}
import sttp.client3.quick._
import scala.concurrent.duration._
import scala.util.Try

object BaseConnection {

  private[domain] def get(url: String): Either[Throwable, String] = {
    Try {
      quickRequest
       .get(uri"$url")
       .send(getBackend)
       .body
    }.toEither
  }

  private[domain] def post(url: String, param: Map[String, String]): Either[Fault, String] = {
    Try {
      quickRequest
       .body(param)
       .post(uri"$url")
       .send(getBackend)
       .body
    }.toEither.swap.map(SubsceneConnectionException).swap
  }

  def download(url: String): Either[SubtitleDownloadException, Array[Byte]] = {
    Try {
      quickRequest
       .get(uri"$url")
       .response(asByteArray)
       .send(getBackend)
       .body.getOrElse(Array())
    }.toEither.swap.map(SubtitleDownloadException).swap
  }

  private def getBackend: SttpBackend[Identity, Any] = {
    val proxy = UserConfiguration.connection

    val proxyType = proxy.proxyType match {
      case Http | Https =>
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
      proxyType.connectionTimeout(AppConfiguration.getTimeout.millisecond)
    )
  }
}
