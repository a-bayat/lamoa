import Dependencies._

ThisBuild / scalaVersion := "2.13.6"
ThisBuild / version := "1.0.0"
ThisBuild / organization := "pro.sanjagh"
ThisBuild / organizationName := "sanjagh"

name := "lamoa"
libraryDependencies += scalaTest
libraryDependencies += slf4j
libraryDependencies += figlet
libraryDependencies += sttp
libraryDependencies += jsoup
libraryDependencies += typesafeConfig

enablePlugins(JavaAppPackaging, UniversalPlugin, RpmPlugin, WindowsPlugin)

// sbt sbt Native Packager General Configuration
maintainer := "Amir <amirhossein.bayyat@gmail.com>"
description := "Movie Subtitle Downloader"


// sbt Native Packager RPM configuration
rpmVendor:= "typesafe"
rpmLicense := Some("Apache License")

