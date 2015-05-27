package service

import org.apache.log4j.{Logger, Level}
import org.apache.spark.sql.SQLContext
import org.specs2.mutable._
import org.specs2.specification.core.{Fragments}
import org.specs2.specification.{Scope, Step, AroundEach}
import org.specs2.execute.{Result, AsResult}
import scala.concurrent.duration._
import scala.util.{Success, Failure, Try}
import core.SparkConfig._


class GodzillaServiceSpec extends Specification with SearchActions {

  sequential

  import scala.concurrent.ExecutionContext.Implicits.global

  "connects and retrieves correct results" >> {
    val stringRDD = sparkContext.parallelize(
      Seq("""{"$type": "noaa.parser.Entry","castNumber": 10071185,"cruiseId": "JP-0","date": "2000-01-12","latitude": 34.5667,"longitude": 139.8667,"depth": 0.0,"temperature": 17.94}""",
        """{"$type": "noaa.parser.Entry","castNumber": 10071185,"cruiseId": "JP-0","date": "2000-01-12","latitude": 34.5667,"longitude": 139.8667,"depth": 50.0,"temperature": 17.83}""",
        """{"$type": "noaa.parser.Entry","castNumber": 10071185,"cruiseId": "JP-0","date": "2000-01-12","latitude": 34.5667,"longitude": 139.8667,"depth": 50.0,"temperature": 20.78}""")
    )
    val df = sqlContext.jsonRDD(stringRDD)
    df.registerTempTable("godzilla")
    val locations = getLocationData(1).asInstanceOf[Success[List[Location]]]

    locations.isSuccess must beTrue
    locations.get.size must_== 1
    println("location results: " + locations); ok
  }

  "connects and retrieves a failure" >> {
    val stringRDD = sparkContext.parallelize(Seq("""{"name" : "bad", "message": "news"}"""))
    val df = sqlContext.jsonRDD(stringRDD)
    df.registerTempTable("godzilla")
    val locations = getLocationData(1);
    locations.isFailure must beTrue
    val failure = locations.asInstanceOf[Failure[List[Location]]]
    println(failure)
    println("again"); ok
  }

}


/**
  * silences Spark's rather verbose logging,
  * stops the spark context after each test
  */
trait SparkTestContext extends AroundEach {

  def around[R: AsResult](r: => R): Result = {
    silenceLogs(Level.WARN, Seq("org.apache.spark", "org.eclipse.jetty", "akka"))
    try AsResult(r)
    finally destroyContext
  }

  private def destroyContext() {
    sparkContext.stop()
  }

  private def silenceLogs(level: Level, loggers: TraversableOnce[String]) = {
    loggers.foreach { name => Logger.getLogger(name).setLevel(level) }
  }
}
