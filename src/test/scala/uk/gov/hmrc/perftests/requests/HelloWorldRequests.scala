//package uk.gov.hmrc.perftests.requests
//
//import uk.gov.hmrc.performance.conf.ServicesConfiguration
//
//import io.gatling.core.Predef._
//import io.gatling.http.Predef._
//
//object HelloWorldRequests extends ServicesConfiguration {
//
//  val baseUrl = baseUrlFor("hello-world-frontend")
//
//  val navigateToLoginPage =
//    http("Navigate to Login Page")
//      .get(s"$baseUrl/login")
//      .check(status.is(200))
//
//
//  val submitLogin = {
//    http("Submit username and password")
//      .post(s"$baseUrl/login": String)
//      .formParam("userId", "${username}")
//      .formParam("password", "${password}")
//      .check(status.is(303))
//      .check(header("Location").is("/home"))
//  }
//
//  val navigateToHome =
//    http("Navigate To Home")
//      .get(s"$baseUrl/home")
//      .check(status.is(200))
//
//}
