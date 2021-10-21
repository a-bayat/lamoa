package pro.sanjagh.lamoa.domain

import com.colofabrix.scala.figlet4s.unsafe.{
  FIGureOps,
  Figlet4s,
  OptionsBuilderOps
}
import pro.sanjagh.lamoa.setting.{AppConfiguration, UserConfiguration}
import pro.sanjagh.lamoa.util.BrushConsole

import scala.io.AnsiColor._
import scala.annotation.tailrec
import scala.io.StdIn
import scala.util.{Random, Success, Try}

sealed trait UI {
  def choose[T](list: List[T], message: String)(f: T => String): T
}

object Console extends UI {

  def choose[T](list: List[T], message: String)(f: T => String): T = {

    def helper: T = {
      BrushConsole.printMessage(message)
      list.zipWithIndex.foreach { case (elm, idx) =>
        println(s"\t${CYAN}$idx: ${f(elm)}")
      }

      @tailrec
      def recItem: Int = {
        print(
          s"${GREEN}${BOLD}Enter the Item index you wish [0-${list.size - 1}]: ${RESET}"
        )

        Try(StdIn.readInt()) match {
          case Success(n) if n >= 0 && n < list.size => n
          case _                                     => recItem
        }
      }
      list(recItem)
    }

    list.size match {
      case s if s > 1  => helper
      case s if s == 1 => list.head
      case s if s == 0 =>
        BrushConsole.printInfoMessage(
          "No particular Video was founded in the specified path!"
        )
        sys.exit
    }
  }

  def printBanner(): Unit =
    Figlet4s
      .builder("LAMOA")
      .render
      .asSeq
      .zipWithIndex
      .foreach { case (line, i) =>
        if (i == 4)
          println(
            s"${BOLD}${MAGENTA}$line ${BLUE}${Random.shuffle(AppConfiguration.getQuoteList).headOption.get}${RESET}"
          )
        else println(s"${MAGENTA}$line${RESET}")
      }
}
