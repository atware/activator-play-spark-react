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
//    val dataFrame = SparkConfig.sqlContext.read.json("conf/data.json")
//    dataFrame.registerTempTable("godzilla")
//    dataFrame.cache()
  }

}


