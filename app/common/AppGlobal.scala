package common

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SQLContext
import play.GlobalSettings
import play.api.Logger


object AppGlobal extends GlobalSettings {

  object SparkConfig {

    @transient val sparkConf =new SparkConf()
      .setMaster("local[4]")
      .setAppName("gds")

    val sparkContext = new SparkContext(sparkConf)
    val sqlContext = new SQLContext(sparkContext)


  }

  /**
    */
  def onStart() {

    Logger.info("Hello World")
    val dataFrame = SparkConfig.sqlContext.jsonFile("conf/data.json")
    dataFrame.registerTempTable("godzilla")
    dataFrame.cache()
  }

}

