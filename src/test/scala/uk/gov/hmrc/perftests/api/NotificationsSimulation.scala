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

import uk.gov.hmrc.performance.api.APIPerformanceTest
import uk.gov.hmrc.performance.api.models.{PerformanceTest, PerformanceTestConfiguration}
import uk.gov.hmrc.perftests.api.NotificationRequests._
import uk.gov.hmrc.perftests.api.TestConfiguration._

import scala.concurrent.duration.{DurationInt, FiniteDuration}

class TransferNotificationsSimulation{


}
/*
class TransferNotificationsSimulation extends APIPerformanceTest {

  import io.gatling.core.Predef._

  override val performanceTest: PerformanceTest = PerformanceTest(
    title = "Secure-Data-Exchange-Notifications",
    apis = exec(postTransferCompleteNotification),
    scope = "write:transfer-complete",
    feeder = "data/transfernotifications.csv"
  )

  val performanceTestConfiguration = PerformanceTestConfiguration(
    duration = testDuration,
    maxLoadPerSecond = maxLoadPerSecond,
    concurrentUsers = concurrentUsers
  )

  runSimulation(false, performanceTestConfiguration)

  override def runSimulation(privileged: Boolean = false, performanceTestConfiguration: PerformanceTestConfiguration): SetUp = {
    val timeoutAtEndOfTest: FiniteDuration = 5 minutes

    super.runSimulation(false, performanceTestConfiguration)
      .maxDuration(rampUpTime + constantRateTime + rampDownTime + timeoutAtEndOfTest)
      .assertions(global.failedRequests.percent.lte(percentageFailureThreshold))

  }

}
*/
