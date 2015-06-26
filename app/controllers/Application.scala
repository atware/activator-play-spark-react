package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json.Json
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits._

object Application extends Controller {

  def index = Action {
    Ok(views.html.index())
  }

  def locations(deviation:Int) = Action.async { implicit request =>
    Future.successful(Ok(Json.obj(
      "id" -> 1,
      "a" -> 2,
      "b" -> 3,
      "c" -> 4
    )))

  }
}
