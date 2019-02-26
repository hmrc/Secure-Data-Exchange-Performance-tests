package uk.gov.hmrc.perftests.requests

import uk.gov.hmrc.performance.conf.ServicesConfiguration
import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._
import io.gatling.http.request.builder.{HttpAttributes, HttpRequestBuilder}
import uk.gov.hmrc.perftests.requests.Requests.{baseUrl, strideBaseUrl}
import utils.RequestUtils

object Requests extends ServicesConfiguration {

  val baseUrl = "https://admin.staging.tax.service.gov.uk/sdes/dashboard"
  val strideBaseUrl = baseUrlFor("stride-auth")

  private def savePageItem(name: String, pattern: String) = regex(_ => pattern).saveAs(name)

  //  val csrfPattern = """<input type="hidden" name="csrfToken" value="([^"]+)""""
  val relayStatePattern =
    """<input type="hidden" id="RelayState" name="RelayState" value="([^"]+)""""
  val samlResponsePattern = """<input type="hidden" name="SAMLResponse" value="([^"]+)""""
  val formUrlPattern = """<form action="([^"]+)"""


  def getLoginPage: HttpRequestBuilder = {
    http("Navigate to auth login stub page")
      .get(s"https://admin.staging.tax.service.gov.uk/stride/sign-in?successURL=%2Fsdes%2Fdashboard&origin=secure-data-exchange-management-frontend")
      .check(status.is(303))
    //      .check(RequestUtils.saveCsrfToken)
    //      .check(header("location").saveAs("authRequestRedirect"))
    //      .check(header("Location").is(s"$baseUrl/stride-idp-stub/auth-request": String))
  }

  def postStrideAuthPage: HttpRequestBuilder = {
    http("[POST] IdP stub login form")
      .post(s"https://admin.staging.tax.service.gov.uk/stride-idp-stub/sign-in")
      .disableFollowRedirect
      //.formParam("csrfToken", s"$${csrfToken}")
      .formParam("RelayState", "successURL=%2Fsdes%2Fdashboard&failureURL=%2Fstride%2Ffailure%3FcontinueURL%3D%25252Fsdes%25252Fdashboard")
      .formParam("pid", "123")
      .formParam("usersGivenName", "Test")
      .formParam("usersSurname", "Test")
      .formParam("emailAddress", "Test@gmail.com")
      .formParam("status", true)
      .formParam("signature", "valid")
      .formParam("roles", "HMRC-SDES-REUESTOR")
      .check(status.is(303))
      .check(header("location").saveAs("signInRedirect"))
  }

