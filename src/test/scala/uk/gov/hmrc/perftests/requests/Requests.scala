package uk.gov.hmrc.perftests.requests

import uk.gov.hmrc.performance.conf.ServicesConfiguration
import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._
import io.gatling.http.request.builder.{HttpAttributes, HttpRequestBuilder}
import uk.gov.hmrc.perftests.requests.Requests.{baseUrl, strideBaseUrl}
import utils.RequestUtils

object Requests extends ServicesConfiguration {

  val baseUrl = "https://admin.qa.tax.service.gov.uk"
  val strideBaseUrl = baseUrlFor("stride-auth")

  private def savePageItem(name: String, pattern: String) = regex(_ => pattern).saveAs(name)

  //  val csrfPattern = """<input type="hidden" name="csrfToken" value="([^"]+)""""
  val relayStatePattern =
    """<input type="hidden" id="RelayState" name="RelayState" value="([^"]+)""""
  val samlResponsePattern = """<input type="hidden" name="SAMLResponse" value="([^"]+)""""
  val formUrlPattern = """<form action="([^"]+)"""


  def getLoginPage: HttpRequestBuilder = {
    http("Navigate to auth login stub page")
      .get(s"https://admin.qa.tax.service.gov.uk/stride/sign-in?successURL=%2Fsdes%2Fdashboard&origin=secure-data-exchange-management-frontend")
      .check(status.is(303))
    //      .check(RequestUtils.saveCsrfToken)
    //      .check(header("location").saveAs("authRequestRedirect"))
    //      .check(header("Location").is(s"$baseUrl/stride-idp-stub/auth-request": String))
  }

