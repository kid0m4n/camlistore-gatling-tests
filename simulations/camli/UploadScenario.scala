package camli

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._
import bootstrap._

object UploadScenario {
  val headers = Map(
    "Keep-Alive" -> "115",
    "Content-Type" -> "multipart/form-data")

  def scn(duration: Duration, pauseMin: Duration, pauseMax: Duration) =
    scenario("Upload File")
      .feed(csv("files_for_upload.csv").circular)
      .during(duration) {
        exec(
          http("upload_file")
            .post("/bs/camli/upload")
            .basicAuth(Options.username, Options.password)
            .headers(headers)
            .bodyPart(RawFileBodyPart("sha1-${sha}", "${filename}", "application/octet-stream").withContentId("sha1-${sha}").withFileName("${filename}"))
            .check(status.is(200)))
        .pause(pauseMin, pauseMax)
      }
}
