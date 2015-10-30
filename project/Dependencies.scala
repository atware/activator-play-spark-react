import sbt._

object Version {
  val spark = "1.5.1"
  val requirejs = "2.1.20"
  val requirejsText = "2.0.14"
  val react = "0.14.0"
  val jsxRequirejs = "0.6.0"
  val jquery = "2.1.4"
  val reactRouter = "0.13.2"
  val bootstrap = "3.3.5"
  val reactBootstrap = "0.25.1"
  val scalaTest = "2.2.5"
  val scalaTestPlus = "1.4.0-M4"
  val slf4japi = "1.7.12"
  val slf4jsimple = "1.7.12"
  val h2 = "1.4.190"
  val scalalike = "2.2.9"
  val ficus = "1.1.2"
}

object Library {
  val sparkCore = "org.apache.spark" %% "spark-core" % Version.spark
  val sparkSql = "org.apache.spark" %% "spark-sql" % Version.spark
  val scalaTest = "org.scalatest" %% "scalatest" % Version.scalaTest % "test"
  val scalaTestPlus = "org.scalatestplus" %% "play" % Version.scalaTestPlus
  val slf4japi = "org.slf4j" % "slf4j-api" % Version.slf4japi % "provided"
  val slf4jnop = "org.slf4j" % "slf4j-nop" % Version.slf4jsimple % "test"
  val scalalike = "org.scalikejdbc" %% "scalikejdbc" % Version.scalalike
  val h2 = "com.h2database"  %  "h2" % Version.h2
  val ficus = "net.ceedubs" %% "ficus" % Version.ficus

  // webjar related dependencies
  val requireJs = "org.webjars" % "requirejs" % Version.requirejs
  val requireText = "org.webjars" % "requirejs-text" % Version.requirejsText
  val react = "org.webjars" % "react" % Version.react
  val jsxRequire = "org.webjars" % "jsx-requirejs-plugin" % Version.jsxRequirejs
  val jquery = "org.webjars" % "jquery" % Version.jquery
  val reactRouter = "org.webjars" % "react-router" % Version.reactRouter
  val bootstrap = "org.webjars" % "bootstrap" % Version.bootstrap
  val reactBoot = "org.webjars" % "react-bootstrap" % Version.reactBootstrap
}

object Dependencies {

  import Library._

  val playSparkReact = List(
    sparkCore,
    sparkSql,
    requireJs,
    requireText,
    react,
    jsxRequire,
    jquery,
    reactRouter,
    bootstrap exclude("org.webjars", "jquery"),
    reactBoot,
    scalaTest,
    scalaTestPlus,
    scalalike,
    h2,
    ficus
  )
}
