package common

import com.typesafe.config.{ConfigFactory, Config}
import database.Database

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SQLContext
import play.api._
import scalikejdbc._



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
    val props = new java.util.Properties()
    props.put("user","user")
    props.put("password","pass")
    props.put("driver", "org.h2.Driver")
    val dataFrame =SparkConfig.sqlContext.read.jdbc(
      "jdbc:h2:mem:japan_ocean",
      "(select year, month, depth, latitude, longitude, temperature from depth_measurements) as d",
    props)
    dataFrame.registerTempTable("godzilla")
    dataFrame.cache()
  }

}


