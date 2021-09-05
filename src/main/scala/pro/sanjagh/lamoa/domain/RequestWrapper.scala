package pro.sanjagh.lamoa.domain

trait RequestUI[T <: String] {
  def get(uri: T): T
  def post(uri: T, param: Map[T, T]): T
}

object RequestWrapper extends RequestUI[String] {
  override def get(uri: String): String = BaseConnection.get(uri) match {
    case Right(response) => response
    case Left(e) =>
      println(e.toString)
      sys.exit
  }

  override def post(uri: String, param: Map[String, String]): String =
    BaseConnection.post(uri, param) match {
      case Right(response) => response
      case Left(e) =>
        println(e.toString)
        sys.exit
    }
}
