package pro.sanjagh.lamoa.domain

import scala.util.matching.Regex

object StringExtractor {
  private val pattern: Regex = {
    Configuration.get.getConfig("config").getString("regex_pattern").r
  }
  def standardString(str: String): String = {
    pattern.findAllIn(str).foldLeft("") { (a, b) => a + " " + b }.trim
  }
}
