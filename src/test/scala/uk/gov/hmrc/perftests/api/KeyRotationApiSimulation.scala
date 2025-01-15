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
import uk.gov.hmrc.performance.api.models.PerformanceTest
import uk.gov.hmrc.performance.conf.JourneyConfiguration
import uk.gov.hmrc.perftests.requests.KeyRotationApiRequests.{requestDeleteKeyAPiUrlRequest, requestGetSdesPublicKeyApiRequest, requestPostKeyAPiUrlRequest}

class KeyRotationApiSimulation extends APIPerformanceTest with JourneyConfiguration {

  override val performanceTest: PerformanceTest = PerformanceTest(
    title = "Secure-data-exchange-key-rotation-api",
    apis =  exec( exec(requestPostKeyAPiUrlRequest()), exec(requestDeleteKeyAPiUrlRequest()), exec(requestGetSdesPublicKeyApiRequest())),
    scope = "write:transfer-complete", // scope required despite not being used by api
    feeder = "data/helloworld.csv" // use dummy feeder
  )

  runSimulation()
}