  def postSamlResponse: HttpRequestBuilder = {
    http("Post SAML response")
      .post("https://admin.staging.tax.service.gov.uk/stride/auth-response")
      .formParam("SAMLResponse", "PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiPz4KPHNhbWwycDpSZXNwb25zZSBJRD0iMTNhNWJjNjctZWNlOS00MTAwLWJjZGUtM2U3ZWY3YTlhNDc2IiBJc3N1ZUluc3RhbnQ9IjIwMTktMDItMTlUMTA6NTM6MDIuNDQ2WiIgVmVyc2lvbj0iMi4wIiB4bWxuczpzYW1sMnA9InVybjpvYXNpczpuYW1lczp0YzpTQU1MOjIuMDpwcm90b2NvbCI+PHNhbWwyOklzc3VlciB4bWxuczpzYW1sMj0idXJuOm9hc2lzOm5hbWVzOnRjOlNBTUw6Mi4wOmFzc2VydGlvbiI+aHR0cDovL2ZzLmVtYWN0ZXN0LmNvbS9hZGZzL3NlcnZpY2VzL3RydXN0PC9zYW1sMjpJc3N1ZXI+PHNhbWwycDpTdGF0dXM+PHNhbWwycDpTdGF0dXNDb2RlIFZhbHVlPSJ1cm46b2FzaXM6bmFtZXM6dGM6U0FNTDoyLjA6c3RhdHVzOlN1Y2Nlc3MiLz48L3NhbWwycDpTdGF0dXM+PHNhbWwyOkFzc2VydGlvbiBWZXJzaW9uPSIyLjAiIHhtbG5zOnNhbWwyPSJ1cm46b2FzaXM6bmFtZXM6dGM6U0FNTDoyLjA6YXNzZXJ0aW9uIj48c2FtbDI6SXNzdWVyPmh0dHA6Ly9mcy5lbWFjdGVzdC5jb20vYWRmcy9zZXJ2aWNlcy90cnVzdDwvc2FtbDI6SXNzdWVyPjxkczpTaWduYXR1cmUgeG1sbnM6ZHM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvMDkveG1sZHNpZyMiPgo8ZHM6U2lnbmVkSW5mbz4KPGRzOkNhbm9uaWNhbGl6YXRpb25NZXRob2QgQWxnb3JpdGhtPSJodHRwOi8vd3d3LnczLm9yZy8yMDA2LzEyL3htbC1jMTRuMTEiLz4KPGRzOlNpZ25hdHVyZU1ldGhvZCBBbGdvcml0aG09Imh0dHA6Ly93d3cudzMub3JnLzIwMDEvMDQveG1sZHNpZy1tb3JlI3JzYS1zaGE1MTIiLz4KPGRzOlJlZmVyZW5jZSBVUkk9IiI+CjxkczpUcmFuc2Zvcm1zPgo8ZHM6VHJhbnNmb3JtIEFsZ29yaXRobT0iaHR0cDovL3d3dy53My5vcmcvMjAwMC8wOS94bWxkc2lnI2VudmVsb3BlZC1zaWduYXR1cmUiLz4KPGRzOlRyYW5zZm9ybSBBbGdvcml0aG09Imh0dHA6Ly93d3cudzMub3JnLzIwMDEvMTAveG1sLWV4Yy1jMTRuIyIvPgo8L2RzOlRyYW5zZm9ybXM+CjxkczpEaWdlc3RNZXRob2QgQWxnb3JpdGhtPSJodHRwOi8vd3d3LnczLm9yZy8yMDAxLzA0L3htbGVuYyNzaGEyNTYiLz4KPGRzOkRpZ2VzdFZhbHVlPmhxUE9uMWlHbTYvR0FOV3hRTVhEUEhrWDBEVzQ4LzRqWXJOQ3J3dUtsUE09PC9kczpEaWdlc3RWYWx1ZT4KPC9kczpSZWZlcmVuY2U+CjwvZHM6U2lnbmVkSW5mbz4KPGRzOlNpZ25hdHVyZVZhbHVlPgpnZE4yN3pQcEpzT3YvWEsrcE5ZMk5mWjNMMjR4MWNtUkUrRDJheHdhVWxLRW9QZnc0eXBsdFppS1p4aE43OHNkS25zaCs3TmtIblZVCkpvK3JwM2grL0V5eEdKWHFPSElOZUx2MlRJdTlkdmRYMlJ5elhHb1NsRzRRbExRaDNuT3FicTRkSDlZWXBQWlN1bzN1RllnRFkrTWUKSkV5UkRKUnRjT3pQNWNEU1NpblBvRm5KUlBhNXc2NTUyM2RFQUhMeXJhSG5PS210VzFoQTk2OXZsUDhXdFVOaU4xbG9ETmdUbWlWVQpTSldBNnhabHROaXQ1RDZHdzJVMHhmZDZhRFl6WFF0T3lhYkVrWkhUV3JyTkNDUldMLysvTGRZZ0hQYWpOalhId0ZDaStZRDZrSk5KCml5QVNQa1RlaUJZTkZ3S1MzdXAwQ2tXWWl6bEwwMkp0Z1pCV1hRPT0KPC9kczpTaWduYXR1cmVWYWx1ZT4KPC9kczpTaWduYXR1cmU+PHNhbWwyOlN1YmplY3Q+PHNhbWwyOlN1YmplY3RDb25maXJtYXRpb24gTWV0aG9kPSJ1cm46b2FzaXM6bmFtZXM6dGM6U0FNTDoyLjA6Y206YmVhcmVyIj48c2FtbDI6U3ViamVjdENvbmZpcm1hdGlvbkRhdGEgSW5SZXNwb25zZVRvPSJNRFRQLTIxYzc3YmQ3LWIyZWItNGZmYy04YjVkLTMyYWI1NGRlMjE1MiIgTm90T25PckFmdGVyPSIyMDE5LTAyLTE5VDEwOjUzOjAyLjQ0N1oiIFJlY2lwaWVudD0iaHR0cHM6Ly93d3cudGF4LnNlcnZpY2UuZ292LnVrL3N0cmlkZS9hdXRoLXJlc3BvbnNlIi8+PC9zYW1sMjpTdWJqZWN0Q29uZmlybWF0aW9uPjwvc2FtbDI6U3ViamVjdD48c2FtbDI6QXV0aG5TdGF0ZW1lbnQgU2Vzc2lvbkluZGV4PSJhOTlkMmQ4Zi0zYmY3LTQ2YTktODFkMC1lYjQ0MWQ1YjljNmQiLz48c2FtbDI6QXR0cmlidXRlU3RhdGVtZW50PjxzYW1sMjpBdHRyaWJ1dGUgTmFtZT0iaHR0cDovL3NjaGVtYXMueG1sc29hcC5vcmcvd3MvMjAwNS8wNS9pZGVudGl0eS9jbGFpbXMvbmFtZSI+PHNhbWwyOkF0dHJpYnV0ZVZhbHVlPjEyMzwvc2FtbDI6QXR0cmlidXRlVmFsdWU+PC9zYW1sMjpBdHRyaWJ1dGU+PHNhbWwyOkF0dHJpYnV0ZSBOYW1lPSJodHRwOi8vc2NoZW1hcy54bWxzb2FwLm9yZy93cy8yMDA1LzA1L2lkZW50aXR5L2NsYWltcy9naXZlbm5hbWUiPjxzYW1sMjpBdHRyaWJ1dGVWYWx1ZT50ZXN0PC9zYW1sMjpBdHRyaWJ1dGVWYWx1ZT48L3NhbWwyOkF0dHJpYnV0ZT48c2FtbDI6QXR0cmlidXRlIE5hbWU9Imh0dHA6Ly9zY2hlbWFzLnhtbHNvYXAub3JnL3dzLzIwMDUvMDUvaWRlbnRpdHkvY2xhaW1zL3N1cm5hbWUiPjxzYW1sMjpBdHRyaWJ1dGVWYWx1ZT50ZXN0czwvc2FtbDI6QXR0cmlidXRlVmFsdWU+PC9zYW1sMjpBdHRyaWJ1dGU+PHNhbWwyOkF0dHJpYnV0ZSBOYW1lPSJodHRwOi8vc2NoZW1hcy54bWxzb2FwLm9yZy93cy8yMDA1LzA1L2lkZW50aXR5L2NsYWltcy9lbWFpbGFkZHJlc3MiPjxzYW1sMjpBdHRyaWJ1dGVWYWx1ZT5UZXN0QGdtYWlsLmNvbTwvc2FtbDI6QXR0cmlidXRlVmFsdWU+PC9zYW1sMjpBdHRyaWJ1dGU+PHNhbWwyOkF0dHJpYnV0ZSBOYW1lPSJodHRwOi8vc2NoZW1hcy5taWNyb3NvZnQuY29tL3dzLzIwMDgvMDYvaWRlbnRpdHkvY2xhaW1zL3JvbGUiPjxzYW1sMjpBdHRyaWJ1dGVWYWx1ZT5ITVJDLVNERVMtUkVRVUVTVE9SPC9zYW1sMjpBdHRyaWJ1dGVWYWx1ZT48L3NhbWwyOkF0dHJpYnV0ZT48L3NhbWwyOkF0dHJpYnV0ZVN0YXRlbWVudD48L3NhbWwyOkFzc2VydGlvbj48L3NhbWwycDpSZXNwb25zZT4=")
      .formParam("RelayState", "successURL=%2Fsdes%2Fdashboard&failureURL=%2Fstride%2Ffailure%3FcontinueURL%3D%25252Fsdes%25252Fdashboard")
      .check(status.is(303))
  }

