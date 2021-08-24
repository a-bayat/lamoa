package pro.sanjagh.lamoa

import pro.sanjagh.lamoa.domain.{Console, ExtractSubtitle}

object Startup {
  def main(args: Array[String]): Unit = {
    Console.printBanner()
    ExtractSubtitle.findSubtitle(args.headOption)
  }
}
