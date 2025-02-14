/*
 * Copyright 2025 HM Revenue & Customs
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

import io.gatling.core.Predef.exec
import uk.gov.hmrc.performance.api.APIPerformanceTest
import uk.gov.hmrc.performance.api.models.{PerformanceTest, PerformanceTestConfiguration}
import uk.gov.hmrc.performance.conf.JourneyConfiguration
import uk.gov.hmrc.perftests.api.MiscApiRequests.postThirdPartyMiscAPiUrlRequest

class MiscApiFileUploadSimulation extends APIPerformanceTest with JourneyConfiguration {

  override val performanceTest: PerformanceTest = PerformanceTest(
    title = "Secure-data-exchange-service-misc-api",
    apis = exec(postThirdPartyMiscAPiUrlRequest),
    scope = "write:transfer-complete",
    feeder = "data/miscApiFeeder.csv"
  )

  override protected def runSimulation(privileged: Boolean = false, performanceTestConfiguration: PerformanceTestConfiguration): SetUp = {
    if (journeysAvailable.contains(performanceTest.title)) {
      super.runSimulation()
    } else {
      setUp(Seq.empty: _*)
    }
  }

  runSimulation()
}
