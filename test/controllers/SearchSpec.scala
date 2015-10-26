package controllers

import common.AppGlobal.SparkConfig
import org.scalatest._
import org.scalatestplus.play._
import play.api.mvc._
import play.api.test.Helpers._
import play.api.test._

import scala.concurrent.Future

class SearchSpec extends PlaySpec with Results with SparkBuilder {

  class TestController() extends Controller with LocationController

  "Location api" should {

    "be valid" in {
      val controller = new TestController()
      val result: Future[Result] = controller.locations(22).apply(FakeRequest())
      status(result) mustEqual OK
    }

  }

}

trait SparkBuilder extends BeforeAndAfterEach { this: Suite =>
  
  override def beforeEach() {

    val stringRDD = SparkConfig.sparkContext.parallelize(
      Seq("""{"$type": "noaa.parser.Entry","castNumber": 10071185,"cruiseId": "JP-0","date": "2000-01-12","latitude": 34.5667,"longitude": 139.8667,"depth": 0.0,"temperature": 17.94}""",
        """{"$type": "noaa.parser.Entry","castNumber": 10071185,"cruiseId": "JP-0","date": "2000-01-12","latitude": 34.5667,"longitude": 139.8667,"depth": 50.0,"temperature": 17.83}""",
        """{"$type": "noaa.parser.Entry","castNumber": 10071185,"cruiseId": "JP-0","date": "2000-01-12","latitude": 34.5667,"longitude": 139.8667,"depth": 50.0,"temperature": 20.78}""")
    )
    val df = SparkConfig.sqlContext.read.json(stringRDD)
    df.registerTempTable("godzilla")    

  }

  override def afterEach() {
    SparkConfig.sparkContext.stop()
  }

}
