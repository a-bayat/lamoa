package pro.sanjagh.lamoa.domain

import pro.sanjagh.lamoa.UnitSpec

class MovieNameExtractorTest extends UnitSpec {
  import MovieNameExtractor._

  "Standard Movie Name Should Be" in {
    movieNamePurify(
      "The.Avatar.Of.The.Earth"
    ) shouldBe ("The Avatar Of The Earth")
    movieNamePurify(
      "The-Avatar-Of-The-Earth"
    ) shouldBe ("The Avatar Of The Earth")
    movieNamePurify(
      "The_Avatar_Of_The_Earth"
    ) shouldBe ("The Avatar Of The Earth")
    movieNamePurify(
      "The/Avatar-Of+The_Earth"
    ) shouldBe ("The Avatar Of The Earth")
    movieNamePurify("MasterOfShaolin2") shouldBe ("Master Of Shaolin2")
    movieNamePurify("ASingleManIn City") shouldBe ("A Single Man In City")
    movieNamePurify("a.single.man.in.city") shouldBe ("a single man in city")
  }

  "Get Movie Release Year" in {
    findReleaseYear("Beirut.2018.720p.BluRay.x264") shouldBe ("2018")
    findReleaseYear("Petes.Dragon.2016.1080p.BluRay") shouldBe ("2016")
    findReleaseYear("Skyscraper.2018.720p.HDRip.MkvCage.mkv") shouldBe ("2018")
    findReleaseYear("The.Startup.Kids.2012.720p.WEB-DL") shouldBe ("2012")
    findReleaseYear("Tomb.Raider.2018.720p.BluRay.x264") shouldBe ("2018")
    findReleaseYear("World-War-Z-(2013)-[1080p].mp4") shouldBe ("2013")
  }

  "Detect the movie quality" in {
    findMovieQuality("Beirut.2018.720p.BluRay.x264.") shouldBe ("BluRay")
    findMovieQuality("Petes.Dragon.2016.1080p.BluRay") shouldBe ("BluRay")
    findMovieQuality(
      "Skyscraper.2018.720p.HDRip.MkvCage.mkv"
    ) shouldBe ("HDRip")
    findMovieQuality("The.Startup.Kids.2012.720p.WEB-DL") shouldBe ("WEB-DL")
    findMovieQuality("Tomb.Raider.2018.720p.BluRay.x264") shouldBe ("BluRay")
    findMovieQuality("World-War-Z-(2013)-[1080p].mp4") shouldBe ("")
  }

  "Detect the movie resolution" in {
    findMovieResolution("Beirut.2018.720p.BluRay.x264.") shouldBe ("720p")
    findMovieResolution("Petes.Dragon.2016.1080p.BluRay") shouldBe ("1080p")
    findMovieResolution(
      "Skyscraper.2018.720p.HDRip.MkvCage.mkv"
    ) shouldBe ("720p")
    findMovieResolution("The.Startup.Kids.2012.720p.WEB-DL") shouldBe ("720p")
    findMovieResolution("Tomb.Raider.2018.720p.BluRay.x264") shouldBe ("720p")
    findMovieResolution("World-War-Z-(2013)-[1080p].mp4") shouldBe ("1080p")
  }

  "Extract the exact movie name" in {
    cleanedUpMovieName(
      "Beirut.2018.720p.BluRay.x264."
    ) shouldBe ("Beirut (2018)")
    cleanedUpMovieName(
      "Petes.Dragon.2016.1080p.BluRay"
    ) shouldBe ("Petes Dragon (2016)")
    cleanedUpMovieName(
      "Skyscraper.2018.720p.HDRip.MkvCage.mkv"
    ) shouldBe ("Skyscraper (2018)")
    cleanedUpMovieName(
      "The.Startup.Kids.2012.720p.WEB-DL"
    ) shouldBe ("The Startup Kids (2012)")
    cleanedUpMovieName(
      "Tomb.Raider.2018.720p.BluRay.x264"
    ) shouldBe ("Tomb Raider (2018)")
    cleanedUpMovieName(
      "World-War-Z-(2013)-[1080p].mp4"
    ) shouldBe ("World War Z (2013)")
  }

  "Find the most match Item in list" should {
    "Should find the match" in {
      getExactMatchToImdb(
        "Beirut (2018)",
        List(
          "Beirut (2018)",
          "Beirut (2018) TV series",
          "Go To Beirut (2012)"
        )
      ) shouldBe (Some("Beirut (2018)"))
    }
    "Should return None cause the item is not available" in {
      getExactMatchToImdb(
        "Tomb Raider (2018)",
        List(
          "Tomb Cruise (2014)",
          "Tomb Raider TV series",
          "Tomb Raider documentary"
        )
      ) shouldBe (None)
    }
  }
}
