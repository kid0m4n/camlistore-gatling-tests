package camli

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._
import io.gatling.http.Headers.Names._
import scala.concurrent.duration._

class ReadPerformanceSimulation extends CamlistoreSimulation {
  val statsDuration = 7 minutes // Max of upload/retrieve time

  val retrieveDuration = 5 minutes
  val retrieveWait = 0 seconds
  val retrieveUsers = 600 users // Approximate prod load
  val retrieveRampTime = 120 seconds // Realistic time for prod load to wake up
  val retrievePauseMin = 300 milliseconds // Roughly a download >= thrice a second
  val retrievePauseMax = 600 milliseconds

  setUp(StatScenario.scn(statsDuration).inject(nothingFor(Options.statsWait), ramp(Options.statsUsers) over (Options.statsRampTime)),
    RetrieveScenario.scn(retrieveDuration, retrievePauseMin, retrievePauseMax).inject(nothingFor(retrieveWait), ramp(retrieveUsers) over (retrieveRampTime)))
    .protocols(httpProtocol)
}
