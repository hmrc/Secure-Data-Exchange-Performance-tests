package uk.gov.hmrc.perftests.requests

import uk.gov.hmrc.performance.simulation.PerformanceTestRunner
import uk.gov.hmrc.perftests.requests.HelloWorldRequests._

class HelloWorldSimulation extends PerformanceTestRunner {

  setup("login", "Login") withRequests (navigateToLoginPage, submitLogin)

  setup("home", "Go to the homepage") withRequests navigateToHome

  runSimulation()
}