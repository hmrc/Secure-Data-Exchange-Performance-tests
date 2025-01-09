import sbt.Keys._
import sbt._

lazy val appName = "secure-data-exchange-performance-tests"
lazy val appVersion = "0.1.0-SNAPSHOT"

lazy val appDependencies = Seq(
  "com.fasterxml.jackson.module" %% "jackson-module-scala"        % "2.14.2",
  "uk.gov.hmrc"                  %% "api-performance-test-runner" % "1.52.0",
  "uk.gov.hmrc" %% "performance-test-runner" % "5.5.0" % Test // Due to api-performance-test-runner only supporting gatling core 3.5.1, cannot go higher than 5.5.0
)

lazy val microservice = Project(appName, file("."))
  .enablePlugins(GatlingPlugin)
//  .enablePlugins(SbtAutoBuildPlugin)
  .settings(
    organization              := "uk.gov.hmrc",
    version                   := appVersion,
    scalaVersion              := "2.13.12",
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
