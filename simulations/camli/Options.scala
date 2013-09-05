package camli

import scala.concurrent.duration._
import io.gatling.core.Predef._
import io.gatling.http.Predef._

object Options {
  val endpoint = sys.env("GATLING_ENDPOINT")

  val username = sys.env("GATLING_USERNAME")
  var password = sys.env("GATLING_PASSWORD")

  val statsDuration = 5 minutes
  val statsWait = 0 seconds
  val statsUsers = 10 users
  val statsRampTime = 0 seconds
  val statsPauseMin = 500 milliseconds
  val statsPauseMax = 1000 milliseconds
}
