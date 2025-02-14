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
import uk.gov.hmrc.performance.conf.ServicesConfiguration
import utils.RequestUtils

object APDERRequst extends ServicesConfiguration {

  val baseUrl = "https://admin.qa.tax.service.gov.uk"
  val strideBaseUrl = baseUrlFor("stride-auth")

  private def savePageItem(name: String, pattern: String) = regex(_ => pattern).saveAs(name)

  //  val csrfPattern = """<input type="hidden" name="csrfToken" value="([^"]+)""""
  val relayStatePattern =
    """<input type="hidden" id="RelayState" name="RelayState" value="([^"]+)""""
  val samlResponsePattern = """<input type="hidden" name="SAMLResponse" value="([^"]+)""""
  val formUrlPattern = """<form action="([^"]+)"""


//  def getLoginPage: HttpRequestBuilder = {
//    http("Navigate to auth login stub page")
//      .get(s"https://admin.qa.tax.service.gov.uk/stride/sign-in?successURL=%2Fsdes%2Fdashboard&origin=secure-data-exchange-management-frontend")
//      .check(status.is(303))
//    //      .check(RequestUtils.saveCsrfToken)
//    //      .check(header("location").saveAs("authRequestRedirect"))
//    //      .check(header("Location").is(s"$baseUrl/stride-idp-stub/auth-request": String))
//  }

//  def postStrideAuthPage: HttpRequestBuilder = {
//    http("[POST] IdP stub login form")
//      .post(s"https://admin.qa.tax.service.gov.uk/stride-idp-stub/sign-in")
//      .disableFollowRedirect
//      //.formParam("csrfToken", s"$${csrfToken}")
//      .formParam("RelayState", "successURL=%2Fsdes%2Fdashboard&failureURL=%2Fstride%2Ffailure%3FcontinueURL%3D%25252Fsdes%25252Fdashboard")
//      .formParam("pid", "123")
//      .formParam("usersGivenName", "Test")
//      .formParam("usersSurname", "Test")
//      .formParam("emailAddress", "Test@gmail.com")
//      .formParam("status", true)
//      .formParam("signature", "valid")
//      .formParam("roles", "HMRC-SDES-REUESTOR")
//      .check(status.is(303))
//      .check(header("location").saveAs("signInRedirect"))
//  }
//
//  def postSamlResponse: HttpRequestBuilder = {
//    http("Post SAML response")
//      .post("https://admin.qa.tax.service.gov.uk/stride/auth-response")
//      .formParam("SAMLResponse", "PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiPz4KPHNhbWwycDpSZXNwb25zZSBJRD0iMTNhNWJjNjctZWNlOS00MTAwLWJjZGUtM2U3ZWY3YTlhNDc2IiBJc3N1ZUluc3RhbnQ9IjIwMTktMDItMTlUMTA6NTM6MDIuNDQ2WiIgVmVyc2lvbj0iMi4wIiB4bWxuczpzYW1sMnA9InVybjpvYXNpczpuYW1lczp0YzpTQU1MOjIuMDpwcm90b2NvbCI+PHNhbWwyOklzc3VlciB4bWxuczpzYW1sMj0idXJuOm9hc2lzOm5hbWVzOnRjOlNBTUw6Mi4wOmFzc2VydGlvbiI+aHR0cDovL2ZzLmVtYWN0ZXN0LmNvbS9hZGZzL3NlcnZpY2VzL3RydXN0PC9zYW1sMjpJc3N1ZXI+PHNhbWwycDpTdGF0dXM+PHNhbWwycDpTdGF0dXNDb2RlIFZhbHVlPSJ1cm46b2FzaXM6bmFtZXM6dGM6U0FNTDoyLjA6c3RhdHVzOlN1Y2Nlc3MiLz48L3NhbWwycDpTdGF0dXM+PHNhbWwyOkFzc2VydGlvbiBWZXJzaW9uPSIyLjAiIHhtbG5zOnNhbWwyPSJ1cm46b2FzaXM6bmFtZXM6dGM6U0FNTDoyLjA6YXNzZXJ0aW9uIj48c2FtbDI6SXNzdWVyPmh0dHA6Ly9mcy5lbWFjdGVzdC5jb20vYWRmcy9zZXJ2aWNlcy90cnVzdDwvc2FtbDI6SXNzdWVyPjxkczpTaWduYXR1cmUgeG1sbnM6ZHM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvMDkveG1sZHNpZyMiPgo8ZHM6U2lnbmVkSW5mbz4KPGRzOkNhbm9uaWNhbGl6YXRpb25NZXRob2QgQWxnb3JpdGhtPSJodHRwOi8vd3d3LnczLm9yZy8yMDA2LzEyL3htbC1jMTRuMTEiLz4KPGRzOlNpZ25hdHVyZU1ldGhvZCBBbGdvcml0aG09Imh0dHA6Ly93d3cudzMub3JnLzIwMDEvMDQveG1sZHNpZy1tb3JlI3JzYS1zaGE1MTIiLz4KPGRzOlJlZmVyZW5jZSBVUkk9IiI+CjxkczpUcmFuc2Zvcm1zPgo8ZHM6VHJhbnNmb3JtIEFsZ29yaXRobT0iaHR0cDovL3d3dy53My5vcmcvMjAwMC8wOS94bWxkc2lnI2VudmVsb3BlZC1zaWduYXR1cmUiLz4KPGRzOlRyYW5zZm9ybSBBbGdvcml0aG09Imh0dHA6Ly93d3cudzMub3JnLzIwMDEvMTAveG1sLWV4Yy1jMTRuIyIvPgo8L2RzOlRyYW5zZm9ybXM+CjxkczpEaWdlc3RNZXRob2QgQWxnb3JpdGhtPSJodHRwOi8vd3d3LnczLm9yZy8yMDAxLzA0L3htbGVuYyNzaGEyNTYiLz4KPGRzOkRpZ2VzdFZhbHVlPmhxUE9uMWlHbTYvR0FOV3hRTVhEUEhrWDBEVzQ4LzRqWXJOQ3J3dUtsUE09PC9kczpEaWdlc3RWYWx1ZT4KPC9kczpSZWZlcmVuY2U+CjwvZHM6U2lnbmVkSW5mbz4KPGRzOlNpZ25hdHVyZVZhbHVlPgpnZE4yN3pQcEpzT3YvWEsrcE5ZMk5mWjNMMjR4MWNtUkUrRDJheHdhVWxLRW9QZnc0eXBsdFppS1p4aE43OHNkS25zaCs3TmtIblZVCkpvK3JwM2grL0V5eEdKWHFPSElOZUx2MlRJdTlkdmRYMlJ5elhHb1NsRzRRbExRaDNuT3FicTRkSDlZWXBQWlN1bzN1RllnRFkrTWUKSkV5UkRKUnRjT3pQNWNEU1NpblBvRm5KUlBhNXc2NTUyM2RFQUhMeXJhSG5PS210VzFoQTk2OXZsUDhXdFVOaU4xbG9ETmdUbWlWVQpTSldBNnhabHROaXQ1RDZHdzJVMHhmZDZhRFl6WFF0T3lhYkVrWkhUV3JyTkNDUldMLysvTGRZZ0hQYWpOalhId0ZDaStZRDZrSk5KCml5QVNQa1RlaUJZTkZ3S1MzdXAwQ2tXWWl6bEwwMkp0Z1pCV1hRPT0KPC9kczpTaWduYXR1cmVWYWx1ZT4KPC9kczpTaWduYXR1cmU+PHNhbWwyOlN1YmplY3Q+PHNhbWwyOlN1YmplY3RDb25maXJtYXRpb24gTWV0aG9kPSJ1cm46b2FzaXM6bmFtZXM6dGM6U0FNTDoyLjA6Y206YmVhcmVyIj48c2FtbDI6U3ViamVjdENvbmZpcm1hdGlvbkRhdGEgSW5SZXNwb25zZVRvPSJNRFRQLTIxYzc3YmQ3LWIyZWItNGZmYy04YjVkLTMyYWI1NGRlMjE1MiIgTm90T25PckFmdGVyPSIyMDE5LTAyLTE5VDEwOjUzOjAyLjQ0N1oiIFJlY2lwaWVudD0iaHR0cHM6Ly93d3cudGF4LnNlcnZpY2UuZ292LnVrL3N0cmlkZS9hdXRoLXJlc3BvbnNlIi8+PC9zYW1sMjpTdWJqZWN0Q29uZmlybWF0aW9uPjwvc2FtbDI6U3ViamVjdD48c2FtbDI6QXV0aG5TdGF0ZW1lbnQgU2Vzc2lvbkluZGV4PSJhOTlkMmQ4Zi0zYmY3LTQ2YTktODFkMC1lYjQ0MWQ1YjljNmQiLz48c2FtbDI6QXR0cmlidXRlU3RhdGVtZW50PjxzYW1sMjpBdHRyaWJ1dGUgTmFtZT0iaHR0cDovL3NjaGVtYXMueG1sc29hcC5vcmcvd3MvMjAwNS8wNS9pZGVudGl0eS9jbGFpbXMvbmFtZSI+PHNhbWwyOkF0dHJpYnV0ZVZhbHVlPjEyMzwvc2FtbDI6QXR0cmlidXRlVmFsdWU+PC9zYW1sMjpBdHRyaWJ1dGU+PHNhbWwyOkF0dHJpYnV0ZSBOYW1lPSJodHRwOi8vc2NoZW1hcy54bWxzb2FwLm9yZy93cy8yMDA1LzA1L2lkZW50aXR5L2NsYWltcy9naXZlbm5hbWUiPjxzYW1sMjpBdHRyaWJ1dGVWYWx1ZT50ZXN0PC9zYW1sMjpBdHRyaWJ1dGVWYWx1ZT48L3NhbWwyOkF0dHJpYnV0ZT48c2FtbDI6QXR0cmlidXRlIE5hbWU9Imh0dHA6Ly9zY2hlbWFzLnhtbHNvYXAub3JnL3dzLzIwMDUvMDUvaWRlbnRpdHkvY2xhaW1zL3N1cm5hbWUiPjxzYW1sMjpBdHRyaWJ1dGVWYWx1ZT50ZXN0czwvc2FtbDI6QXR0cmlidXRlVmFsdWU+PC9zYW1sMjpBdHRyaWJ1dGU+PHNhbWwyOkF0dHJpYnV0ZSBOYW1lPSJodHRwOi8vc2NoZW1hcy54bWxzb2FwLm9yZy93cy8yMDA1LzA1L2lkZW50aXR5L2NsYWltcy9lbWFpbGFkZHJlc3MiPjxzYW1sMjpBdHRyaWJ1dGVWYWx1ZT5UZXN0QGdtYWlsLmNvbTwvc2FtbDI6QXR0cmlidXRlVmFsdWU+PC9zYW1sMjpBdHRyaWJ1dGU+PHNhbWwyOkF0dHJpYnV0ZSBOYW1lPSJodHRwOi8vc2NoZW1hcy5taWNyb3NvZnQuY29tL3dzLzIwMDgvMDYvaWRlbnRpdHkvY2xhaW1zL3JvbGUiPjxzYW1sMjpBdHRyaWJ1dGVWYWx1ZT5ITVJDLVNERVMtUkVRVUVTVE9SPC9zYW1sMjpBdHRyaWJ1dGVWYWx1ZT48L3NhbWwyOkF0dHJpYnV0ZT48L3NhbWwyOkF0dHJpYnV0ZVN0YXRlbWVudD48L3NhbWwyOkFzc2VydGlvbj48L3NhbWwycDpSZXNwb25zZT4=")
//      .formParam("RelayState", "successURL=%2Fsdes%2Fdashboard&failureURL=%2Fstride%2Ffailure%3FcontinueURL%3D%25252Fsdes%25252Fdashboard")
//      .check(status.is(303))
//  }
//
//  def getSdesLandingPage: HttpRequestBuilder = {
//    http("Navigate to Sdes Landing Page")
//      .get(s"$baseUrl/sdes/dashboard/requestor/requestorDashboard")
//      .check(status.is(200))
////      .check(RequestUtils.saveCsrfToken)
//      .check(regex("Requestor dashboard").exists)
//  }
//  def getRequestDatamovement: HttpRequestBuilder = {
//    http("RequestDatamovement Page")
//      .get(s"$baseUrl/sdes/dashboard/requestor/create")
//      .check(status.is(200))
//      .check(RequestUtils.saveCsrfToken)
//      .check(regex("Request a data movement").exists)
//  }
//
//  def getCreateDER: HttpRequestBuilder = {
//    http("Create DER")
//      .get(s"$baseUrl/sdes/dashboard/requestor/create-der?csrfToken={csrfToken}")
//      .check(status.is(303))
//  }
//
//  def getCreateTitlename: HttpRequestBuilder = {
//    http("CreateTitlename")
//      .get(s"$baseUrl/sdes/create-request/enter-request-name")
//      .check(status.is(200))
//      .check(RequestUtils.saveCsrfToken)
//      .check(regex("Create a title for this data movement").exists)
//  }
//  def postEnterDataMovementName: HttpRequestBuilder = {
//    http("EnterDataMovementName")
//      .post(s"$baseUrl/sdes/create-request/enter-request-name")
//      .formParam("request-name", "Test")
//      .formParam("csrfToken", "${csrfToken}")
//      .check(status.is(303))
//      .check(header("Location").is(s"/sdes/create-request/how-move-data":String))
//  }
//  def getHowDataMoved: HttpRequestBuilder = {
//    http("HowdatawillbeMoved")
//      .get(s"$baseUrl/sdes/create-request/how-move-data")
//      .check(status.is(200))
//      .check(RequestUtils.saveCsrfToken)
//      .check(regex("How do you want to move the data").exists)
//  }
//def postAPMEDIAtransfer: HttpRequestBuilder = {
//  http("ApprovalonlyPhysicalTransfer")
//    .post(s"$baseUrl/sdes/create-request/how-move-data")
//    .formParam("field", "MEDIA")
//    .formParam("csrfToken", "${csrfToken}")
//    .check(status.is(303))
//    .check(header("Location").is(s"/sdes/create-request/storage-media":String))
//}

//  def getStorageMedia: HttpRequestBuilder = {
//    http("StorageMedia")
//      .get(s"$baseUrl/sdes/create-request/storage-media")
//      .check(status.is(200))
//      .check(RequestUtils.saveCsrfToken)
//      .check(regex("What will the data be stored on").exists)
//  }

//  def postDataStored: HttpRequestBuilder = {
//    http("DataStoredOn")
//      .post(s"$baseUrl/sdes/create-request/storage-media")
//      .formParam("storage-media[]", "USB")
//      .formParam("usb-count", "1")
//      .formParam("csrfToken", "${csrfToken}")
//      .check(status.is(303))
//      .check(header("Location").is(s"/sdes/create-request/secure-transport":String))
//  }
//  def getSecureTransport: HttpRequestBuilder = {
//    http("SecureTransport")
//      .get(s"$baseUrl/sdes/create-request/secure-transport")
//      .check(status.is(200))
//      .check(RequestUtils.saveCsrfToken)
//      .check(regex("Do you need SDES Support to arrange secure transport").exists)
//  }
  def postNotASecureTransport: HttpRequestBuilder = {
    http("SelectNotSecureTransport")
      .post(s"$baseUrl/sdes/create-request/secure-transport")
      .formParam("field", "false")
      .formParam("csrfToken", "${csrfToken}")
      .check(status.is(303))
      .check(header("Location").is(s"/sdes/create-request/how-move-storage-media":String))
  }
  def getMoveStorageMedia: HttpRequestBuilder = {
    http("MoveStorageMedia")
      .get(s"$baseUrl/sdes/create-request/how-move-storage-media")
      .check(status.is(200))
      .check(RequestUtils.saveCsrfToken)
      .check(regex("How will you move the storage device").exists)
  }

