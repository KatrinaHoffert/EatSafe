name := """EatSafe"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.5"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  ws,
  "junit" % "junit" % "4.11",
  "org.seleniumhq.selenium" % "selenium-java" % "2.44.0",
  "org.apache.commons" % "commons-lang3" % "3.3.2",
  "net.sf.opencsv" % "opencsv" % "2.0"
)
