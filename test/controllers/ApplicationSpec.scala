package controllers

import scala.concurrent.Future
import org.scalatest._
import org.scalatestplus.play._

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

  }

}
