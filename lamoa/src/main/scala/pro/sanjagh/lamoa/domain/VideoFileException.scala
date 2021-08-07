package pro.sanjagh.lamoa.domain

trait VideoFileException extends RuntimeException {

  val `type`: String

  val title: String

  val description: Option[String]
}

object VideoFileException {
  class InvalidArgumentException(
      val title: String,
      val subType: Option[String] = None,
      val description: Option[String] = None
  ) extends VideoFileException {
    val `type`: String = Some("sanjagh://lamoa/EmptySequence").flatMap { base =>
      subType.map(s => s"$base/$s")
    }.get
  }

}
