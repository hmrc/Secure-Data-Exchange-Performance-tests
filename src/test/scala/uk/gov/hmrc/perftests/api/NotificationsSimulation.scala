package uk.gov.hmrc.perftests.api

import io.gatling.core.structure.ChainBuilder
import uk.gov.hmrc.performance.api.APIPerformanceTest
import uk.gov.hmrc.performance.api.models.PerformanceTest
import uk.gov.hmrc.performance.conf.JourneyConfiguration
import uk.gov.hmrc.perftests.api.NotificationRequests._

class TransferNotificationsSimulation extends APIPerformanceTest with JourneyConfiguration {

  override val performanceTest: PerformanceTest = PerformanceTest(
    title = "Secure-Data-Exchange-Notifications",
    apis = ChainBuilder.chainOf(postTransferCompleteNotification),
    scope = "write:transfer-complete",
    feeder = "data/transfernotifications.csv"
  )

  // Allow non-API tests to be run when this isn't specified in `journeysToRun'
  override protected def runSimulation(): SetUp = {
    if (journeysAvailable.contains(performanceTest.title)) {
      super.runSimulation()
    } else {
      setUp(Seq.empty: _*)
    }
  }

  runSimulation()

}
