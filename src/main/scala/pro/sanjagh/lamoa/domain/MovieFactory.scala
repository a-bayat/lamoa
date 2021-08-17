package pro.sanjagh.lamoa.domain

import java.net.UnknownHostException
import java.nio.file.Path

import pro.sanjagh.lamoa.domain.FileOperations.{filePathRead, removeExtension}
import pro.sanjagh.lamoa.model.MovieDetail

import scala.annotation.tailrec
import scala.io.StdIn
import scala.util.{Failure, Success, Try}
import java.io.File

object MovieFactory {

  def Ignite(address: Option[String]): MovieDetail = {
    val language = Configuration.get.getConfig("config").getString("language")
    val targetFile: File = address match {
      case Some(a) => new File(a)
      case None    => new File(System.getProperty("user.dir"))
    }

    println("------ debug -----")
    println(targetFile.getAbsolutePath())

    val mediaFile = filePathRead(targetFile)
    val extractedMovieName = ExtractMovieName(mediaFile.getName)

    MovieDetail(
      extractedMovieName,
      Path.of(mediaFile.getParent + "/" + removeExtension(mediaFile)),
      getYear(mediaFile.getName),
      "",
      getQuality(mediaFile.getName),
      getResolution(mediaFile.getName),
      language
    )
  }

  def ExtractMovieName(name: String): String = {
    finalMovieName(extractName(StringExtractor.standardString(name))) match {
      case Success(movie) => movie
      case Failure(ex) =>
        println(ex)
        sys.exit
    }
  }

  @tailrec
  def finalMovieName(name: String): Try[String] = {
    val validation = ImdbValidator.validateName(name)

    validation match {
      case Right(listName) if listName.size > 1 =>
        Success(listName(chooseItem(listName)))
      case Right(fileName) if fileName.size == 1 => Success(fileName.head)
      case Left(ex) =>
        ex match {
          case _: UnknownHostException =>
            Failure {
              println(
                "It seems you are not connect to internet. check your connection then try again."
              )
              sys.exit
            }
          case _: NullPointerException =>
            println(s"The movie $name you are looking for is not available.")
            StdIn.readLine("Plz Enter the right movie name: ") match {
              case n: String =>
                finalMovieName(StringExtractor.standardString(n))
            }
        }
    }
  }

  def chooseItem(items: List[String]): Int = {
    println(
      "The most similar available movies listed bellow, please enter the index of right one (to Cancel Operation enter -1): "
    )
    items.zipWithIndex.foreach { case (item, idx) => println(s"\t$idx. $item") }

    @tailrec
    def recItem: Int = {
      print("Enter the index: ")
      Try(StdIn.readInt()) match {
        case Success(idx)
            if idx >= items.indexOf(items.head) && idx <= items.indexOf(
              items.last
            ) || idx == -1 =>
          idx
        case _ => recItem
      }
    }
    recItem match {
      case n if n >= 0 => n
      case n if n == -1 =>
        println("Good luck")
        sys.exit
    }
  }

  private def extractName(name: String): String = {
    val year_regex = "(19|20)\\d{2}".r
    val year = year_regex.findFirstIn(name).mkString
    val finalName = name.substring(0, name.indexOf(year)).trim
    finalName match {
      case str if str.isEmpty => name
      case str: String        => s"$str ($year)"
      case _                  => ""
    }
  }

  private def getResolution(file: String): String = {
    file match {
      case q
          if q.toLowerCase
            .contains("1080p") || q.toLowerCase.contains("1080") =>
        "1080p"
      case q
          if q.toLowerCase.contains("720p") || q.toLowerCase.contains("720") =>
        "720p"
      case q
          if q.toLowerCase.contains("480p") || q.toLowerCase.contains("480") =>
        "480p"
      case q if q.toLowerCase.contains("brrip")  => "BRRip"
      case q if q.toLowerCase.contains("web-dl") => "WEB-DL"
      case q if q.toLowerCase.contains("webrip") => "WEBRip"
      case _                                     => ""
    }
  }

  private def getQuality(file: String): String = {
    file match {
      case q if q.toLowerCase.contains("bluray") => "BluRay"
      case q if q.toLowerCase.contains("bdrip")  => "BDRip"
      case q if q.toLowerCase.contains("brrip")  => "BRRip"
      case q if q.toLowerCase.contains("web-DL") => "WEB-DL"
      case q if q.toLowerCase.contains("webrip") => "WEBRip"
      case q if q.toLowerCase.contains("hdtv")   => "HDTV"
      case q if q.toLowerCase.contains("tvrip")  => "TVRip"
      case q if q.toLowerCase.contains("hdcam")  => "HDCAM"
      case _                                     => ""
    }
  }

  private def getYear(file: String): String = {
    val year_regex = "(19|20)\\d{2}".r
    year_regex.findAllIn(file).mkString
  }

}