  def postStrideAuthPage: HttpRequestBuilder = {
    http("[POST] IdP stub login form")
      .post(s"https://admin.qa.tax.service.gov.uk/stride-idp-stub/sign-in")
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
      .post("https://admin.qa.tax.service.gov.uk/stride/auth-response")
      .formParam("SAMLResponse", "PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiPz4KPHNhbWwycDpSZXNwb25zZSBJRD0iMTNhNWJjNjctZWNlOS00MTAwLWJjZGUtM2U3ZWY3YTlhNDc2IiBJc3N1ZUluc3RhbnQ9IjIwMTktMDItMTlUMTA6NTM6MDIuNDQ2WiIgVmVyc2lvbj0iMi4wIiB4bWxuczpzYW1sMnA9InVybjpvYXNpczpuYW1lczp0YzpTQU1MOjIuMDpwcm90b2NvbCI+PHNhbWwyOklzc3VlciB4bWxuczpzYW1sMj0idXJuOm9hc2lzOm5hbWVzOnRjOlNBTUw6Mi4wOmFzc2VydGlvbiI+aHR0cDovL2ZzLmVtYWN0ZXN0LmNvbS9hZGZzL3NlcnZpY2VzL3RydXN0PC9zYW1sMjpJc3N1ZXI+PHNhbWwycDpTdGF0dXM+PHNhbWwycDpTdGF0dXNDb2RlIFZhbHVlPSJ1cm46b2FzaXM6bmFtZXM6dGM6U0FNTDoyLjA6c3RhdHVzOlN1Y2Nlc3MiLz48L3NhbWwycDpTdGF0dXM+PHNhbWwyOkFzc2VydGlvbiBWZXJzaW9uPSIyLjAiIHhtbG5zOnNhbWwyPSJ1cm46b2FzaXM6bmFtZXM6dGM6U0FNTDoyLjA6YXNzZXJ0aW9uIj48c2FtbDI6SXNzdWVyPmh0dHA6Ly9mcy5lbWFjdGVzdC5jb20vYWRmcy9zZXJ2aWNlcy90cnVzdDwvc2FtbDI6SXNzdWVyPjxkczpTaWduYXR1cmUgeG1sbnM6ZHM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvMDkveG1sZHNpZyMiPgo8ZHM6U2lnbmVkSW5mbz4KPGRzOkNhbm9uaWNhbGl6YXRpb25NZXRob2QgQWxnb3JpdGhtPSJodHRwOi8vd3d3LnczLm9yZy8yMDA2LzEyL3htbC1jMTRuMTEiLz4KPGRzOlNpZ25hdHVyZU1ldGhvZCBBbGdvcml0aG09Imh0dHA6Ly93d3cudzMub3JnLzIwMDEvMDQveG1sZHNpZy1tb3JlI3JzYS1zaGE1MTIiLz4KPGRzOlJlZmVyZW5jZSBVUkk9IiI+CjxkczpUcmFuc2Zvcm1zPgo8ZHM6VHJhbnNmb3JtIEFsZ29yaXRobT0iaHR0cDovL3d3dy53My5vcmcvMjAwMC8wOS94bWxkc2lnI2VudmVsb3BlZC1zaWduYXR1cmUiLz4KPGRzOlRyYW5zZm9ybSBBbGdvcml0aG09Imh0dHA6Ly93d3cudzMub3JnLzIwMDEvMTAveG1sLWV4Yy1jMTRuIyIvPgo8L2RzOlRyYW5zZm9ybXM+CjxkczpEaWdlc3RNZXRob2QgQWxnb3JpdGhtPSJodHRwOi8vd3d3LnczLm9yZy8yMDAxLzA0L3htbGVuYyNzaGEyNTYiLz4KPGRzOkRpZ2VzdFZhbHVlPmhxUE9uMWlHbTYvR0FOV3hRTVhEUEhrWDBEVzQ4LzRqWXJOQ3J3dUtsUE09PC9kczpEaWdlc3RWYWx1ZT4KPC9kczpSZWZlcmVuY2U+CjwvZHM6U2lnbmVkSW5mbz4KPGRzOlNpZ25hdHVyZVZhbHVlPgpnZE4yN3pQcEpzT3YvWEsrcE5ZMk5mWjNMMjR4MWNtUkUrRDJheHdhVWxLRW9QZnc0eXBsdFppS1p4aE43OHNkS25zaCs3TmtIblZVCkpvK3JwM2grL0V5eEdKWHFPSElOZUx2MlRJdTlkdmRYMlJ5elhHb1NsRzRRbExRaDNuT3FicTRkSDlZWXBQWlN1bzN1RllnRFkrTWUKSkV5UkRKUnRjT3pQNWNEU1NpblBvRm5KUlBhNXc2NTUyM2RFQUhMeXJhSG5PS210VzFoQTk2OXZsUDhXdFVOaU4xbG9ETmdUbWlWVQpTSldBNnhabHROaXQ1RDZHdzJVMHhmZDZhRFl6WFF0T3lhYkVrWkhUV3JyTkNDUldMLysvTGRZZ0hQYWpOalhId0ZDaStZRDZrSk5KCml5QVNQa1RlaUJZTkZ3S1MzdXAwQ2tXWWl6bEwwMkp0Z1pCV1hRPT0KPC9kczpTaWduYXR1cmVWYWx1ZT4KPC9kczpTaWduYXR1cmU+PHNhbWwyOlN1YmplY3Q+PHNhbWwyOlN1YmplY3RDb25maXJtYXRpb24gTWV0aG9kPSJ1cm46b2FzaXM6bmFtZXM6dGM6U0FNTDoyLjA6Y206YmVhcmVyIj48c2FtbDI6U3ViamVjdENvbmZpcm1hdGlvbkRhdGEgSW5SZXNwb25zZVRvPSJNRFRQLTIxYzc3YmQ3LWIyZWItNGZmYy04YjVkLTMyYWI1NGRlMjE1MiIgTm90T25PckFmdGVyPSIyMDE5LTAyLTE5VDEwOjUzOjAyLjQ0N1oiIFJlY2lwaWVudD0iaHR0cHM6Ly93d3cudGF4LnNlcnZpY2UuZ292LnVrL3N0cmlkZS9hdXRoLXJlc3BvbnNlIi8+PC9zYW1sMjpTdWJqZWN0Q29uZmlybWF0aW9uPjwvc2FtbDI6U3ViamVjdD48c2FtbDI6QXV0aG5TdGF0ZW1lbnQgU2Vzc2lvbkluZGV4PSJhOTlkMmQ4Zi0zYmY3LTQ2YTktODFkMC1lYjQ0MWQ1YjljNmQiLz48c2FtbDI6QXR0cmlidXRlU3RhdGVtZW50PjxzYW1sMjpBdHRyaWJ1dGUgTmFtZT0iaHR0cDovL3NjaGVtYXMueG1sc29hcC5vcmcvd3MvMjAwNS8wNS9pZGVudGl0eS9jbGFpbXMvbmFtZSI+PHNhbWwyOkF0dHJpYnV0ZVZhbHVlPjEyMzwvc2FtbDI6QXR0cmlidXRlVmFsdWU+PC9zYW1sMjpBdHRyaWJ1dGU+PHNhbWwyOkF0dHJpYnV0ZSBOYW1lPSJodHRwOi8vc2NoZW1hcy54bWxzb2FwLm9yZy93cy8yMDA1LzA1L2lkZW50aXR5L2NsYWltcy9naXZlbm5hbWUiPjxzYW1sMjpBdHRyaWJ1dGVWYWx1ZT50ZXN0PC9zYW1sMjpBdHRyaWJ1dGVWYWx1ZT48L3NhbWwyOkF0dHJpYnV0ZT48c2FtbDI6QXR0cmlidXRlIE5hbWU9Imh0dHA6Ly9zY2hlbWFzLnhtbHNvYXAub3JnL3dzLzIwMDUvMDUvaWRlbnRpdHkvY2xhaW1zL3N1cm5hbWUiPjxzYW1sMjpBdHRyaWJ1dGVWYWx1ZT50ZXN0czwvc2FtbDI6QXR0cmlidXRlVmFsdWU+PC9zYW1sMjpBdHRyaWJ1dGU+PHNhbWwyOkF0dHJpYnV0ZSBOYW1lPSJodHRwOi8vc2NoZW1hcy54bWxzb2FwLm9yZy93cy8yMDA1LzA1L2lkZW50aXR5L2NsYWltcy9lbWFpbGFkZHJlc3MiPjxzYW1sMjpBdHRyaWJ1dGVWYWx1ZT5UZXN0QGdtYWlsLmNvbTwvc2FtbDI6QXR0cmlidXRlVmFsdWU+PC9zYW1sMjpBdHRyaWJ1dGU+PHNhbWwyOkF0dHJpYnV0ZSBOYW1lPSJodHRwOi8vc2NoZW1hcy5taWNyb3NvZnQuY29tL3dzLzIwMDgvMDYvaWRlbnRpdHkvY2xhaW1zL3JvbGUiPjxzYW1sMjpBdHRyaWJ1dGVWYWx1ZT5ITVJDLVNERVMtUkVRVUVTVE9SPC9zYW1sMjpBdHRyaWJ1dGVWYWx1ZT48L3NhbWwyOkF0dHJpYnV0ZT48L3NhbWwyOkF0dHJpYnV0ZVN0YXRlbWVudD48L3NhbWwyOkFzc2VydGlvbj48L3NhbWwycDpSZXNwb25zZT4=")
      .formParam("RelayState", "successURL=%2Fsdes%2Fdashboard&failureURL=%2Fstride%2Ffailure%3FcontinueURL%3D%25252Fsdes%25252Fdashboard")
      .check(status.is(303))
  }

