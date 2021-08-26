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
      val request = BaseConnection.get(s"$getImdb_url$movieName &s=tt")
      val imdbHtml = Jsoup.parse(request)
      val titleElements: Elements = imdbHtml
        .select(".findSection .findSectionHeader:contains(Titles)")
        .parents
        .first
        .select("table.findList tr td.result_text")
        .not(
          ":contains(TV Episode), :contains(TV Series), :contains(in development), :contains(TV Mini Series)"
        )
      titleElements.eachText.asScala.toList
    }.toEither.swap.map(ImdbConnectionException).swap
  }
}
