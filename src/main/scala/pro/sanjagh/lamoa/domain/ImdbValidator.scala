package pro.sanjagh.lamoa.domain

import scala.jdk.CollectionConverters._
import org.jsoup.Jsoup
import org.jsoup.select.Elements

import scala.util.Try
import pro.sanjagh.lamoa.model.ImdbConnectionException

object ImdbValidator {
  private val imdb_url =
    Configuration.get.getConfig("server.validator").getString("address")

  def getImdbCandidates(name: String): Either[ImdbConnectionException, List[String]] = {
    Try {
      val request = BaseConnection.get(s"$imdb_url$name&s=tt")
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
    }.toEither.swap.map(ImdbConnectionException.apply).swap
  }

  def validateName(name: String): Either[Throwable, List[String]] = {
    Try {
      val request = BaseConnection.get(s"$imdb_url$name&s=tt")
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
    }.toEither
  }
}