  def postStorageMedia: HttpRequestBuilder = {
    http("EnterStorageMediaRoute")
      .post(s"$baseUrl/sdes/create-request/how-move-storage-media")
      .formParam("field", "test")
      .formParam("csrfToken", "${csrfToken}")
      .check(status.is(303))
      .check(header("Location").is(s"/sdes/create-request/is-data-protected":String))
  }
  //  def getDataProtected: HttpRequestBuilder = {
  //    http("Is DataProtected")
  //      .get(s"$baseUrl/sdes/create-request/is-data-protected")
  //      .check(status.is(200))
  //      .check(RequestUtils.saveCsrfToken)
  //      .check(regex("Will the data be protected").exists)
  //  }
//  def postAPProtectedYes: HttpRequestBuilder = {
//    http("ProtectedYes")
//      .post(s"$baseUrl/sdes/create-request/is-data-protected")
//      .formParam("isDataProtected", "Yes")
//      .formParam("howProtected", "Test Protected")
//      .formParam("csrfToken", "${csrfToken}")
//      .check(status.is(303))
//  }
    def getPackaging: HttpRequestBuilder = {
      http("GetPackaging")
        .get(s"$baseUrl/sdes/create-request/packaging")
        .check(status.is(200))
        .check(RequestUtils.saveCsrfToken)
        .check(regex("Will the storage device be packaged").exists)
    }
  def postAPPackaging: HttpRequestBuilder = {
    http("APPackagingSection")
      .post(s"$baseUrl/sdes/create-request/packaging")
      .formParam("field", "true")
      .formParam("csrfToken", "${csrfToken}")
      .check(status.is(303))
      .check(header("Location").is(s"/sdes/create-request/packaging-details":String))
  }
  def getPackagingDetails :HttpRequestBuilder = {
    http("PackagingDetails")
      .get(s"$baseUrl/sdes/create-request/packaging-details")
      .check(status.is(200))
      .check(RequestUtils.saveCsrfToken)
      .check(regex("What packaging will you use").exists)
  }
  def postPackagingDetails: HttpRequestBuilder = {
    http("EnteringPackagingDetails")
      .post(s"$baseUrl/sdes/create-request/packaging-details")
      .formParam("packaging-details", "BOX")
      .formParam("box-details", "Test")
      .formParam("csrfToken", "${csrfToken}")
      .check(status.is(303))
      .check(header("Location").is(s"/sdes/create-request/data-sending-request-frequency":String))
  }

