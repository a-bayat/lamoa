package pro.sanjagh.lamoa

import pro.sanjagh.lamoa.domain.{ExtractSubtitle, MovieFactory}

object Startup {

  def main(args: Array[String]): Unit = {
    Console.printBanner()
    ExtractSubtitle.extract(args.headOption)
  }
}
