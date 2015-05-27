package core

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SQLContext
import com.typesafe.config.{ ConfigFactory, Config }


object SparkConfig {

  val config = ConfigFactory.load()

  @transient val sc = new SparkConf()
    .setMaster(config.getString("application.spark.master-uri"))
    .setAppName(config.getString("application.spark.app-name"))

  val sparkContext = new SparkContext(sc)
  val sqlContext = new SQLContext(sparkContext)

  def init() = {
    // this loads the json data and creates a temporary table used for SQL later
    val dataFrame = sqlContext.jsonFile("src/main/resources/data.json")
    dataFrame.registerTempTable("godzilla")
    dataFrame.cache()
  }

}
