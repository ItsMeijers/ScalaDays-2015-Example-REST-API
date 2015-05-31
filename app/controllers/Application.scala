package controllers

import javax.inject.Inject

import play.api.cache.Cached
import play.api.libs.json.Json
import play.api.mvc._

class Application @Inject() (cached: Cached) extends Controller {

  def index = cached.status(_ => "apiIndex", OK, 5000) {
    Action { implicit request =>
      val json = Json.obj(
        "version" -> "0.1",
        "links" -> Seq(
          Json.obj("rel" -> "projects", "href" -> routes.Projects.getProjects().absoluteURL()),
          Json.obj("rel" -> "employees", "href" -> routes.Employees.getEmployees().absoluteURL()),
          Json.obj("rel" -> "time-entries","href" -> routes.TimeEntries.getTimeEntries().absoluteURL())
        )
      )
      Ok(json)
    }
  }

}
