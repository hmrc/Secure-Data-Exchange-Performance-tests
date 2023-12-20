import sbt.Keys._
import sbt._
import sbt.plugins.{CorePlugin, IvyPlugin, JvmPlugin}

lazy val appName = "secure-data-exchange-performance-tests"
lazy val appVersion = "0.1.0-SNAPSHOT"

val gatlingVersion = "3.5.0"

lazy val appDependencies = Seq(
  "io.gatling" % "gatling-test-framework" % gatlingVersion,
  "io.gatling.highcharts" % "gatling-charts-highcharts" % gatlingVersion,
  "com.typesafe"          % "config"                    % "1.4.2",
  "com.fasterxml.jackson.module" %% "jackson-module-scala"        % "2.14.2",
  "uk.gov.hmrc"                  %% "api-performance-test-runner" % "1.44.0",
  "uk.gov.hmrc" %% "performance-test-runner" % "5.5.0"
)

lazy val microservice = Project(appName, file("."))
  .enablePlugins(GatlingPlugin)
  .enablePlugins(SbtAutoBuildPlugin)
  .settings(
    organization              := "uk.gov.hmrc",
    version                   := appVersion,
    scalaVersion              := "2.13.8",
    scalacOptions ++= Seq(
      "-feature",
      "-language:implicitConversions",
      "-language:postfixOps"
    ),
    retrieveManaged           := true,
    console / initialCommands := "import uk.gov.hmrc._",
    Test / parallelExecution  := false,
    Test / publishArtifact    := true,
    Test / testOptions        := Seq.empty,
    libraryDependencies ++= appDependencies
  )
