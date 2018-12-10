package uk.gov.hmrc.perftests.requests

import uk.gov.hmrc.performance.conf.ServicesConfiguration
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.request.builder.HttpRequestBuilder
import uk.gov.hmrc.perftests.requests.Requests.{authUrl, redirectUrl, strideUrl}
import utils.RequestUtils

object Requests extends ServicesConfiguration {

  val baseUrl = baseUrlFor("secure-data-exchange")
  val strideUrl=baseUrlFor("stride-auth")


  def getLoginPage: HttpRequestBuilder = {
    http("Navigate to auth login stub page")
      .get(s"$strideUrl/sdes/dashboard")
      .check(status.is(303))
      .check(RequestUtils.saveCsrfToken)
      .check(regex("Stride Identity Provider Login Stub").exists)
      .check(regex("SAML Signature scenario").exists)
//      .check(header("Location").is(s"$baseUrl/stride-idp-stub/auth-request": String))
  }


  def postLoginStub: HttpRequestBuilder = {
    http("Login with user credentials")
      .post(s"$strideUrl/auth-login-stub/gg-sign-in")
      .formParam("RelayState", "successURL=http%3A%2F%2Flocalhost%3A9000%2Fsdes%2Fdashboard&failureURL=%2Fstride%2Ffailure%3FcontinueURL%3Dhttp%25253A%25252F%25252Flocalhost%25253A9000%25252Fsdes%25252Fdashboard")
      .formParam("pid", "Test")
      .formParam("usersGivenName", "test")
      .formParam("usersSurname", "test")
      .formParam("emailAddress", "t4est@gmail.com")
      .formParam("status", "true")
      .formParam("signature", "valid")
      .formParam("roles", "HMRC-SDES-REQUESTOR")
      .check(status.is(303))
      .check(header("Location").is(s"$baseUrl/customs-financials": String))
  }

  def getSdesLandingPage: HttpRequestBuilder = {
    http("Navigate to Sdes Landing Page")
      .get(s"$baseUrl/sdes/dashboard")
      .check(status.is(200))
  }

  def getCreateDER: HttpRequestBuilder = {
    http("Navigate to new request Page")
      .get(s"$baseUrl/sdes/create-request/select-request-type")
      .check(status.is(200))
      .check(regex("").exists)
      .check(RequestUtils.saveCsrfToken)
  }

  def postCreateDER: HttpRequestBuilder = {
    http("Navigate to new request Page")
      .post(s"$baseUrl/sdes/create-request/select-request-type")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("request-type", "electronic")
      .check(status.is(200))
      .check(header("Location").is(s"$baseUrl/sdes/create-request/select-template": String))
  }

  def getTemplatePage: HttpRequestBuilder = {
    http("Navigate to Template Page")
      .get(s"$baseUrl/sdes/create-request/select-template")
      .check(status.is(200))
      .check(regex("").exists)
      .check(RequestUtils.saveCsrfToken)
  }

  def postTemplatePage: HttpRequestBuilder = {
    http("Navigate to Template Page")
      .post(s"$baseUrl/sdes/create-request/select-template")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("data-exchange-template", "gms")
      .check(status.is(200))

  }

}
