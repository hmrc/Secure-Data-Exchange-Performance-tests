import sbt.Keys._
import sbt._
import sbt.plugins.{CorePlugin, IvyPlugin, JvmPlugin}

lazy val appName = "secure-data-exchange-performance-tests"
lazy val appVersion = "0.1.0-SNAPSHOT"

val gatlingVersion = "2.2.5"

lazy val appDependencies = Seq(
  "io.gatling" % "gatling-test-framework" % gatlingVersion,
  "io.gatling.highcharts" % "gatling-charts-highcharts" % gatlingVersion,
  "com.typesafe" % "config" % "1.3.1",
  "uk.gov.hmrc" %% "api-performance-test-runner" % "1.32.0",
  "uk.gov.hmrc" %% "performance-test-runner" % "3.5.0"
)

lazy val microservice = Project(appName, file("."))
  .enablePlugins(GatlingPlugin, CorePlugin, JvmPlugin, IvyPlugin)
  .disablePlugins(JUnitXmlReportPlugin)
  .settings(
    organization := "uk.gov.hmrc",
    version := appVersion,
    scalaVersion := "2.12.11",
    scalacOptions ++= Seq(
      "-unchecked",
      "-deprecation",
      "-Xlint",
      "-language:_",
      "-target:jvm-1.8",
      "-Xmax-classfile-name", "100",
      "-encoding", "UTF-8"
    ),
    retrieveManaged := true,
    initialCommands in console := "import uk.gov.hmrc._",
    parallelExecution in Test := false,
    publishArtifact in Test := true,
    libraryDependencies ++= appDependencies,
    resolvers ++= Seq(
      "hmrc-releases" at "https://artefacts.tax.service.gov.uk/artifactory/hmrc-releases/",
      Resolver.bintrayRepo("hmrc", "releases"),
      Resolver.typesafeRepo("releases"),
      Resolver.jcenterRepo
    )
  )
