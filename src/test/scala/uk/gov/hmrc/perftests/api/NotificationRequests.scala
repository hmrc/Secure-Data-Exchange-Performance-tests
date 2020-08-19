package uk.gov.hmrc.perftests.api

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmrc.performance.api.APIRequest
import uk.gov.hmrc.performance.api.configuration.APIConfiguration.apiGatewayBaseUrl

object NotificationRequests extends APIRequest {

  val postTransferCompleteNotification = apiRequest({
    http("POST transfer complete notification")
      .post(apiGatewayBaseUrl + "/organisations/notification/files/transfer/complete/${srn}")
      .body(RawFileBody("data/transfer-complete-notification.${type}"))
      .headers(Map(
        "Authorization" -> "Bearer ${accessToken}",
        "Accept" -> "application/vnd.hmrc.1.0+${type}",
        "Content-Type" -> "application/${type}"))
      .check(status.is(204))
  })

}
