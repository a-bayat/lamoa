import Dependencies._

ThisBuild / scalaVersion := "2.13.6"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "pro.sanjagh"
ThisBuild / organizationName := "sanjagh"

lazy val root = (project in file("."))
  .settings(
    name := "lamoa",
    libraryDependencies += scalaTest,
    libraryDependencies += slf4j,
    libraryDependencies += figlet,
    libraryDependencies += sttp,
    libraryDependencies += jsoup,
    libraryDependencies += typesafeConfig
  )
