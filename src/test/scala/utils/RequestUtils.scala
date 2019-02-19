package utils

import io.gatling.core.Predef._
import io.gatling.http.Predef._

object RequestUtils {

  val csrfPattern = """name="csrfToken" value="([^"]+)""""
  val groupIdPattern = """name="groupId" value="([^"]+)""""

  def saveCsrfToken = regex(_ => csrfPattern).saveAs("csrfToken")
  def saveGroupId = regex(_ => groupIdPattern).saveAs("groupId")
}
