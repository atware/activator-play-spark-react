package common

import com.typesafe.config.{ConfigFactory, Config}
import net.ceedubs.ficus.Ficus._
import net.ceedubs.ficus.readers.ValueReader
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SQLContext
import play.api._
import scalikejdbc._

case class LatLong(lat: String, long: String)
case class Depth(value: String, range: String)

object AppGlobal extends GlobalSettings {

  /**
   * Consolidate Spark into a configuration object
   */
  object SparkConfig {

    @transient val sparkConf =new SparkConf()
      .setMaster("local[2]")
      .setAppName("gds")

    val sparkContext = new SparkContext(sparkConf)
    val sqlContext = new SQLContext(sparkContext)

  }

  /**
   * On start load the json data from conf/data.json into in-memory Spark
   */
  override def onStart(app: Application) {
    Database.load
//    val dataFrame = SparkConfig.sqlContext.read.json("conf/data.json")
//    dataFrame.registerTempTable("godzilla")
//    dataFrame.cache()
  }

}

object Database {

  val latLongReader: ValueReader[List[LatLong]] = ValueReader.relative { config =>
    val entries = config.as[List[String]]("latlongs")
    entries.map { ll =>
      val digits = ll.split(":")
      LatLong(digits(0), digits(1))
    }
  }


  def load(): Unit = {
    Class.forName("org.h2.Driver")
    ConnectionPool.singleton("jdbc:h2:mem:japan_ocean", "user", "pass")
    implicit val session = AutoSession

    implicit val reader: ValueReader[List[LatLong]] = latLongReader

    val config: Config = ConfigFactory.load()
    val years = 1 to config.as[Int]("application.years")
    val months = 1 to 12
    val depths = config.as[List[Int]]("application.depths")
    val latlongs = config.as[List[LatLong]]("application")

    for( year <- years) {
      for( depth <- depths) {
        for( month <- months ) {
          for( latlong <- latlongs ) {
            println("Year: " + year + " Month: " + month + " Depth: " + depth + " LatLong: " + latlong)
          }
        }
      }
    }

//    sql"""
//    CREATE TABLE metadata_region (
//      id SERIAL PRIMARY KEY NOT NULL,
//      name VARCHAR(200) NOT NULL)
//    """.execute.apply()
  }

}
