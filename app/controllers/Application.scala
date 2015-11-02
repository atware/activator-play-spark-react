package controllers

import common.AppGlobal._
import play.api.libs.json._
import play.api.mvc._

case class Location(year: Int, month: Int, depth: Int, latitude: String, longitude: String, temperature: String)

trait ApplicationController {
  this: Controller =>

  def index = Action {
    Ok(views.html.index())
  }

}

trait LocationController {
  this: Controller =>

  def getLocationData(): List[Location] = {

    val query = s"""
    select YEAR, MONTH, DEPTH, LATITUDE, LONGITUDE, TEMPERATURE from godzilla
    """

    val dataFrame = SparkConfig.sqlContext.sql(query)

    dataFrame.map(row => Location(
      row.getInt(0),
      row.getInt(1),
      row.getInt(2),
      row.getString(3),
      row.getString(4),
      row.getString(5)
    )).collect().toList
  }

  def locations() = Action { implicit request =>
    implicit val locationToJson = new Writes[Location] {
      def writes(location: Location) = Json.obj(
        "year" -> location.year,
        "month" -> location.month,
        "depth" -> location.depth,
        "latitude" -> location.latitude,
        "longitude" -> location.longitude,
        "temperature" -> location.temperature
      )
    }

    Ok(Json.toJson(getLocationData()))
  }
  

}

object Application extends Controller with ApplicationController with LocationController
