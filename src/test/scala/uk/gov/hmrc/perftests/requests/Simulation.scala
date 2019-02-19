package uk.gov.hmrc.perftests.requests

import uk.gov.hmrc.performance.simulation.PerformanceTestRunner
import uk.gov.hmrc.perftests.requests.Requests._

class Simulation extends PerformanceTestRunner {

  setup("Sdes Page", "Navigate to sdes  page") withRequests (
    getLoginPage,
    getStrideAuthLoginPage,
    postStrideAuthPage,
    getCreateDER,
    getSdesLandingPage
  )

//  setup("login", "Login") withRequests (navigateToLoginPage, submitLogin)
//
//  setup("home", "Go to the homepage") withRequests navigateToHome

  runSimulation()
}