  def getSdesLandingPage: HttpRequestBuilder = {
    http("Navigate to Sdes Landing Page")
      .get(s"$baseUrl/sdes/dashboard")
      .check(status.is(200))
      .check(RequestUtils.saveCsrfToken)
      .check(regex("Requestor dashboard").exists)
  }

  def postCreateDER: HttpRequestBuilder = {
    http("Create DER")
      .post(s"$baseUrl/sdes/dashboard/requestor/create-der")
      .formParam("csrfToken", "${csrfToken}")
      .check(status.is(303))
      .check(header("Location").is(s"/sdes/create-request/select-request-type":String))
  }

  def getRequestType: HttpRequestBuilder = {
    http("get Request type")
      .get(s"$baseUrl/sdes/create-request/select-request-type")
      .check(status.is(200))
      .check(RequestUtils.saveCsrfToken)
    .check(regex("Create a new data exchange request").exists)

    }

  def postRequestType: HttpRequestBuilder = {
    http("post Request type")
      .post(s"$baseUrl/sdes/create-request/select-request-type")
      .formParam("request-type", "electronic")
//      .formParam("type", "submit")
      .formParam("csrfToken", "${csrfToken}")
      .check(status.is(303))
      .check(header("Location").is(s"/sdes/create-request/select-template":String))


  }

