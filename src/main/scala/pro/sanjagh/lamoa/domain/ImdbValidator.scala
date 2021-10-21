package pro.sanjagh.lamoa.domain

import scala.jdk.CollectionConverters._
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import scala.util.Try
import pro.sanjagh.lamoa.setting.AppConfiguration
import pro.sanjagh.lamoa.model.ImdbConnectionException

object ImdbValidator {
  import AppConfiguration.getImdb_url

  /** check the given movie name to be available on IMDB
    * @param movieName
    *   the movie name wish to validate
    * @return
    *   all the matched result to given parameter
    */
  def getImdbCandidates(
      movieName: String
  ): Either[ImdbConnectionException, List[String]] = {
    Try {
      val request = RequestWrapper.get(getImdb_url.format(movieName))
      val imdbHtml = Jsoup.parse(request)
      val titleElements: Elements = imdbHtml
        .select(s".findResult .result_text")
        .not(
          ":contains(TV Episode), :contains(TV Series), :contains(in development), :contains(TV Mini Series), :contains(Short)"
        )
      titleElements
        .select("a")
        .eachText
        .asScala
        .toList
        .zipWithIndex
        .map { case (item, idx) =>
          s"${item} (${findReleaseYear(titleElements.get(idx).text)})"
        }
        .distinct

      /** Description: First extract the name and then merge the result with
        * release year of each movie in this way the pure name of each movie
        */

    }.toEither.swap.map(ImdbConnectionException).swap
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
