package services

import scala.util.Try
import common.AppGlobal._

case class LocationData(deviation: Int)
case class Location(
    depth: Double, temperature: Double, cast: Long, cruise: String, latitude: Double, longitude: Double)

/**
  * Our Godzilla actor receives a message about locationdata -
  * specifically it receives an int specifying the deviation from the average
  * ocean temperature for a given depth. Based on that parameter,
  * it retrieves potential Godzilla locations.
  */
object Godzilla {

  /**
  * SearchActions use a Try pattern to retrieve data from Spark and
  * map the results to Location case classes.
  * Try will contain either a Success or Failure class.
  */
trait SearchActions {

  def getLocationData(deviation: Int): Try[List[Location]] = {
    val query = s"""
    SELECT
      T1.depth, T1.temperature, T1.castNumber, T1.cruiseId, T1.latitude, T1.longitude
    FROM (
     SELECT depth, temperature, castNumber, cruiseId,
       latitude, longitude from godzilla) AS T1
     JOIN (
       SELECT avg(temperature) as average, depth from godzilla group by depth
     )  AS T2
     ON T1.depth = T2.depth
     WHERE T1.temperature > T2.average + $deviation
    """

    Try {
      val dataFrame = SparkConfig.sqlContext.sql(query)

      dataFrame.map(row => Location(
        row.getDouble(0),
        row.getDouble(1),
        row.getLong(2),
        row.getString(3),
        row.getDouble(4),
        row.getDouble(5)
      )).collect().toList
    }

  }
}

}


