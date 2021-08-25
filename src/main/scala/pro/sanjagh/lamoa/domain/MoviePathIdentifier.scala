package pro.sanjagh.lamoa.domain
import java.io.File
import java.nio.file.Files

import scala.annotation.tailrec
import scala.io.StdIn
import scala.util.{Success, Try}
import pro.sanjagh.lamoa.model.NoVideoFileWasFound

object MoviePathIdentifier {

  /** Just return the video files in the specified path
    * @param path
    *   the address we wish to probe into
    * @return
    *   if any video available in path return unless return NoVideoFileWasFound
    */
  /* TODO => Left Tested but for right we have no idea about returned data*/
  def getVideoFilesIn(
      path: Option[String]
  ): Either[NoVideoFileWasFound, List[File]] = {
    val targetFile: File = path match {
      case Some(a) => new File(a)
      case None    => new File(System.getProperty("user.dir"))
    }
    getMediaFiles(targetFile)
  }

  /** get all media file by filtering file list, and if
    * @return
    */
  /* TODO => How I Can test this function because it should probe inside a directory and return a list and we don't
   *   have any idea what it should contain */
  def getMediaFiles(
      file: File
  ): Either[NoVideoFileWasFound, List[File]] = {
    file match {
      case d if d.isDirectory =>
        Right(file.listFiles().filter(isMediaFile).toList)
      case f if isMediaFile(f) => Right(List(f))
      case _                   => Left(NoVideoFileWasFound(file.getPath))
    }
  }

  /** Check if file is media type
    * @return
    */
  def isMediaFile(file: File): Boolean = {
    getMimeType(file).exists(name => name.startsWith("video"))
  }

  def getMimeType(file: File): Option[String] = {
    Option(Files.probeContentType(file.toPath))
  }

  /** omit movie name extension
    * @param file
    *   the name of file
    * @return
    *   file name without extension
    */
  def removeExtension(file: File): String = {
    file.getName.replaceFirst("[.][^.]+$", "")
  }
}