  //  def getmovingdata: HttpRequestBuilder = {
  //    http("moving data")
  //      .get(s"$baseUrl/sdes/create-request/data-sending-request-frequency")
  //      .check(status.is(200))
  //      .check(RequestUtils.saveCsrfToken)
  //      .check(regex("Are you moving data more than once").exists)
  //  }
  //  def postFrequency: HttpRequestBuilder = {
  //    http("Recurring")
  //      .post(s"$baseUrl/sdes/create-request/data-sending-request-frequency")
  //      .formParam("request-frequency", "recurring")
  //      .formParam("csrfToken", "${csrfToken}")
  //      .check(status.is(303))
  //      .check(header("Location").is(s"/sdes/create-request/data-sending-unit-of-frequency":String))
  //  }
  //  def getFrequency: HttpRequestBuilder = {
  //    http("Frequency")
  //      .get(s"$baseUrl/sdes/create-request/data-sending-unit-of-frequency")
  //      .check(status.is(200))
  //      .check(RequestUtils.saveCsrfToken)
  //      .check(regex("How frequently will the data be moved").exists)
  //  }
//  def postPhysicalDailyFrequency: HttpRequestBuilder = {
//    http("DailyPhysicalTransfer")
//      .post(s"$baseUrl/sdes/create-request/data-sending-unit-of-frequency")
//      .formParam("unit-of-frequency", "DAILY")
//      .formParam("csrfToken", "${csrfToken}")
//      .check(status.is(303))
//      .check(header("Location").is(s"/sdes/create-request/start-date":String))
//  }
  def getAPStartdate: HttpRequestBuilder = {
    http("StartDate")
      .get(s"$baseUrl/sdes/create-request/start-date")
      .check(status.is(200))
      .check(RequestUtils.saveCsrfToken)
      .check(regex("When do you want the movement to start").exists)
  }