  def getSelectTemplate: HttpRequestBuilder = {
    http("TemplateType")
      .get(s"$baseUrl/sdes/create-request/select-template")
      .check(RequestUtils.saveCsrfToken)
      .check(status.is(200))
      .check(regex("Select an information type").exists)

  }
  def postSelectTemplate: HttpRequestBuilder = {
    http("TemplateType")
      .post(s"$baseUrl/sdes/create-request/select-template")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("data-exchange-template", "businessIntel_;_Business Intelligence to MoD")
      .check(status.is(303))
      .check(header("Location").is(s"/sdes/create-request/data-sending-frequency":String))
  }
//
  def getFrequency: HttpRequestBuilder = {
    http("Frequency")
      .get(s"$baseUrl/sdes/create-request/data-sending-frequency")
      .check(RequestUtils.saveCsrfToken)
      .check(status.is(200))
      .check(regex("Will data be sent more than once in 5 years?").exists)
  }
  def postFrequency: HttpRequestBuilder = {
    http("Frequency")
      .post(s"$baseUrl/sdes/create-request/data-sending-frequency")
      .formParam("request-frequency", "recurring")
      .formParam("csrfToken", "${csrfToken}")
      .check(status.is(303))
      .check(header("Location").is(s"/sdes/create-request/confirm-requestor-details":String))
  }

