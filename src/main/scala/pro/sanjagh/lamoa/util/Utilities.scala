package pro.sanjagh.lamoa.util

import scala.language.postfixOps
import scala.math.min

object Utilities {

  def levenshtein[A](a: Iterable[A], b: Iterable[A]): Int = {
    val startRow = (0 to b.size).toList
    a.foldLeft(startRow) { (prevRow, aElem) =>
      (prevRow.zip(prevRow.tail).zip(b)).scanLeft(prevRow.head + 1) {
        case (left, ((diag, up), bElem)) => {
          val aGapScore = up + 1
          val bGapScore = left + 1
          val matchScore = diag + (if (aElem == bElem) 0 else 1)
          List(aGapScore, bGapScore, matchScore).min
        }
      }
    }.last
  }
}
