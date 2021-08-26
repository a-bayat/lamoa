package pro.sanjagh.lamoa.domain

import com.colofabrix.scala.figlet4s.options.HorizontalLayout
import com.colofabrix.scala.figlet4s.unsafe.{
  FIGureOps,
  Figlet4s,
  OptionsBuilderOps
}

import scala.annotation.tailrec
import scala.io.StdIn
import scala.util.{Success, Try}

sealed trait UI {
  def choose[T](list: List[T], message: String)(f: T => String): T
}

object Console extends UI {

  def choose[T](list: List[T], message: String)(f: T => String): T = {

    def helper: T = {
      println(message)
      list.zipWithIndex.foreach { case (elm, idx) =>
        println(s"\t$idx: ${f(elm)}")
      }

      @tailrec
      def recItem: Int = {
        print("Enter the Item index you wish: ")
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
        println("No particular Video founded in specified path!")
        sys.exit
    }
  }

  def printBanner(): Unit =
    Figlet4s
      .builder("LAMOA")
      .withHorizontalLayout(HorizontalLayout.FullWidth)
      .render()
      .print()
}