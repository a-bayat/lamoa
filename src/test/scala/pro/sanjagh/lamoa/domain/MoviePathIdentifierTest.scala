package pro.sanjagh.lamoa.domain

import org.jsoup.Jsoup
import org.jsoup.parser.Parser.unescapeEntities
import org.jsoup.select.Elements
import pro.sanjagh.lamoa.UnitSpec
import pro.sanjagh.lamoa.model.NoVideoFileWasFound

import java.io.File

class MoviePathIdentifierTest extends UnitSpec {
  import MoviePathIdentifier._

  "remove the extension of movie name shouldBe equal to" in {
    removeExtension(
      new File("/home/user/The.World.War.Z.2013.1080p.mp4")
    ) shouldBe ("The.World.War.Z.2013.1080p")
  }

  "get the mime type of file" in {
    getMimeType(
      new File("/home/user/The.World.War.Z.2013.1080p.mp4")
    ) shouldBe (Some("video/mp4"))

    getMimeType(
      new File("/home/user/Tomb.Raider.2018.720p.BluRay.x264.avi")
    ) shouldBe (Some("video/x-msvideo"))

    getMimeType(
      new File("/home/user/Hard.Kill.2020.720p.BluRay.x264.mkv")
    ) shouldBe (Some("video/x-matroska"))

    getMimeType(
      new File("/home/user/nothing-else-matters.mp3")
    ) shouldBe (Some("audio/mpeg"))

    getMimeType(
      new File("/home/user/CuteGirl.jpg")
    ) shouldBe (Some("image/jpeg"))
  }

  "check the file is the video file" in {
    isMediaFile(
      new File("/home/user/The.World.War.Z.2013.1080p.mp4")
    ) shouldBe (true)

    isMediaFile(
      new File("/home/user/Tomb.Raider.2018.720p.BluRay.x264.avi")
    ) shouldBe (true)

    isMediaFile(
      new File("/home/user/Hard.Kill.2020.720p.BluRay.x264.mkv")
    ) shouldBe (true)

    isMediaFile(
      new File("/home/user/CuteGirl.jpg")
    ) shouldBe (false)
  }

  "media files in path" should {
    "no file in path equal to NoVideoFileWasFound handler" in {
      getVideoFilesIn(Option("/home/user/media.amir")) shouldBe (
        Left(NoVideoFileWasFound("/home/user/media.amir"))
      )
    }
  }
}