  def getSdesLandingPage: HttpRequestBuilder = {
    http("Navigate to Sdes Landing Page")
      .get(s"$baseUrl/sdes/dashboard/requestor/requestorDashboard")
      .check(status.is(200))
//      .check(RequestUtils.saveCsrfToken)
      .check(regex("Requestor dashboard").exists)
  }
  def getRequestDatamovement: HttpRequestBuilder = {
    http("RequestDatamovement Page")
      .get(s"$baseUrl/sdes/dashboard/requestor/create")
      .check(status.is(200))
      .check(RequestUtils.saveCsrfToken)
      .check(regex("Request a data movement").exists)
  }

  def getCreateDER: HttpRequestBuilder = {
    http("Create DER")
      .get(s"$baseUrl/sdes/dashboard/requestor/create-der?csrfToken={csrfToken}")
      .check(status.is(303))
  }

  def getCreateTitlename: HttpRequestBuilder = {
    http("CreateTitlename")
      .get(s"$baseUrl/sdes/create-request/enter-request-name")
      .check(status.is(200))
      .check(RequestUtils.saveCsrfToken)
      .check(regex("Create a title for this data movement").exists)
  }
  def postEnterDataMovementName: HttpRequestBuilder = {
    http("EnterDataMovementName")
      .post(s"$baseUrl/sdes/create-request/enter-request-name")
      .formParam("request-name", "Test")
      .formParam("csrfToken", "${csrfToken}")
      .check(status.is(303))
      .check(header("Location").is(s"/sdes/create-request/how-move-data":String))
  }
  def getHowDataMoved: HttpRequestBuilder = {
    http("HowdatawillbeMoved")
      .get(s"$baseUrl/sdes/create-request/how-move-data")
      .check(status.is(200))
      .check(RequestUtils.saveCsrfToken)
      .check(regex("How do you want to move the data").exists)
  }
  def postDigitaltransfer: HttpRequestBuilder = {
    http("DigitalTransfer")
      .post(s"$baseUrl/sdes/create-request/how-move-data")
      .formParam("field", "SDES")
      .formParam("csrfToken", "${csrfToken}")
      .check(status.is(303))
      .check(header("Location").is(s"/sdes/create-request/select-data-direction":String))
  }
  def getDirection: HttpRequestBuilder = {
    http("DisplayDirection  ")
      .get(s"$baseUrl/sdes/create-request/select-data-direction")
      .check(status.is(200))
      .check(RequestUtils.saveCsrfToken)
      .check(regex("Where is the data moving").exists)
  }
  def postDirection: HttpRequestBuilder = {
    http("SelectDirection")
      .post(s"$baseUrl/sdes/create-request/select-data-direction")
      .formParam("data-direction", "INBOUND")
      .formParam("csrfToken", "${csrfToken}")
      .check(status.is(303))
      .check(header("Location").is(s"/sdes/create-request/select-business-area?direction=INBOUND":String))
  }
  def getBusinessArea: HttpRequestBuilder = {
    http("BusinessArea")
      .get(s"$baseUrl/sdes/create-request/select-business-area?direction=INBOUND")
      .check(status.is(200))
      .check(RequestUtils.saveCsrfToken)
      .check(regex("What type of organisation is sending the data").exists)
  }
  def postDERDirection: HttpRequestBuilder = {
    http("DERDirection")
      .post(s"$baseUrl/sdes/create-request/select-business-area?direction=INBOUND")
      .formParam("direction", "INBOUND")
      .formParam("business-area", "OTHR")
      .formParam("csrfToken", "${csrfToken}")
      .check(status.is(303))
      .check(header("Location").is(s"/sdes/create-request/select-template?businessArea=OTHR&direction=INBOUND":String))
  }
  def getTemplate: HttpRequestBuilder = {
    http("InformationType")
      .get(s"$baseUrl/sdes/create-request/select-template?businessArea=OTHR&direction=INBOUND")
      .check(status.is(200))
      .check(RequestUtils.saveCsrfToken)
      .check(regex("Select an information type").exists)
  }
  def postInformationType: HttpRequestBuilder = {
    http("Select InformationType")
      .post(s"$baseUrl/sdes/create-request/select-template?businessArea=OTHR&direction=INBOUND")
      .formParam("data-exchange-template", "OthersSETtoSecureFolder_;_No information type_;_INBOUND_;_INDIVIDUAL")
      .formParam("csrfToken", "${csrfToken}")
      .check(status.is(303))
      .check(header("Location").is(s"/sdes/create-request/data-sending-request-frequency":String))
  }
  def getmovingdata: HttpRequestBuilder = {
    http("moving data")
      .get(s"$baseUrl/sdes/create-request/data-sending-request-frequency")
      .check(status.is(200))
      .check(RequestUtils.saveCsrfToken)
      .check(regex("Are you moving data more than once").exists)
  }
  def postFrequency: HttpRequestBuilder = {
    http("Recurring")
      .post(s"$baseUrl/sdes/create-request/data-sending-request-frequency")
      .formParam("request-frequency", "recurring")
      .formParam("csrfToken", "${csrfToken}")
      .check(status.is(303))
      .check(header("Location").is(s"/sdes/create-request/data-sending-unit-of-frequency":String))
  }
  def getFrequency: HttpRequestBuilder = {
    http("Frequency")
      .get(s"$baseUrl/sdes/create-request/data-sending-unit-of-frequency")
      .check(status.is(200))
      .check(RequestUtils.saveCsrfToken)
      .check(regex("How frequently will the data be moved").exists)
  }
  def postDailyFrequency: HttpRequestBuilder = {
    http("DAILYTransfer")
      .post(s"$baseUrl/sdes/create-request/data-sending-unit-of-frequency")
      .formParam("unit-of-frequency", "DAILY")
      .formParam("csrfToken", "${csrfToken}")
      .check(status.is(303))
      .check(header("Location").is(s"/sdes/create-request/data-sending-date-frequency":String))
  }
  def getRequestReviewed: HttpRequestBuilder = {
    http("RequestReviewedDate")
      .get(s"$baseUrl/sdes/create-request/data-sending-date-frequency")
      .check(status.is(200))
      .check(RequestUtils.saveCsrfToken)
      .check(regex("When will this data request be reviewed").exists)
  }
  def postRequestSendingDate: HttpRequestBuilder = {
    http("RequestSendingDate")
      .post(s"$baseUrl/sdes/create-request/data-sending-date-frequency")
      .formParam("sending-date.day", "10")
      .formParam("sending-date.month", "10")
      .formParam("sending-date.year", "2022")
      .formParam("csrfToken", "${csrfToken}")
      .check(status.is(303))
      .check(header("Location").is(s"/sdes/create-request/data-contents-type":String))
  }
  def getDataContentType: HttpRequestBuilder = {
    http("DataContentType")
      .get(s"$baseUrl/sdes/create-request/data-contents-type")
      .check(status.is(200))
      .check(RequestUtils.saveCsrfToken)
      .check(regex("What information is in the data").exists)
  }

