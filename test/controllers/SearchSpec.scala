package controllers

import scala.concurrent.Future

import org.scalatest._
import org.scalatestplus.play._

import play.api.mvc._
import play.api.test._
import play.api.test.Helpers._

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SQLContext

import org.apache.log4j.{Logger, Level}

import common.AppGlobal.SparkConfig

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

  import scala.concurrent.ExecutionContext.Implicits.global
  
  override def beforeEach() {

    val stringRDD = SparkConfig.sparkContext.parallelize(
      Seq("""{"$type": "noaa.parser.Entry","castNumber": 10071185,"cruiseId": "JP-0","date": "2000-01-12","latitude": 34.5667,"longitude": 139.8667,"depth": 0.0,"temperature": 17.94}""",
        """{"$type": "noaa.parser.Entry","castNumber": 10071185,"cruiseId": "JP-0","date": "2000-01-12","latitude": 34.5667,"longitude": 139.8667,"depth": 50.0,"temperature": 17.83}""",
        """{"$type": "noaa.parser.Entry","castNumber": 10071185,"cruiseId": "JP-0","date": "2000-01-12","latitude": 34.5667,"longitude": 139.8667,"depth": 50.0,"temperature": 20.78}""")
    )
    val df = SparkConfig.sqlContext.jsonRDD(stringRDD)
    df.registerTempTable("godzilla")    

  }

  override def afterEach() {
    SparkConfig.sparkContext.stop()
  }

  private def silenceLogs(level: Level, loggers: TraversableOnce[String]) = {
    loggers.foreach { name => Logger.getLogger(name).setLevel(level) }
  }

}
