package camli

import io.gatling.core.Predef._
import io.gatling.http.Headers.Names._
import io.gatling.http.Predef._
import scala.concurrent.duration._
import bootstrap._

object StatScenario {
  val headers = Map(
    "Keep-Alive" -> "115")

  def scn(duration: Duration) =
    scenario("Stats")
      .during(duration) {
        exec(
          http("check_stat")
            .get("/bs/camli/stat")
            .basicAuth(Options.username, Options.password)
            .queryParam("camliversion", "1")
            .headers(headers)
            .check(status.is(200)))
        .pause(Options.statsPauseMin, Options.statsPauseMax)
      }
}
