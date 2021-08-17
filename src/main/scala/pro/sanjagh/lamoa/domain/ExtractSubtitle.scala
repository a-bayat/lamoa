package pro.sanjagh.lamoa.domain

import java.io.{File, FileOutputStream}
import java.util.zip.ZipInputStream

import org.jsoup.Connection.Method
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import pro.sanjagh.lamoa.model.MovieDetail

import scala.util.{Failure, Success, Try}

object ExtractSubtitle {
  private val subtitle_url =
    Configuration.get.getConfig("server.subtitle").getString("address")
  private val timeout =
    Configuration.get.getConfig("server.subtitle").getInt("timeout")

  val subtitleName: MovieDetail = MovieFactory.Ignite()

  /**
   * this function is the main function which handle all the logic of this object
   */
  def extract(): Unit = {
    val elements = checkForSubtitle
    val matchedUrl = getSimilarObjects(elements, subtitleName.name) match {
      case Success(a) => a.absUrl("href")
      case Failure(_) =>
        println("Subtitle could not be found.")
        sys.exit
    }

    val filteredElement = applyFilter(matchedUrl)

    downloadFileInto(getDownloadLink(filteredElement))
  }

  /**
   * this will check the validated name is available in the subtitle site
   * @return
   */
  def checkForSubtitle: Elements = {
    val subtitleHtml =
      Jsoup
        .connect(s"$subtitle_url")
        .timeout(timeout)
        .data("query", subtitleName.name)
        .post

    subtitleHtml.select("div.title")
  }

  /**
   * this function tries to extract the exact time which is applied by checkForSubtitle function
   * @param elements the available items which is returned on subtitle site which we try to get the exact item
   * @param name the standard name of movie which is validated by IMDB site
   * @return the exact item similar to name
   */
  def getSimilarObjects(elements: Elements, name: String): Try[Element] = {

    elements.select(s"a:contains($name)").first match {
      case r if r.text == name => Success(r)
      case _ =>
        Failure(
          throw new Exception("Subtitle Not found")
        ) // TODO: throw NotFoundSubtitleException
    }
  }

  /**
   * apply all filters on elements related to movie such as quality, resolution and language
   * @param url the link which returns elements
   * @return
   */
  def applyFilter(url: String): Element = {
    val subtitleHtml =
      Jsoup
        .connect(url)
        .timeout(timeout)
        .get()

    var result = subtitleHtml.select(
      s".content table tr td.a1 a:contains(${subtitleName.language})"
    )
    result = subtitleName match {
      case r if r.quality.isEmpty && r.resolution.nonEmpty =>
        result.select(s"a:contains(${subtitleName.resolution})")
      case r if r.quality.nonEmpty && r.quality.nonEmpty =>
        result
         .select(s"a:contains(${subtitleName.resolution})")
         .select(s"a:contains(${subtitleName.quality})")
    }

    result.first()
  }

  /**
   * Tries to extract element download link
   * @param elm is the specified element contains subtitle link
   * @return
   */
  private def getDownloadLink(elm: Element): String = {
    val url = elm.absUrl("href")

    Jsoup.connect(url).get().select(".download a").first.absUrl("href")
  }

  /**
   * this function download and unzip subtitle into specified directory
   * @param url the link we try to download file from it
   */
  def downloadFileInto(url: String): Unit = {
    val intro = Jsoup
     .connect(url)
     .followRedirects(true)
     .ignoreContentType(true)
     .maxBodySize(0)
     .method(Method.GET)
     .execute

    val inputStream = new ZipInputStream(intro.bodyStream())
    val pathBuilder = new StringBuilder(subtitleName.path.toString)

    val amir = inputStream.getNextEntry
    val file = new File(
      pathBuilder
       .append(amir.getName.substring(amir.getName.lastIndexOf(".")))
       .toString()
    )
    val fos = new FileOutputStream(file)
    val buffer = new Array[Byte](1024)
    var len = inputStream.read(buffer)
    while (len != -1) {
      fos.write(buffer, 0, len)
      len = inputStream.read(buffer)
    }
  }
}
