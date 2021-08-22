package pro.sanjagh.lamoa.domain
import java.io.File
import java.nio.file.Files

import scala.annotation.tailrec
import scala.io.StdIn
import scala.util.{Success, Try}
import pro.sanjagh.lamoa.model.NoVideoFileWasFound

object FileOperations2 {
  def getVideoFilesIn(address: Option[String]): Either[NoVideoFileWasFound,List[File]] = {
    val targetFile: File = address match {
      case Some(a) => new File(a)
      case None    => new File(System.getProperty("user.dir"))
    }
    getMediaFiles(targetFile)
  }

  /** get all media file by filtering file list, and if
    * @return
    */
  private def getMediaFiles(file: File): Either[NoVideoFileWasFound, List[File]] = {
    file match {
      case d if d.isDirectory  => Right(file.listFiles().filter(isMediaFile).toList)
      case f if isMediaFile(f) => Right(List(f))
      case _                   => Left(NoVideoFileWasFound(file.getPath()))
    }
  }

  /** Check if file is media type
    * @return
    */
  private[domain] def isMediaFile(file: File): Boolean = {
    getMimeType(file).exists(name => name.startsWith("video"))
  }

  private[domain] def getMimeType(file: File): Option[String] = {
    Option(Files.probeContentType(file.toPath))
  }

  // TODO Test
  private[domain] def removeExtension(file: File): String = {
    file.getName.replaceFirst("[.][^.]+$", "")
  }

}

object FileOperations {

  /** read dir and extract the name of file without extension
    *
    * @return
    *   file name only
    */
  def filePathRead(address: File): File = {
    val files = getMediaFiles(address)
    files match {
      case files if files.size > 1  => files(chooseItem(files))
      case files if files.size == 1 => files.head
      case files if files.isEmpty =>
        println(
          "The path you entered does not contain any file. please enter again."
        )
        System.exit(0)
        ???
    }
  }

  /** Get the index of movie to find subtitle
    * @return
    *   Int
    * @note
    *   db case Success value means directory or media file
    */
  def chooseItem(media: List[File]): Int = {
    println("Available Items: ")
    media.zipWithIndex.foreach { case (elm, idx) =>
      println(s"\t$idx. ${elm.getName}")
    }

    @tailrec
    def recItem: Int = {
      print("Enter the Item index you wish to get subtitle: ")
      Try(StdIn.readInt()) match {
        case Success(n)
            if n >= media
              .indexOf(media.head) && n <= media.indexOf(media.last) =>
          n
        case _ => recItem
      }
    }
    recItem
  }

  /** get all media file by filtering file list, and if
    * @return
    */
  def getMediaFiles(file: File): List[File] = {
    file match {
      case d if d.isDirectory  => file.listFiles().filter(isMediaFile).toList
      case f if isMediaFile(f) => List(f)
      case _                   => List()
    }
  }

  /** Check if file is media type
    * @return
    */
  private[domain] def isMediaFile(file: File): Boolean = {
    getMimeType(file).exists(name => name.startsWith("video"))
  }

  private[domain] def getMimeType(file: File): Option[String] = {
    Option(Files.probeContentType(file.toPath))
  }

  private[domain] def removeExtension(file: File): String = {
    file.getName.replaceFirst("[.][^.]+$", "")
  }
}
