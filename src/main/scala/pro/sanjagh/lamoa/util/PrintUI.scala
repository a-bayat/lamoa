package pro.sanjagh.lamoa.util

import scala.io.AnsiColor._

sealed trait PrintUI {
  private[util] def buildOutStream[T](ctx: T, colorArg: String = ""): Unit = {
    println(s"${REVERSED}${BOLD}${colorArg}${ctx}${RESET}")
  }
}

/** This Utility helps to make terminal more colorful
  */
object BrushConsole extends PrintUI {

  def printMessage(ctx: String): Unit = buildOutStream(ctx)

  def printInfoMessage(ctx: String): Unit = buildOutStream(ctx, CYAN)

  def printWarningMessage(ctx: String): Unit = buildOutStream(ctx, YELLOW)

  def printErrorMessage(ctx: String): Unit = buildOutStream(ctx, RED)

  def printSuccessMessage(ctx: String): Unit = buildOutStream(ctx, GREEN)
}
