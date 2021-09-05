package pro.sanjagh.lamoa.model


sealed trait Fault 

case class NoVideoFileWasFound(address: String) extends Fault
case class ImdbConnectionException(throwable: Throwable) extends Fault
case class SubsceneConnectionException(throwable: Throwable) extends Fault
case class SubtitleDownloadException(throwable: Throwable) extends Fault
case class MovieNotFoundInImdb(name: String) extends Fault
case class SubtitleNotFoundInSubscene(name: String) extends Fault
