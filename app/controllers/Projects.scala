package controllers

import models.{TimeEntry, ProjectPost, Project}
import play.api.libs.json._
import play.api.mvc.Action
import scalikejdbc._

/**
 * Created by Thomas Meijers
 * Example REST API ScalaDays 2015
 * Projects controller
 */
object Projects extends BaseController {

  def getProjects = Action { implicit request =>
    val projects = Project.findAll()

    val projectsJson = Json.obj("projects" ->
      projects.map(p => addSelfLink(Json.toJson(p), routes.Projects.getProject(p.id)))
    )

    Ok(projectsJson)
  }

  def getProject(id: Int) = Action { implicit request =>
    Project.find(id) match {
      case Some(project) =>
        val projectJson = addSelfLink(Json.toJson(project), routes.Projects.getProject(id))

        val projectJsonWithLinks = projectJson ++ Json.obj("links" -> Seq(
          createLink("time-entries", routes.Projects.getTimeEntriesForProject(id))
        ))

        Ok(projectJsonWithLinks)
      case None => NotFound(errorJson("The requested resource could not be found"))
    }
  }

  def createProject = Action(parse.json) { implicit request =>
    request.body.validate[ProjectPost].fold(
      errors => BadRequest(errorJson(errors)),
      projectData => {
        val project = projectData.create

        Created.withHeaders(LOCATION -> routes.Projects.getProject(project.id).absoluteURL())
      }
    )
  }

  def editProject(id: Int) = Action(parse.json) { implicit request =>
    request.body.validate[ProjectPost].fold(
      errors => BadRequest(errorJson(errors)),
      projectData => {
        Project.find(id) match {
          case Some(project) => projectData.update(project.id); NoContent
          case None => NotFound(errorJson("The requested resource could not be found"))
        }
      }
    )
  }

  def deleteProject(id: Int) = Action(parse.empty) { implicit request =>
    Project.find(id) match {
      case Some(project) => project.destroy(); NoContent
      case None => NotFound(errorJson("The requested resource could not be found"))
    }
  }

  def searchProjects(projectName: String) = Action { implicit request =>
    val projects = Project.findAllBy(sqls.like(Project.p.projectName, s"%$projectName%"))

    val projectsJson = Json.obj("projects" ->
      projects.map(p => addSelfLink(Json.toJson(p), routes.Projects.getProject(p.id)))
    )

    Ok(projectsJson)
  }

  def getTimeEntriesForProject(id: Int) = Action { implicit request =>
    Project.find(id) match{
      case Some(project) =>
        val timeEntries = TimeEntry.findAllBy(sqls.eq(TimeEntry.te.projectId, id))

        val timeEntriesJson = Json.obj("time-entries" -> Seq(
           timeEntries.map(te => addSelfLink(Json.toJson(te), routes.TimeEntries.getTimeEntry(te.id)))
        ))

        Ok(timeEntriesJson)
      case None => NotFound(errorJson("The requested resource could not be found"))
    }
  }
}
