package controllers

import org.specs2.mutable._
import org.specs2.runner._
import play.api.mvc._
import play.api.test._
import play.api.test.Helpers._

import scala.concurrent.{Await, ExecutionContext}

class ApplicationSpec extends Specification {

  "Application should" should {

    "return landing page on /" in {

      val result = Application.index(FakeRequest(GET, "/"))

      status(result) mustEqual OK
    }
  }
}
