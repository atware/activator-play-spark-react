import sbt._

object Version {
  val spark          = "1.3.0"
  val requirejs      = "2.1.17"
  val requirejsText  = "2.0.10-3"
  val react          = "0.13.1"
  val jsxRequirejs   = "0.6.0"
  val jquery         = "2.1.4"
  val reactRouter    = "0.13.2"
  val bootstrap      = "3.3.4"
  val reactBootstrap = "0.19.1"
}

object Library {
  val sparkCore   = "org.apache.spark"  %% "spark-core"                  % Version.spark
  val sparkSql    = "org.apache.spark"  %% "spark-sql"                   % Version.spark
  val requireJs   = "org.webjars"        % "requirejs"                   % Version.requirejs
  val requireText = "org.webjars"        % "requirejs-text"              % Version.requirejsText
  val react       = "org.webjars"        % "react"                       % Version.react
  val jsxRequire  = "org.webjars"        % "jsx-requirejs-plugin"        % Version.jsxRequirejs
  val jquery      = "org.webjars"        % "jquery"                      % Version.jquery
  val reactRouter = "org.webjars"        % "react-router"                % Version.reactRouter
  val bootstrap   = "org.webjars"        % "bootstrap"                   % Version.bootstrap
  val reactBoot   = "org.webjars"        % "react-bootstrap"             % Version.reactBootstrap
}

object Dependencies {

  import Library._

  val playSparkReact = List (
    sparkCore,
    sparkSql,
    requireJs,
    requireText,
    react,
    jsxRequire,
    jquery,
    reactRouter,
    bootstrap exclude("org.webjars", "jquery"),
    reactBoot
  )
}
