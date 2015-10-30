package controllers

import com.typesafe.config.{ConfigFactory, Config}
import common.LatLong
import common.Database._
import net.ceedubs.ficus.readers.ValueReader
import scala.concurrent.Future
import org.scalatest._
import org.scalatestplus.play._
import net.ceedubs.ficus.Ficus._
import play.api.mvc._
import play.api.test._
import play.api.test.Helpers._

class ApplicationSpec extends PlaySpec with Results {

  class TestController() extends Controller with ApplicationController

  "Application Page#index" should {

    "should be valid" in {
      val controller = new TestController()
      val result: Future[Result] = controller.index().apply(FakeRequest())
      status(result) mustEqual OK

    }

    "data loading should" in {

      implicit val reader: ValueReader[List[LatLong]] = latLongReader
      val config: Config = ConfigFactory.load()
      val years = 1 to config.as[Int]("application.years")
      val depths = config.as[List[Int]]("application.depths")
      val latlongs = config.as[List[LatLong]]("application")
      latlongs.size mustEqual 2
    }

  }

}
