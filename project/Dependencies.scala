import sbt._

object Dependencies {

  private val gatlingVersion = "2.2.5"

  object Compile {
    val typesafeConfig = "com.typesafe" % "config" % "1.3.1"
    val performanceTestRunner = "uk.gov.hmrc" %% "performance-test-runner" % "3.1.0"
    val gatlingTestFramework = "io.gatling" % "gatling-test-framework" % gatlingVersion
    val gatlingHighCharts = "io.gatling.highcharts" % "gatling-charts-highcharts" % gatlingVersion
  }
}


