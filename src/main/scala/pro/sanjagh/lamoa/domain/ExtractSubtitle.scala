package pro.sanjagh.lamoa.domain

import java.io.{BufferedInputStream, ByteArrayInputStream, File, FileOutputStream, InputStream}
import java.util.zip.ZipInputStream
import org.jsoup.Connection.Method
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import pro.sanjagh.lamoa.model.MovieDetail

import scala.util.{Failure, Success, Try}

object ExtractSubtitle {
  import Configuration._

  /** this function is the main function which handle all the logic of this
    * object
    */
  def extract(address: Option[String]): Unit = {
    val movieDetail: MovieDetail = MovieFactory.Ignite(address)
    val elements = checkForSubtitle(movieDetail)
    val matchedUrl = getSimilarObjects(elements, movieDetail.name) match {
      case Success(a) => a.absUrl("href")
      case Failure(_) =>
        println("Subtitle could not be found.")
        sys.exit
    }

    val filteredElement = applyFilter(matchedUrl, movieDetail)

    downloadFileInto(getDownloadLink(filteredElement), movieDetail)
  }

  /** this will check the validated name is available in the subtitle site
    * @return
    */
  def checkForSubtitle(movieDetail: MovieDetail): Elements = {
    val request =
      BaseConnection.post(getSubtitleUrl, Map("query" -> movieDetail.name))
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
  def getSimilarObjects(elements: Elements, name: String): Try[Element] = {

    elements.select(s"a:contains($name)").first match {
      case r if r.text == name => Success(r)
      case _ =>
        Failure(
          throw new Exception("Subtitle Not found")
        ) // TODO: throw NotFoundSubtitleException
    }
  }

  /** apply all filters on elements related to movie such as quality, resolution
    * and language
    * @param url
    *   the link which returns elements
    * @return
    */
  def applyFilter(url: String, movieDetail: MovieDetail): Element = {
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
    Jsoup.parse(request, getSubtitleUrl).select(".download a").first.absUrl("href")
  }

  /** this function download and unzip subtitle into specified directory
    * @param url
    *   the link we try to download file from it
    */
  def downloadFileInto(url: String, movieDetail: MovieDetail): Unit = {
    val connection = BaseConnection.download(url)

    val is:InputStream = connection match {
      case Right(array) => new ByteArrayInputStream(array)
      case Left(_) =>
        println("Could not download file form subtitle site. you can check it out by bellow link:")
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
