package controllers

import play.api.cache.Cached
import play.api.libs.json.{JsArray, Json}
import play.api.mvc._
import scala.concurrent.duration._
import play.api.Play.current

/**
 * Created by Thomas Meijers
 * Example REST API ScalaDays 2015
 * Controller for the index
 */
object Application extends BaseController {

  def index = Cached.status(_ => "apiIndex", OK, 7 days) {
    Action { implicit request =>

      val indexJson = Json.obj(
        "version" -> "0.1",
        "links" -> Seq(
          Json.obj("rel" -> "projects", "href" -> routes.Projects.getProjects().absoluteURL()),
          Json.obj("rel" -> "employees", "href" -> routes.Employees.getEmployees().absoluteURL()),
          Json.obj("rel" -> "time-entries","href" -> routes.TimeEntries.getTimeEntries().absoluteURL())
        )
      )

      Ok(indexJson)
    }
  }

}