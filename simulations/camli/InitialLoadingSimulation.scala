package camli

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._
import io.gatling.http.Headers.Names._
import scala.concurrent.duration._

class InitialLoadingSimulation extends CamlistoreSimulation {
  val statsDuration = 6 minutes // Max of upload/retrieve time

  val uploadDuration = 3 minutes // Excercise time
  val uploadWait = 15 seconds // Let the system warm up for 15 seconds
  val uploadUsers = 100 users // This is bottlenecked by our capacity to download content from client
  val uploadRampTime = 60 seconds // Realistic time
  val uploadPauseMin = 100 milliseconds // An upload twice a second (roughly)
  val uploadPauseMax = 500 milliseconds

  val retrieveDuration = 3 minutes
  val retrieveWait = 120 seconds // Wait for some time for the initial loading to get data into system
  val retrieveUsers = 200 users // This is during initial ingress, system is still rocky
  val retrieveRampTime = 30 seconds // Realistic system start grace time
  val retrievePauseMin = 200 milliseconds // Roughly a download thrice a second
  val retrievePauseMax = 400 milliseconds

  setUp(StatScenario.scn(statsDuration).inject(nothingFor(Options.statsWait), ramp(Options.statsUsers) over (Options.statsRampTime)),
    UploadScenario.scn(uploadDuration, uploadPauseMin, uploadPauseMax).inject(nothingFor(uploadWait), ramp(uploadUsers) over (uploadRampTime)),
    RetrieveScenario.scn(retrieveDuration, retrievePauseMin, retrievePauseMax).inject(nothingFor(retrieveWait), ramp(retrieveUsers) over (retrieveRampTime)))
    .protocols(httpProtocol)
}
