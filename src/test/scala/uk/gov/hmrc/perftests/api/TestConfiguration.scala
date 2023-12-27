/*
 * Copyright 2023 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.perftests.api

import uk.gov.hmrc.performance.api.configuration.Configuration

import scala.concurrent.duration.{DurationInt, FiniteDuration}

object TestConfiguration extends Configuration {
  lazy val feeder = "data/feeder.csv"

  lazy val loadPercentage: Long = readProperty("perftest.loadPercentage", "100").toLong / 100L
  lazy val testDuration: FiniteDuration = readProperty("perftest.durationInSeconds", "60").toInt.seconds
  lazy val maxLoadPerSecond: Int = readProperty("perftest.maxLoadPerSecond", "30").toInt
  lazy val concurrentUsers: Int = readProperty("perftest.concurrentUsers", "2").toInt
  lazy val rampUpDuration: Int = readProperty("perftest.rampUpDuration", "10").toInt

  import scala.concurrent.duration._

  lazy val rampUpTime: FiniteDuration             = readProperty("perftest.rampupTime", "1").toInt minutes
  lazy val rampDownTime: FiniteDuration           = readProperty("perftest.rampdownTime", "1").toInt minutes
  lazy val constantRateTime: FiniteDuration       = readProperty("perftest.constantRateTime", "1").toInt minutes
  lazy val runSingleUserJourney: Boolean          = readProperty("perftest.runSmokeTest", "true").toBoolean
  lazy val percentageFailureThreshold: Int        = readProperty("perftest.percentageFailureThreshold", "1").toInt
  lazy val requestPercentageFailureThreshold: Int =
    readProperty("perftest.requestPercentageFailureThreshold", "1").toInt
}