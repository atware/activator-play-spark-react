package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.functional.syntax._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits._
import common.AppGlobal._
import scala.util.Try

case class Location(
    depth: Double, temperature: Double, cast: Long, cruise: String, latitude: Double, longitude: Double)

trait ApplicationController {
  this: Controller =>

  def index = Action {
    Ok(views.html.index())
  }

}

trait LocationController {
  this: Controller =>

  def getLocationData(deviation: Int): List[Location] = {
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

  def locations(deviation:Int) = Action { implicit request =>
    implicit val locationToJson = new Writes[Location] {
      def writes(location: Location) = Json.obj(
        "depth" -> location.depth,
        "temperature" -> location.temperature,
        "cast" -> location.cast,
        "cruise" -> location.cruise,
        "latitude" -> location.latitude,
        "longitude" -> location.longitude
      )
    }

    Ok(

      Json.toJson(getLocationData(deviation))
    )

  }
  

}

object Application extends Controller with ApplicationController with LocationController
