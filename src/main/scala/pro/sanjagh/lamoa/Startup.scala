package pro.sanjagh.lamoa

import com.colofabrix.scala.figlet4s.options.HorizontalLayout
import com.colofabrix.scala.figlet4s.unsafe.{
  FIGureOps,
  Figlet4s,
  OptionsBuilderOps
}
import pro.sanjagh.lamoa.domain.{ExtractSubtitle, MovieFactory}

object Startup {
  private def printBanner(): Unit =
    Figlet4s
      .builder("LAMOA")
      .withHorizontalLayout(HorizontalLayout.FullWidth)
      .render()
      .print()

  def main(args: Array[String]): Unit = {
    printBanner()
    ExtractSubtitle.extract(args.headOption)
  }
}