  def postAPStartDate: HttpRequestBuilder = {
    http("APMovementStartDate")
      .post(s"$baseUrl/sdes/create-request/start-date")
      .formParam("date.day", "10")
      .formParam("date.month", "10")
      .formParam("date.year", "2022")
      .formParam("csrfToken", "${csrfToken}")
      .check(status.is(303))
      .check(header("Location").is(s"/sdes/create-request/review-date":String))
  }

//  def getReviewDate: HttpRequestBuilder = {
//    http("APReviewDate")
//      .get(s"$baseUrl/sdes/create-request/review-date")
//      .check(status.is(200))
//      .check(RequestUtils.saveCsrfToken)
//      .check(regex("When will this movement be reviewed").exists)
//  }
//
//  def postReviewDate: HttpRequestBuilder = {
//    http("EnterReviewDate")
//      .post(s"$baseUrl/sdes/create-request/review-date")
//      .formParam("date.day", "10")
//      .formParam("date.month", "10")
//      .formParam("date.year", "2022")
//      .formParam("csrfToken", "${csrfToken}")
//      .check(status.is(303))
//      .check(header("Location").is(s"/sdes/create-request/data-contents-type":String))
//  }
  //  def getDataContentType: HttpRequestBuilder = {
  //    http("DataContentType")
  //      .get(s"$baseUrl/sdes/create-request/data-contents-type")
  //      .check(status.is(200))
  //      .check(RequestUtils.saveCsrfToken)
  //      .check(regex("What information is in the data").exists)
  //  }

