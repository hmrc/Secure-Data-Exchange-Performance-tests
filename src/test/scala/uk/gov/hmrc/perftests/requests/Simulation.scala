package uk.gov.hmrc.perftests.requests

import uk.gov.hmrc.performance.simulation.PerformanceTestRunner
import uk.gov.hmrc.perftests.requests.Requests._

class Simulation extends PerformanceTestRunner {

  setup("Sdes Page", "Navigate to sdes  page") withRequests (
    getLoginPage,
    postStrideAuthPage,
    postSamlResponse,
    getSdesLandingPage,
    postCreateDER,
    getRequestType,
    postRequestType,
    getSelectTemplate,
    postSelectTemplate,
    getFrequency,
    postFrequency,
////    getPidSearch,
////    postPidSearch,
    getConfirmDetails,
    postConfirmDetails,
    getDataRecipients,
    postDataRecipients,
    getRecipientDetails,
    postRecipientDetails,
    getRequestName,
    postRequestName,
    getCheckYourAnswers,
    postCheckyouranswers,
    getApprovalPage
  )

//  setup("login", "Login") withRequests (navigateToLoginPage, submitLogin)
//
//  setup("home", "Go to the homepage") withRequests navigateToHome

  runSimulation()
}