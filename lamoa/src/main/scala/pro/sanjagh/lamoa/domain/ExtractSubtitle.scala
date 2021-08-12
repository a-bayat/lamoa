package pro.sanjagh.lamoa.domain

import org.jsoup.Jsoup
import pro.sanjagh.lamoa.model.MovieDetail

object ExtractSubtitle {
  private val subtitle_url =
    Configuration.get.getConfig("server.subtitle").getString("address")
  private val timeout =
    Configuration.get.getConfig("server.subtitle").getInt("timeout")

  private val subtitleName: MovieDetail = MovieFactory.Ignite()

  def extract: String = {
    val subtitleHtml =
      Jsoup.connect(s"$subtitle_url").timeout(timeout).data("query", subtitleName.name).post
    val elements = subtitleHtml.select("div.title a:lt(5)")
    elements.forEach(e => println(e.text))
    ""
  }

}
