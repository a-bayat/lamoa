import sbt._

object Dependencies {
  lazy val slf4j = "org.slf4j" % "slf4j-log4j12" % "1.7.32" % Test
  lazy val figlet = "com.colofabrix.scala" %% "figlet4s-core" % "0.3.0"
  lazy val sttp = "com.softwaremill.sttp.client3" %% "core" % "3.3.13"
  lazy val jsoup = "org.jsoup" % "jsoup" % "1.14.1"
  lazy val typesafeConfig = "com.typesafe" % "config" % "1.4.1"
  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.2.8" % Test
}