  //  def postContentType: HttpRequestBuilder = {
  //    http("ContentType")
  //      .post(s"$baseUrl/sdes/create-request/data-contents-type")
  //      .formParam("typeOfData", "Test123")
  //      .formParam("csrfToken", "${csrfToken}")
  //      .check(status.is(303))
  //      .check(header("Location").is(s"/sdes/create-request/data-contents-identify":String))
  //  }
  //  def getContentIdentify: HttpRequestBuilder = {
  //    http("DataIdentify")
  //      .get(s"$baseUrl/sdes/create-request/data-contents-identify")
  //      .check(status.is(200))
  //      .check(RequestUtils.saveCsrfToken)
  //      .check(regex("Does the data allow you to identify anyone").exists)
  //  }
  //  def postIndentify: HttpRequestBuilder = {
  //    http("Idnentifythedata")
  //      .post(s"$baseUrl/sdes/create-request/data-contents-identify")
  //      .formParam("doesIdentify", "Yes")
  //      .formParam("noOfPeople", "123")
  //      .formParam("csrfToken", "${csrfToken}")
  //      .check(status.is(303))
  //      .check(header("Location").is(s"/sdes/create-request/data-contents-special-customer":String))
  //  }
  //  def getSpecialCustomer: HttpRequestBuilder = {
  //    http("SpecialCustomer")
  //      .get(s"$baseUrl/sdes/create-request/data-contents-special-customer")
  //      .check(status.is(200))
  //      .check(RequestUtils.saveCsrfToken)
  //      .check(regex("Does the data include special customer records").exists)
  //  }
  //  def postSpecialCustomer: HttpRequestBuilder = {
  //    http("Contentsspecialcustomer")
  //      .post(s"$baseUrl/sdes/create-request/data-contents-special-customer")
  //      .formParam("isCustomerSpecial", "Yes")
  ////      .formParam("howSpecialRemoved", "")
  //      .formParam("csrfToken", "${csrfToken}")
  //      .check(status.is(303))
  //      .check(header("Location").is(s"/sdes/create-request/data-contents-security-classification":String))
  //  }
  //  def getClassification: HttpRequestBuilder = {
  //    http("Classification")
  //      .get(s"$baseUrl/sdes/create-request/data-contents-security-classification")
  //      .check(status.is(200))
  //      .check(RequestUtils.saveCsrfToken)
  //      .check(regex("What is the Government Security Classification for this data").exists)
  //  }
  //  def postSecurityClassification: HttpRequestBuilder = {
  //    http("SecurityClassification")
  //      .post(s"$baseUrl/sdes/create-request/data-contents-security-classification")
  //      .formParam("field", "Official")
  //      .formParam("csrfToken", "${csrfToken}")
  //      .check(status.is(303))
  //      .check(header("Location").is(s"/sdes/create-request/select-approval-group":String))
  //  }
  //  def getApprovalgroup: HttpRequestBuilder = {
  //    http("Approvalgroup")
  //      .get(s"$baseUrl/sdes/create-request/select-approval-group")
  //      .check(status.is(200))
  //      .check(RequestUtils.saveCsrfToken)
  //      .check(regex("Which business area is responsible for the data").exists)
  //  }
  //  def postApprovalgroup: HttpRequestBuilder = {
  //    http("SelectApprovalGroup")
  //      .post(s"$baseUrl/sdes/create-request/select-approval-group")
  //      .formParam("approval-group", "7")
  //      .formParam("csrfToken", "${csrfToken}")
  //      .check(status.is(303))
  //      .check(header("Location").is(s"/sdes/create-request/why-data-needed":String))
  //  }

