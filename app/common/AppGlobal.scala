package common

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SQLContext
import play.api._

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
  override def onStart(app: Application) {

    val dataFrame = SparkConfig.sqlContext.jsonFile("conf/data.json")
    dataFrame.registerTempTable("godzilla")
    dataFrame.cache()
  }

}

