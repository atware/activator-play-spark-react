package api

import core.DefaultTimeout
import akka.actor.ActorSystem
import spray.routing.Directives
import spray.httpx.TwirlSupport
import spray.httpx.encoding.Gzip
import service.{LocationFormats, LocationData, Location}
import akka.pattern.ask
import scala.util.Try

/**
  * Sample API for the Godzilla Prediction System
  * Each api path is in a separate spray 'path' directive for easier management.
  * LocationFormats provide implicit JSON marshalling,
  * TwirlSupport provides twirl templates
  * DefaultTimeout provide a base timeout
  */
class GodzillaApi(implicit val actorSystem: ActorSystem) extends Directives with DefaultTimeout with TwirlSupport with LocationFormats {

  import scala.concurrent.ExecutionContext.Implicits.global
  val godzillaActor = actorSystem.actorSelection("/user/gds/godzilla")

  // home page, retrieves compiled twirl template
  val index = path("") {
    get {
      complete {
        html.index()
      }
    }
  }

  // does not block
  val locations = path("locations" / IntNumber) { deviation =>
    get {
      complete {
        (godzillaActor ? LocationData(deviation)).mapTo[Try[List[Location]]]
      }
    }
  }

  // for webjar javascript dependencies
  val webjars = pathPrefix("webjars") {
    get {
      getFromResourceDirectory("META-INF/resources/webjars")
    }
  }

  val routes = index ~ locations ~ webjars ~ getFromResourceDirectory("assets")
}