  //  def getDataNeeded: HttpRequestBuilder = {
  //    http("DataNeeded")
  //      .get(s"$baseUrl/sdes/create-request/why-data-needed")
  //      .check(status.is(200))
  //      .check(RequestUtils.saveCsrfToken)
  //      .check(regex("Why will the data be moved").exists)
  //  }
  //  def postDataNeeded: HttpRequestBuilder = {
  //    http("WhyDataNeeded")
  //      .post(s"$baseUrl/sdes/create-request/why-data-needed")
  //      .formParam("why-data-needed", " Test. 123")
  //      .formParam("csrfToken", "${csrfToken}")
  //      .check(status.is(303))
  //      .check(header("Location").is(s"/sdes/create-request/data-exchange-requestors":String))
  //  }
  //  def getRequestors: HttpRequestBuilder = {
  //    http("Requestors")
  //      .get(s"$baseUrl/sdes/create-request/data-exchange-requestors")
  //      .check(status.is(200))
  //      .check(RequestUtils.saveCsrfToken)
  //      .check(regex("Add team members").exists)
  //  }
  //  def postAddTeammember: HttpRequestBuilder = {
  //    http("TeamMemberPage")
  //      .post(s"$baseUrl/sdes/create-request/data-exchange-requestors?showWarning=false")
  //      .formParam("requestors-list[0]", "test tests ; 123 ; Test@gmail.com")
  //      .formParam("new-requestor", "Vatti, Venkat  ; 7834910 ; venkat.vatti@hmrc.gov.uk")
  //      .formParam("add-another", "true")
  //      .formParam("csrfToken", "${csrfToken}")
  //      .check(status.is(303))
  //      .check(header("Location").is(s"/sdes/create-request/data-exchange-requestors":String))
  //  }
  def postAdditionalTeamMember: HttpRequestBuilder = {
    http("AdditionalTeamMember-No")
      .post(s"$baseUrl/sdes/create-request/data-exchange-requestors?showWarning=false")
      .formParam("requestors-list[0]", "test tests ; 123 ; Test@gmail.com")
      .formParam("requestors-list[1]:", "Vatti, Venkat  ; 7834910 ; venkat.vatti@hmrc.gov.uk")
      .formParam("add-another", "false")
      .formParam("csrfToken", "${csrfToken}")
      .check(status.is(303))
      .check(header("Location").is(s"/sdes/create-request/where-data-from":String))
  }
  def getSenderorReceive: HttpRequestBuilder = {
    http("SenderorReceive")
      .get(s"$baseUrl/sdes/create-request/send-or-receive")
      .check(status.is(200))
      .check(RequestUtils.saveCsrfToken)
      .check(regex("Are you or your team sending or receiving the storage device").exists)
  }
  def postSendorReceive: HttpRequestBuilder = {
    http("EnterSendorReceive")
      .post(s"$baseUrl/sdes/create-request/send-or-receive")
      .formParam("field", "sending")
      .formParam("csrfToken", "${csrfToken}")
      .check(status.is(303))
      .check(header("Location").is(s"/sdes/create-request/recipient-contact-details-known":String))
  }
  def getrecipientcontactdetailsknown: HttpRequestBuilder = {
    http("recipientcontactdetailknown")
      .get(s"$baseUrl/sdes/create-request/recipient-contact-details-known")
      .check(status.is(200))
      .check(RequestUtils.saveCsrfToken)
      .check(regex("Do you know the contact details for the recipients").exists)
  }
  def postRecipientYes: HttpRequestBuilder = {
    http("RecipientDetailsYes")
      .post(s"$baseUrl/sdes/create-request/recipient-contact-details-known")
      .formParam("field", "true")
      .formParam("csrfToken", "${csrfToken}")
      .check(status.is(303))
      .check(header("Location").is(s"/sdes/create-request/recipient-contact-details":String))
  }
  def getrecipientcontactdetailst: HttpRequestBuilder = {
    http("APrecipientcontactdetails")
      .get(s"$baseUrl/sdes/create-request/recipient-contact-details")
      .check(status.is(200))
      .check(RequestUtils.saveCsrfToken)
      .check(regex("Recipient details").exists)
  }
  def postAPRecipientContactDetails: HttpRequestBuilder = {
    http("EnterAPDERRecipientContactDetails")
      .post(s"$baseUrl/sdes/create-request/recipient-contact-details")
      .formParam("name", "Venkat")
      .formParam("organisation", "HM Revenue & Customs")
      .formParam("how-contact[]", "Email")
      .formParam("email", "vattivenkat@gmail.com")
      .formParam("csrfToken", "${csrfToken}")
      .check(status.is(303))
      .check(header("Location").is(s"/sdes/create-request/additional-information":String))
  }

