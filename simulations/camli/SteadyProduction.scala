package camli

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._
import io.gatling.http.Headers.Names._
import scala.concurrent.duration._

class SteadyProductionSimulation extends CamlistoreSimulation {
  val statsDuration = 6 minutes // Max of upload/retrieve time

  val uploadDuration = 6 minutes // Lets simulate production for 5 minutes
  val uploadWait = 0 seconds // It is already warmed up
  val uploadUsers = 10 users // Approximate 20 concurrent uploads happening at any given time
  val uploadRampTime = 0 seconds
  val uploadPauseMin = 500 milliseconds // Roughly a upload once a second
  val uploadPauseMax = 1000 milliseconds

  val retrieveDuration = 5 minutes
  val retrieveWait = 0 seconds
  val retrieveUsers = 400 users // Approximate prod load
  val retrieveRampTime = 60 seconds // Realistic time for prod load to wake up
  val retrievePauseMin = 400 milliseconds // Roughly a download thrice a second
  val retrievePauseMax = 600 milliseconds

  setUp(StatScenario.scn(statsDuration).inject(nothingFor(Options.statsWait), ramp(Options.statsUsers) over (Options.statsRampTime)),
    UploadScenario.scn(uploadDuration, uploadPauseMin, uploadPauseMax).inject(nothingFor(uploadWait), ramp(uploadUsers) over (uploadRampTime)),
    RetrieveScenario.scn(retrieveDuration, retrievePauseMin, retrievePauseMax).inject(nothingFor(retrieveWait), ramp(retrieveUsers) over (retrieveRampTime)))
    .protocols(httpProtocol)
}
