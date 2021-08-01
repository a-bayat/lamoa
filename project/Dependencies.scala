import sbt._

object Dependencies {
  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.2.9" % Test
  lazy val slf4j = "org.slf4j" % "slf4j-log4j12" % "1.7.32" % Test
  lazy val figlet = "com.colofabrix.scala" %% "figlet4s-core" % "0.3.0"
}
