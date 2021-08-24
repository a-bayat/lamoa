package pro.sanjagh.lamoa.domain

import pro.sanjagh.lamoa.UnitSpec

import scala.util.Right

class ImdbValidatorTest extends UnitSpec {
  import ImdbValidator._

  "Check Imdb Candidate" should {
    "Check functionality" in {
      (getImdbCandidates("World War Z").toSeq.flatten.size) should be > 0
    }
  }
}
