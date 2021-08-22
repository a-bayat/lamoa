package pro.sanjagh.lamoa

import java.io.File
import scala.annotation.tailrec
import scala.util.Try
import scala.io.StdIn
import scala.util.Success

import com.colofabrix.scala.figlet4s.options.HorizontalLayout
import com.colofabrix.scala.figlet4s.unsafe.{
  FIGureOps,
  Figlet4s,
  OptionsBuilderOps
}

sealed trait UI {
  def choose[T](list: List[T], message: String)(f: T => String): T
}

object Console extends UI {

  def choose[T](list: List[T], message: String)(f: T => String): T = {
    println(message)
    list.zipWithIndex.foreach { case (elm, idx) =>
      println(s"\t$idx: ${f(elm)}")
    }

    @tailrec
    def recItem: Int = {
      print("Enter the Item index you wish:")
      Try(StdIn.readInt()) match {
        case Success(n) if n >= 0 && n < list.size =>
          n
        case _ => recItem
      }
    }
    list(recItem)
  }

  def printBanner(): Unit =
    Figlet4s
      .builder("LAMOA")
      .withHorizontalLayout(HorizontalLayout.FullWidth)
      .render()
      .print()
}
