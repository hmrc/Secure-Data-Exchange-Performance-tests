import io.gatling.sbt.GatlingPlugin
import sbt.Keys._
import sbt._
import sbt.plugins.{CorePlugin, IvyPlugin, JvmPlugin}

object PerformanceTestingBuilds extends Build {

  val appName = "performance-testing-template"
  val appVersion = "0.1.0-SNAPSHOT"

  lazy val root = Project(appName, file("."))
    .enablePlugins(GatlingPlugin)
    .enablePlugins(CorePlugin)
    .enablePlugins(JvmPlugin)
    .enablePlugins(IvyPlugin)
    .settings(
      organization := "uk.gov.hmrc",
      version := appVersion,
      scalaVersion := "2.11.12",
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
      libraryDependencies ++= Seq(
        Dependencies.Compile.typesafeConfig,
        Dependencies.Compile.gatlingHighCharts,
        Dependencies.Compile.gatlingTestFramework,
        Dependencies.Compile.performanceTestRunner
      ),
      resolvers ++= Seq(
        Resolver.bintrayRepo("hmrc", "releases"),
        Resolver.typesafeRepo("releases")
      )
    )

}
