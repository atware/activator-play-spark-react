name    := "play-spark-react"
version := "4.0-SNAPSHOT"

licenses := Seq("Apache-2.0" -> url("http://opensource.org/licenses/Apache-2.0"))

scalaVersion := "2.11.6"

libraryDependencies ++= Dependencies.playSparkReact

enablePlugins(PlayScala)