  def postContentType: HttpRequestBuilder = {
    http("ContentType")
      .post(s"$baseUrl/sdes/create-request/data-contents-type")
      .formParam("typeOfData", "Test123")
      .formParam("csrfToken", "${csrfToken}")
      .check(status.is(303))
      .check(header("Location").is(s"/sdes/create-request/data-contents-identify":String))
  }
  def getContentIdentify: HttpRequestBuilder = {
    http("DataIdentify")
      .get(s"$baseUrl/sdes/create-request/data-contents-identify")
      .check(status.is(200))
      .check(RequestUtils.saveCsrfToken)
      .check(regex("Does the data allow you to identify anyone").exists)
  }
  def postIndentify: HttpRequestBuilder = {
    http("Idnentifythedata")
      .post(s"$baseUrl/sdes/create-request/data-contents-identify")
      .formParam("doesIdentify", "Yes")
      .formParam("noOfPeople", "123")
      .formParam("csrfToken", "${csrfToken}")
      .check(status.is(303))
      .check(header("Location").is(s"/sdes/create-request/data-contents-special-customer":String))
  }
  def getSpecialCustomer: HttpRequestBuilder = {
    http("SpecialCustomer")
      .get(s"$baseUrl/sdes/create-request/data-contents-special-customer")
      .check(status.is(200))
      .check(RequestUtils.saveCsrfToken)
      .check(regex("Does the data include special customer records").exists)
  }
  def postSpecialCustomer: HttpRequestBuilder = {
    http("Contentsspecialcustomer")
      .post(s"$baseUrl/sdes/create-request/data-contents-special-customer")
      .formParam("isCustomerSpecial", "Yes")
//      .formParam("howSpecialRemoved", "")
      .formParam("csrfToken", "${csrfToken}")
      .check(status.is(303))
      .check(header("Location").is(s"/sdes/create-request/data-contents-security-classification":String))
  }
  def getClassification: HttpRequestBuilder = {
    http("Classification")
      .get(s"$baseUrl/sdes/create-request/data-contents-security-classification")
      .check(status.is(200))
      .check(RequestUtils.saveCsrfToken)
      .check(regex("What is the Government Security Classification for this data").exists)
  }
  def postClassification: HttpRequestBuilder = {
    http("SecurityClassification")
      .post(s"$baseUrl/sdes/create-request/data-contents-security-classification")
      .formParam("field", "Official")
      .formParam("csrfToken", "${csrfToken}")
      .check(status.is(303))
      .check(header("Location").is(s"/sdes/create-request/why-data-needed":String))
  }
  def getDataNeeded: HttpRequestBuilder = {
    http("DataNeeded")
      .get(s"$baseUrl/sdes/create-request/why-data-needed")
      .check(status.is(200))
      .check(RequestUtils.saveCsrfToken)
      .check(regex("Why will the data be moved").exists)
  }
  def postDataNeeded: HttpRequestBuilder = {
    http("WhyDataNeeded")
      .post(s"$baseUrl/sdes/create-request/why-data-needed")
      .formParam("why-data-needed", " Test. 123")
      .formParam("csrfToken", "${csrfToken}")
      .check(status.is(303))
      .check(header("Location").is(s"/sdes/create-request/data-exchange-requestors":String))
  }
  def getRequestors: HttpRequestBuilder = {
    http("Requestors")
      .get(s"$baseUrl/sdes/create-request/data-exchange-requestors")
      .check(status.is(200))
      .check(RequestUtils.saveCsrfToken)
      .check(regex("Add team members").exists)
  }
  def postAddTeammember: HttpRequestBuilder = {
    http("TeamMemberPage")
      .post(s"$baseUrl/sdes/create-request/data-exchange-requestors?showWarning=false")
      .formParam("requestors-list[0]", "test tests ; 123 ; Test@gmail.com")
      .formParam("new-requestor", "Vatti, Venkat  ; 7834910 ; venkat.vatti@hmrc.gov.uk")
      .formParam("add-another", "true")
      .formParam("csrfToken", "${csrfToken}")
      .check(status.is(303))
      .check(header("Location").is(s"/sdes/create-request/data-exchange-requestors":String))
  }
//  def postAdditionalTeamMember: HttpRequestBuilder = {
//    http("AdditionalTeamMember-No")
//      .post(s"$baseUrl/sdes/create-request/data-exchange-requestors?showWarning=false")
//      .formParam("requestors-list[0]", "test tests ; 123 ; Test@gmail.com")
//      .formParam("requestors-list[1]:", "Vatti, Venkat  ; 7834910 ; venkat.vatti@hmrc.gov.uk")
//      .formParam("add-another", "false")
//      .formParam("csrfToken", "${csrfToken}")
//      .check(status.is(303))
//      .check(header("Location").is(s"/sdes/create-request/add-organisations":String))
//  }
  def getOrganisationPage: HttpRequestBuilder = {
    http("OrganisationPage")
      .get(s"$baseUrl/sdes/create-request/add-organisations")
      .check(status.is(200))
      .check(RequestUtils.saveCsrfToken)
      .check(regex("Add organisations sending data").exists)
  }
  def postAddOrganisation: HttpRequestBuilder = {
    http("AddOrganisation")
      .post(s"$baseUrl/sdes/create-request/add-organisations")
      .formParam("organisation-details[0].name", "Brown Shipley - 317309006941")
      .formParam("csrfToken", "${csrfToken}")
      .check(status.is(303))
      .check(header("Location").is(s"/sdes/create-request/do-you-need-to-add-identifiers":String))
  }
  def getIndentifiers: HttpRequestBuilder = {
    http("IndentifiersPage")
      .get(s"$baseUrl/sdes/create-request/do-you-need-to-add-identifiers")
      .check(status.is(200))
      .check(RequestUtils.saveCsrfToken)
      .check(regex("Do you need to add identifier codes to this request").exists)
  }
  def postIndentifiers: HttpRequestBuilder = {
    http("AddIndentifiers")
      .post(s"$baseUrl/sdes/create-request/do-you-need-to-add-identifiers")
      .formParam("field", "false")
      .formParam("csrfToken", "${csrfToken}")
      .check(status.is(303))
      .check(header("Location").is(s"/sdes/create-request/additional-information":String))
  }
  def getAdditionalInfo: HttpRequestBuilder = {
    http("AdditionalinfoPage")
      .get(s"$baseUrl/sdes/create-request/additional-information")
      .check(status.is(200))
      .check(RequestUtils.saveCsrfToken)
      .check(regex("Additional information").exists)
  }
  def postAdditonalInformation: HttpRequestBuilder = {
    http("AddAdditionalInformation")
      .post(s"$baseUrl/sdes/create-request/additional-information")
      .formParam("field", "helllo")
      .formParam("csrfToken", "${csrfToken}")
      .check(status.is(303))
      .check(header("Location").is(s"/sdes/create-request/check-your-answers":String))
  }
  def getCYAPage: HttpRequestBuilder = {
    http("CYA")
      .get(s"$baseUrl/sdes/create-request/check-your-answers")
      .check(status.is(200))
      .check(RequestUtils.saveCsrfToken)
      .check(regex("Check your answers").exists)
  }
    def postSubmitDER: HttpRequestBuilder = {
    http("SubmitDER ")
      .post(s"$baseUrl/sdes/create-request/check-your-answers")
      .formParam("csrfToken", "${csrfToken}")
      .check(status.is(303))
  }
//  def postCreateDER: HttpRequestBuilder = {
//    http("Create DER")
//      .post(s"$baseUrl/sdes/dashboard/requestor/create-der")
//      .check(RequestUtils.saveCsrfToken)
//      .check(status.is(303))
//      .check(header("Location").is(s"/sdes/create-request/select-request-type":String))
//  }
//
//  def getRequestType: HttpRequestBuilder = {
//    http("get Request type")
//      .get(s"$baseUrl/sdes/create-request/select-request-type")
//      .check(status.is(200))
//      .check(RequestUtils.saveCsrfToken)
//    .check(regex("Create a new data exchange request").exists)
//
//    }
//
//  def postRequestType: HttpRequestBuilder = {
//    http("post Request type")
//      .post(s"$baseUrl/sdes/create-request/select-request-type")
//      .formParam("request-type", "electronic")
////      .formParam("type", "submit")
//      .formParam("csrfToken", "${csrfToken}")
//      .check(status.is(303))
//      .check(header("Location").is(s"/sdes/create-request/select-template":String))
//
//
//  }
//
//  def getSelectTemplate: HttpRequestBuilder = {
//    http("TemplateType")
//      .get(s"$baseUrl/sdes/create-request/select-template")
//      .check(RequestUtils.saveCsrfToken)
//      .check(status.is(200))
//      .check(regex("Select an information type").exists)
//
//  }
//  def postSelectTemplate: HttpRequestBuilder = {
//    http("TemplateType")
//      .post(s"$baseUrl/sdes/create-request/select-template")
//      .formParam("csrfToken", "${csrfToken}")
//      .formParam("data-exchange-template", "businessIntel_;_Business Intelligence to MoD")
//      .check(status.is(303))
//      .check(header("Location").is(s"/sdes/create-request/data-sending-frequency":String))
//  }
////
//  def getFrequency: HttpRequestBuilder = {
//    http("Frequency")
//      .get(s"$baseUrl/sdes/create-request/data-sending-frequency")
//      .check(RequestUtils.saveCsrfToken)
//      .check(status.is(200))
//      .check(regex("Will data be sent more than once in 5 years?").exists)
//  }
//  def postFrequency: HttpRequestBuilder = {
//    http("Frequency")
//      .post(s"$baseUrl/sdes/create-request/data-sending-frequency")
//      .formParam("request-frequency", "recurring")
//      .formParam("csrfToken", "${csrfToken}")
//      .check(status.is(303))
//      .check(header("Location").is(s"/sdes/create-request/confirm-requestor-details":String))
//  }
//
//  def getConfirmDetails: HttpRequestBuilder = {
//    http("get ConfirmDetails")
//      .get(s"$baseUrl/sdes/create-request/confirm-requestor-details")
//      .check(status.is(200))
//      .check(RequestUtils.saveCsrfToken)
//      .check(regex("Confirm requestor details").exists)
//  }
//  def postConfirmDetails: HttpRequestBuilder = {
//    http("post ConfirmDetails")
//      .post(s"$baseUrl/sdes/create-request/confirm-requestor-details")
//      .formParam("main-requestor", "test testst ; 123 ; Test@gmail.com")
//      .formParam("alternative-requestor.name", "Knight, Gladys ; 2121921 ; Gladys.Knight@hmrc.gov.uk")
//      .formParam("alternative-requestor.selected", "true")
//      .formParam("csrfToken", "${csrfToken}")
//      .check(status.is(303))
//      .check(header("Location").is(s"/sdes/create-request/number-of-data-recipients":String))
//
//  }
//  def getDataRecipients: HttpRequestBuilder = {
//    http("get DataRecepients")
//      .get(s"$baseUrl/sdes/create-request/number-of-data-recipients")
//      .check(status.is(200))
//      .check(RequestUtils.saveCsrfToken)
//      .check(regex("Add organisations").exists)
//
//  }
//  def postDataRecipients: HttpRequestBuilder = {
//    http("post DataRecepients")
//      .post(s"$baseUrl/sdes/create-request/number-of-data-recipients")
//      .formParam("csrfToken", "${csrfToken}")
//      .check(status.is(303))
//      .formParam("organisation-number", "1")
//      .check(header("Location").is(s"/sdes/create-request/enter-recipient-details":String))
//  }
////  def getSrn: HttpRequestBuilder = {
////    http("Srn")
////      .get(s"https://admin.development.tax.service.gov.uk/sdes/srn-or-name-search?term=Ab")
////      .check(status.is(200))
////      .check(regex("").exists)
////    //      .check(RequestUtils.saveCsrfToken)
////  }
////  def postsrn: HttpRequestBuilder = {
////    http("srn")
////      .get(s"https://admin.development.tax.service.gov.uk/sdes/create-request/srn-or-name-search?term=Ab")
////      .check(status.is(200))
////      .check(regex("").exists)
////    //      .check(RequestUtils.saveCsrfToken)
////  }
//  def getRecipientDetails: HttpRequestBuilder = {
//    http("get RecipientDetails")
//      .get(s"$baseUrl/sdes/create-request/enter-recipient-details")
//      .check(status.is(200))
//      .check(regex("Enter organisation details").exists)
//          .check(RequestUtils.saveCsrfToken)
//  }
//  def postRecipientDetails: HttpRequestBuilder = {
//    http("post RecipientDetails")
//      .post(s"$baseUrl/sdes/create-request/enter-recipient-details")
//      .formParam("csrfToken", "${csrfToken}")
//      .check(status.is(303))
//      .formParam("organisation-details[0].name", "Abbey Bank PLC - 589117616467")
//      .formParam("organisation-details[0].selected", "true")
//      .check(header("Location").is(s"/sdes/create-request/enter-request-name":String))
//  }
//  def getRequestName: HttpRequestBuilder = {
//    http("get RequestName")
//      .get(s"$baseUrl/sdes/create-request/enter-request-name")
//      .check(status.is(200))
//      .check(regex("Enter a request name").exists)
//      .check(RequestUtils.saveCsrfToken)
//  }
//
//  def postRequestName: HttpRequestBuilder = {
//    http("post RequestName")
//      .post(s"$baseUrl/sdes/create-request/enter-request-name")
//      .formParam("csrfToken", "${csrfToken}")
//      .check(status.is(303))
//      .formParam("request-name", "test")
//      .check(header("Location").is(s"/sdes/create-request/check-your-answers":String))
//  }
//
//  def getCheckYourAnswers: HttpRequestBuilder = {
//    http("get Checkyouranswers")
//      .get(s"$baseUrl/sdes/create-request/check-your-answers")
//      .check(status.is(200))
//      .check(regex("Check your answers").exists)
//          .check(RequestUtils.saveCsrfToken)
//  }
//
//  def postCheckyouranswers: HttpRequestBuilder = {
//    http("post Checkyouranswers")
//      .post(s"$baseUrl/sdes/create-request/check-your-answers")
//      .formParam("csrfToken", "${csrfToken}")
//      .check(status.is(303))
////      .check(header("Location").is(s"/sdes/create-request/confirmation/STUB-1":String))
//  }
//  def getApprovalPage: HttpRequestBuilder = {
//    http("Sent for Approval page")
//      .get(s"$baseUrl/sdes/create-request/confirmation/STUB-1")
//      .check(status.is(200))
////      .check(regex("Data Exchange Request sent for approval").exists)
////     .check(RequestUtils.saveCsrfToken)
//  }

}