  //  def getAdditionalInfo: HttpRequestBuilder = {
  //    http("AdditionalinfoPage")
  //      .get(s"$baseUrl/sdes/create-request/additional-information")
  //      .check(status.is(200))
  //      .check(RequestUtils.saveCsrfToken)
  //      .check(regex("Additional information").exists)
  //  }
  def postAPDERAdditonalInformation: HttpRequestBuilder = {
    http("PhysicalDERAdditionalInformation")
      .post(s"$baseUrl/sdes/create-request/additional-information")
      .formParam("field", "helllo")
      .formParam("csrfToken", "${csrfToken}")
      .check(status.is(303))
      .check(header("Location").is(s"/sdes/create-request/check-storage-media-answers":String))
  }
  def getAPDERCYAPage: HttpRequestBuilder = {
    http("PhysicalCYA")
      .get(s"$baseUrl/sdes/create-request/check-storage-media-answers")
      .check(status.is(200))
      .check(RequestUtils.saveCsrfToken)
      .check(regex("Check your answers").exists)
  }
//  def postStoragemMediaAnnswersSubmit: HttpRequestBuilder = {
//    http("SubmitDER ")
//      .post(s"$baseUrl/sdes/create-request/check-storage-media-answers")
//      .formParam("csrfToken", "${csrfToken}")
//      .check(status.is(303))
//  }

}