  def getConfirmDetails: HttpRequestBuilder = {
    http("get ConfirmDetails")
      .get(s"$baseUrl/sdes/create-request/confirm-requestor-details")
      .check(status.is(200))
      .check(RequestUtils.saveCsrfToken)
      .check(regex("Confirm requestor details").exists)
  }
  def postConfirmDetails: HttpRequestBuilder = {
    http("post ConfirmDetails")
      .post(s"$baseUrl/sdes/create-request/confirm-requestor-details")
      .formParam("main-requestor", "test testst ; 123 ; Test@gmail.com")
      .formParam("alternative-requestor.name", "Knight, Gladys ; 2121921 ; Gladys.Knight@hmrc.gov.uk")
      .formParam("alternative-requestor.selected", "true")
      .formParam("csrfToken", "${csrfToken}")
      .check(status.is(303))
      .check(header("Location").is(s"/sdes/create-request/number-of-data-recipients":String))

  }
  def getDataRecipients: HttpRequestBuilder = {
    http("DataRecepients")
      .get(s"https://admin.development.tax.service.gov.uk/sdes/create-request/number-of-data-recipients")
      .check(status.is(200))
      .check(RequestUtils.saveCsrfToken)
      .check(regex("Add organisations").exists)

  }
  def postDataRecipients: HttpRequestBuilder = {
    http("DataRecepients")
      .post(s"$baseUrl/sdes/create-request/number-of-data-recipients")
      .formParam("csrfToken", "${csrfToken}")
      .check(status.is(303))
      .formParam("organisation-number", "1")
      .check(header("Location").is(s"/sdes/create-request/enter-recipient-details":String))
  }
//  def getSrn: HttpRequestBuilder = {
//    http("Srn")
//      .get(s"https://admin.development.tax.service.gov.uk/sdes/srn-or-name-search?term=Ab")
//      .check(status.is(200))
//      .check(regex("").exists)
//    //      .check(RequestUtils.saveCsrfToken)
//  }
//  def postsrn: HttpRequestBuilder = {
//    http("srn")
//      .get(s"https://admin.development.tax.service.gov.uk/sdes/create-request/srn-or-name-search?term=Ab")
//      .check(status.is(200))
//      .check(regex("").exists)
//    //      .check(RequestUtils.saveCsrfToken)
//  }
  def getRecipientDetails: HttpRequestBuilder = {
    http("get RecipientDetails")
      .get(s"$baseUrl/sdes/create-request/enter-recipient-details")
      .check(status.is(200))
      .check(regex("Enter organisation details").exists)
          .check(RequestUtils.saveCsrfToken)
  }
  def postRecipientDetails: HttpRequestBuilder = {
    http("post RecipientDetails")
      .post(s"$baseUrl/sdes/create-request/enter-recipient-details")
      .formParam("csrfToken", "${csrfToken}")
      .check(status.is(303))
      .formParam("organisation-details[0].name", "Abbey Bank PLC - 589117616467")
      .formParam("organisation-details[0].selected", "true")
      .check(header("Location").is(s"/sdes/create-request/enter-request-name":String))
  }
  def getRequestName: HttpRequestBuilder = {
    http("get RequestName")
      .get(s"$baseUrl/sdes/create-request/enter-request-name")
      .check(status.is(200))
      .check(regex("Enter a request name").exists)
      .check(RequestUtils.saveCsrfToken)
  }

  def postRequestName: HttpRequestBuilder = {
    http("post RequestName")
      .post(s"$baseUrl/sdes/create-request/enter-request-name")
      .formParam("csrfToken", "${csrfToken}")
      .check(status.is(303))
      .formParam("request-name", "test")
      .check(header("Location").is(s"/sdes/create-request/check-your-answers":String))
  }

  def getCheckYourAnswers: HttpRequestBuilder = {
    http("get Checkyouranswers")
      .get(s"$baseUrl/sdes/create-request/check-your-answers")
      .check(status.is(200))
      .check(regex("Check your answers").exists)
          .check(RequestUtils.saveCsrfToken)
  }

  def postCheckyouranswers: HttpRequestBuilder = {
    http("post Checkyouranswers")
      .post(s"https://admin.development.tax.service.gov.uk/sdes/create-request/check-your-answers")
      .formParam("csrfToken", "${csrfToken}")
      .check(status.is(303))
//      .check(header("Location").is(s"/sdes/create-request/confirmation/STUB-1":String))
  }
  def getApprovalPage: HttpRequestBuilder = {
    http("Sent for Approval page")
      .get(s"$baseUrl/sdes/create-request/confirmation/STUB-1")
      .check(status.is(200))
//      .check(regex("Data Exchange Request sent for approval").exists)
//     .check(RequestUtils.saveCsrfToken)
  }

}

