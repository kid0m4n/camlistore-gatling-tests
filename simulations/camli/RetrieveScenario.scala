package camli

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._
import bootstrap._

object RetrieveScenario {
  val headers = Map(
    "Keep-Alive" -> "115")

  def scn(duration: Duration, pauseMin: Duration, pauseMax: Duration) =
    scenario("Retrieve File")
    .feed(csv("files_for_retrieval.csv").circular)
    .during(duration) {
      exec(
        http("retrieve_file")
          .get("/bs/camli/sha1-${sha}")
          .basicAuth(Options.username, Options.password)
          .headers(headers)
          .check(status.is(200)))
      .pause(pauseMin, pauseMax)
    }
}
