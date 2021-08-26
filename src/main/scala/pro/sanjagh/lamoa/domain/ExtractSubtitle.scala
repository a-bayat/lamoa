package pro.sanjagh.lamoa.domain

import java.io.{ByteArrayInputStream, File, FileOutputStream, InputStream}
import java.util.zip.ZipInputStream
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import pro.sanjagh.lamoa.model.MovieDetail
import pro.sanjagh.lamoa.setting.AppConfiguration

import scala.util.Try

object ExtractSubtitle {
  import AppConfiguration._

  /**
   * Tries to find subtitle and filter out most match and finally download the file
   * @param path path where we look for video file to get subtitle
   */
  def findSubtitle(path: Option[String]): Unit = {
    val movieDetail: MovieDetail =
      MovieNameExtractor.extractMovieDetails(path, Console) match {
        case Right(x) => x
        case Left(e) =>
          println(e)
          sys.exit
      }
    val elements = checkForSubtitle(movieDetail)
    val matchedUrl = getSimilarObjects(elements, movieDetail.name) match {
      case Some(element) => element.absUrl("href")
      case None =>
        println(s"Subtitle for '${movieDetail.name}' in $language could not be found.")
        sys.exit
    }

    val filteredElement = applyFilterOnResult(matchedUrl, movieDetail)

    downloadFileInto(getDownloadLink(filteredElement), movieDetail)
  }

  /** this will check the validated name is available in the subtitle site
    * @return
    */
  def checkForSubtitle(movieDetail: MovieDetail): Elements = {
    val year_regex = "[(19|20)\\d{2}]"
    val nameWithoutYear = movieDetail.name.replaceAll(year_regex, "").trim
    /*nameWithoutYear:> be omit the year from the name of imdb result because subscene site works better with out
    * year attached to name. and finally check the result with the full name of imdb result */
    val request =
      BaseConnection.post(getSubtitleUrl, Map("query" -> nameWithoutYear))
    val subtitleHtml = Jsoup.parse(request, getSubtitleUrl)
    subtitleHtml.select("div.title")
  }

  /** this function tries to extract the exact time which is applied by
    * checkForSubtitle function
    * @param elements
    *   the available items which is returned on subtitle site which we try to
    *   get the exact item
    * @param name
    *   the standard name of movie which is validated by IMDB site
    * @return
    *   the exact item similar to name
    */
  def getSimilarObjects(
      elements: Elements,
      name: String
  ): Option[Element] = {
    Try {
      val encodedName = name.replace("'", "\\'")
      elements.select(raw"""a:contains($encodedName)""").first match {
        case r if r.text == name => r
      }
    }.toOption
  }

  /** apply all filters on elements related to movie such as quality, resolution
    * and language
    * @param url
    *   the link which returns elements
    * @return
    */
  def applyFilterOnResult(url: String, movieDetail: MovieDetail): Element = {
    val request = BaseConnection.get(url)
    val subtitleHtml = Jsoup.parse(request, getSubtitleUrl)

    var result = subtitleHtml.select(
      s".content table tr td.a1 a:contains(${movieDetail.language})"
    )
    result = movieDetail match {
      case r if r.quality.isEmpty && r.resolution.nonEmpty =>
        result.select(s"a:contains(${movieDetail.resolution})")
      case r if r.quality.nonEmpty && r.quality.nonEmpty =>
        result
          .select(s"a:contains(${movieDetail.resolution})")
          .select(s"a:contains(${movieDetail.quality})")
    }

    result.first()
  }

  /** Tries to extract element download link
    * @param elm
    *   is the specified element contains subtitle link
    * @return
    */
  private def getDownloadLink(elm: Element): String = {
    val url = elm.absUrl("href")
    val request = BaseConnection.get(url)
    Jsoup
      .parse(request, getSubtitleUrl)
      .select(".download a")
      .first
      .absUrl("href")
  }

  /** this function download and unzip subtitle into specified directory
    * @param url
    *   the link we try to download file from it
    */
  /*TODO => How to test this function*/
  def downloadFileInto(url: String, movieDetail: MovieDetail): Unit = {
    val connection = BaseConnection.download(url)

    val is: InputStream = connection match {
      case Right(array) => new ByteArrayInputStream(array)
      case Left(_) =>
        println(
          "Could not download file form subtitle site. you can check it out by bellow link:"
        )
        println(url)
        sys.exit
    }

    val inputStream = new ZipInputStream(is)
    val pathBuilder = new StringBuilder(movieDetail.path.toString)

    val nEntry = inputStream.getNextEntry
    val file = new File(
      pathBuilder
        .append(nEntry.getName.substring(nEntry.getName.lastIndexOf(".")))
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