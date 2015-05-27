package api

import org.specs2.mutable._
import spray.testkit.Specs2RouteTest
import service.{LocationFormats, Location}
import spray.http.MediaTypes._

class GodzillaApiSpec extends Specification with Specs2RouteTest with LocationFormats {

  def actorRefFactory = system
  def route = new GodzillaApi().locations

  "GodzillaService" should {

    "get locations for a given deviation from temperature average" in {
      Get("/locations/" + 1) ~> route ~> check {
        println("ok")
      }
    }
  }

}
