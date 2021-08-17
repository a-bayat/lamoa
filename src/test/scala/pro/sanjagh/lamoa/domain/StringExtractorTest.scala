package pro.sanjagh.lamoa.domain

import pro.sanjagh.lamoa.UnitSpec

class StringExtractorTest extends UnitSpec {
  import StringExtractor._

  "Standard File Name Should Be" in {
    standardString("The.Avatar.Of.The.Earth") should be("The Avatar Of The Earth")
    standardString("The-Avatar-Of-The-Earth") should be("The Avatar Of The Earth")
    standardString("The_Avatar_Of_The_Earth") should be("The Avatar Of The Earth")
    standardString("The/Avatar-Of+The_Earth") should be("The Avatar Of The Earth")
    standardString("MasterOfShaolin2") should be("Master Of Shaolin2")
    standardString("ASingleManIn City") should be("A Single Man In City")
    standardString("a.single.man.in.city") should be("a single man in city")
  }
}
