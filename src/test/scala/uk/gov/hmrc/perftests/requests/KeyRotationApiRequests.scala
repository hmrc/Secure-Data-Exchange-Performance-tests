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

package uk.gov.hmrc.perftests.requests

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.request.builder.HttpRequestBuilder
import uk.gov.hmrc.performance.api.APIRequest
import uk.gov.hmrc.performance.api.configuration.APIConfiguration.apiGatewayBaseUrl

import java.time.Instant

object KeyRotationApiRequests extends APIRequest {

  def requestPostKeyAPiUrlRequest(): HttpRequestBuilder = {

    val fortyNineHoursInSeconds: Int = 60 * 60 * 49
    val effectiveDateTime: Instant = Instant.now().plusSeconds(fortyNineHoursInSeconds)
    val srn: String = "561121409291"
    val rotationKey = "be35bf67-0932-4ad8-86d0-56e4f5f3f9f4"
    val informationType = "CNIRegisteredPensionSchemeReliefatsource-RPSRAS"

    val url: String = apiGatewayBaseUrl + "/misc/exchange/customer-encryption-public-key"

    println(s"POST Request key rotation API: $url")

    apiRequest({
      http("POST Request key rotation API")
        .post(apiGatewayBaseUrl + "/misc/exchange/customer-encryption-public-key")
        .body(StringBody(
          """-----BEGIN PGP PUBLIC KEY BLOCK-----
            |
            |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
            |OymV
            |=M/FH""".stripMargin))
        .headers(Map(
          "Authorization" -> "Bearer ${accessToken}",
          "Content-Type" -> "application/pgp-keys",
          "Accept" -> "application/vnd.hmrc.1.0+json",
          "x-effective-date-time" -> s"${effectiveDateTime.toString()}",
          "x-srn" -> s"${srn}",
          "x-key-rotation-key" -> s"${rotationKey}",
          "x-information-type" -> s"${informationType}",
        )
        )
        .check(status.is(201))
    })
  }

}
