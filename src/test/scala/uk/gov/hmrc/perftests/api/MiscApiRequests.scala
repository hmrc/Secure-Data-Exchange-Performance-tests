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

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmrc.performance.api.APIRequest
import uk.gov.hmrc.performance.api.configuration.APIConfiguration.apiGatewayBaseUrl

object MiscApiRequests extends APIRequest {

  val postThirdPartyMiscAPiUrlRequest = apiRequest({
    http("POST Create third-party upload URL's")
      .post(apiGatewayBaseUrl + "/misc/sdes-file-upload/files/upload/url/${srn}/${filetype}")
      .body(StringBody(
        """[
           |  {
           |    "filename": "${srn}.csv",
           |    "metadata": [
           |                  {
           |                    "key": "${key1}",
           |                    "value": "${value1}"
           |                  },
           |                  {
           |                    "key": "${key2}",
           |                    "value": "${value2}"
           |                  }
           |               ]
           |   }
           |]
           |          """.stripMargin))
      .headers(Map(
        "Authorization" -> "Bearer ${accessToken}",
        "Accept" -> "application/vnd.hmrc.1.0+json",
        "Content-Type" -> "application/json"))
      .check(status.is(201))
  })

}
