name    := "play-spark-react"
version := "5.0"

licenses := Seq("Apache-2.0" -> url("http://opensource.org/licenses/Apache-2.0"))

scalaVersion := "2.11.7"

resolvers ++= Seq(
  "Sonatype OSS Releases"  at "http://oss.sonatype.org/content/repositories/releases/"
)

libraryDependencies ++= Dependencies.playSparkReact
libraryDependencies ~= { _.map(_.exclude("org.slf4j", "slf4j-log4j12")) }
dependencyOverrides ++= Set(
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.4.4"
)

enablePlugins(PlayScala)

fork in run := true