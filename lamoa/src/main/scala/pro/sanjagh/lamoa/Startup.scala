package pro.sanjagh.lamoa

import com.colofabrix.scala.figlet4s.options.HorizontalLayout
import com.colofabrix.scala.figlet4s.unsafe.{FIGureOps, Figlet4s, OptionsBuilderOps}

object Startup extends App {
  private def Banner(): Unit =
    Figlet4s
      .builder("LAMOA")
      .withHorizontalLayout(HorizontalLayout.FullWidth)
      .render()
      .print()

  Banner()
}
