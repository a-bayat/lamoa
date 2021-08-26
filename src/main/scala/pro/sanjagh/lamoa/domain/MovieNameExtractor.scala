package pro.sanjagh.lamoa.domain

import pro.sanjagh.lamoa.setting.{UserConfiguration, AppConfiguration}
import java.nio.file.Path
import pro.sanjagh.lamoa.domain.MoviePathIdentifier.removeExtension
import pro.sanjagh.lamoa.model.MovieDetail
import pro.sanjagh.lamoa.model.MovieNotFoundInImdb
import pro.sanjagh.lamoa.model.Fault
import scala.util.Try

/** Extract the standard name of movie by it's name validate the name on IMDB
  * site
  */
object MovieNameExtractor {
  import AppConfiguration._

  /** Create a Movie Model by extracting data from it's name
    * @param path
    *   the address we wish to get movie items
    * @param ui
    *   generic helper
    * @return
    *   the detailed information model of movie
    */
  /*TODO: => how to test this function*/
  def extractMovieDetails(
      path: Option[String],
      ui: UI
  ): Either[Fault, MovieDetail] =
    for {
      files <- MoviePathIdentifier.getVideoFilesIn(path)
      targetFile = ui.choose(
        files,
        "Available videos in directory, choose target video: "
      )(_.getName)
      cleanedUpName = cleanedUpMovieName(targetFile.getName)
      imdbCandidates <- ImdbValidator
        .getImdbCandidates(cleanedUpName)
        .filterOrElse(_.nonEmpty, MovieNotFoundInImdb(cleanedUpName))
      imdbValidMovieName = getMostMatchToImdb(cleanedUpName, imdbCandidates)
        .getOrElse {
          ui.choose(imdbCandidates, "Which movie ?")(identity)
        }
    } yield {
      MovieDetail(
        imdbValidMovieName,
        Path.of(targetFile.getParent + "/" + removeExtension(targetFile)),
        findReleaseYear(targetFile.getName),
        "",
        findMovieQuality(targetFile.getName),
        findMovieResolution(targetFile.getName),
        UserConfiguration.language
      )
    }

  /** Extract the name of movie from its file name and remove additional things
    * from it's name it also split the words by the defined pattern and remove
    * @param movieFileName
    *   the name of movie
    * @return
    *   the pure name of movie (The.Name.Of.Movie.2013.1080p.mp4) => The Name Of
    *   Movie (2013) for example
    */
  def cleanedUpMovieName(movieFileName: String): String = {
    val name = movieNamePurify(movieFileName)
    val year = findReleaseYear(name)
    val finalName = name.substring(0, name.indexOf(year)).trim
    finalName match {
      case str if str.isEmpty => name
      case str: String        => s"$str ($year)"
      case _                  => name
    }
  }

  /** get the most similar item from the list of available items by IMDB result
    * @param videoName
    *   the movie name we try to find best match
    * @param imdbAvailableVideo
    *   the result of IMDB item list
    * @return
    */
  def getMostMatchToImdb(
      videoName: String,
      imdbAvailableVideo: List[String]
  ): Option[String] = {
    Try {
      val idx = imdbAvailableVideo.indexWhere(video => video == videoName)
      imdbAvailableVideo(idx)
    }.toOption

  }

  /** remove special character such [.-_*+ and ...]
    * @param movieName
    *   the name of movie name
    * @return
    *   the pure name of movie (The.Name.Of.Movie.2013.1080p.mp4) => The Name Of
    *   Movie 2013 1080p
    */
  def movieNamePurify(movieName: String): String = {
    getMovieNameSplitterPattern
      .findAllIn(movieName)
      .foldLeft("") { (a, b) => a + " " + b }
      .trim
  }

  /** detect if the movie resolution is 1080 - 720 - 480
    * @param movieName
    *   the name of movie
    * @return
    *   Resolution of movie file
    */
  def findMovieResolution(movieName: String): String = {
    movieName match {
      case q if q.toLowerCase.contains("4320") => "4320p" // 8K
      case q if q.toLowerCase.contains("2160") => "2160p" // 4K
      case q if q.toLowerCase.contains("1080") => "1080p"
      case q if q.toLowerCase.contains("720")  => "720p"
      case q if q.toLowerCase.contains("480")  => "480p"
      case _                                   => ""
    }
  }

  /** detect the common quality of movie
    * @param movieName
    *   the name of movie
    * @return
    *   Quality of movie file
    */
  def findMovieQuality(movieName: String): String = {
    movieName match {
      case q if q.toLowerCase.contains("bluray") => "BluRay"
      case q if q.toLowerCase.contains("bdrip")  => "BDRip"
      case q if q.toLowerCase.contains("brip")   => "BRip"
      case q if q.toLowerCase.contains("hdrip")  => "HDRip"
      case q if q.toLowerCase.contains("web-dl") => "WEB-DL"
      case q if q.toLowerCase.contains("webrip") => "WEBRip"
      case q if q.toLowerCase.contains("hdtv")   => "HDTV"
      case q if q.toLowerCase.contains("tvrip")  => "TVRip"
      case q if q.toLowerCase.contains("hdcam")  => "HDCAM"
      case q if q.toLowerCase.contains("dvdrip") => "DVDRip"
      case _                                     => ""
    }
  }

  /** extract the release year of movie
    * @param movieName
    *   the name of movie
    * @return
    *   release year
    */
  def findReleaseYear(movieName: String): String = {
    val year_regex = "(19|20)\\d{2}".r
    year_regex.findFirstIn(movieName).mkString
  }
}
