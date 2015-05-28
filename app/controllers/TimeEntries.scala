package controllers

import models.{TimeEntryPost, TimeEntry}
import org.joda.time.DateTime
import play.api.libs.json._
import play.api.mvc.Action
import models.TimeEntry.timeEntryWrites
import models.TimeEntryPost.timeEntryDataReads

/**
 * Created by Thomas Meijers
 * Example REST API ScalaDays 2015
 * TimeEntries controller
 */
object TimeEntries extends BaseController{

  def getTimeEntries = Action { implicit request =>
    val timeEntries = TimeEntry.findAll()

    val timeEntriesJson = Json.obj("time-entries" ->
      timeEntries.map(te => addSelfLink(Json.toJson(te), routes.TimeEntries.getTimeEntry(te.id)))
    )

    Ok(timeEntriesJson)
  }

  def getTimeEntry(id: Int) = Action { implicit request =>
    TimeEntry.find(id) match{
      case Some(timeEntry) =>
        val timeEntryJson = addSelfLink(Json.toJson(timeEntry), routes.TimeEntries.getTimeEntry(id))

        val timeEntryJsonWithLinks = timeEntryJson ++ Json.obj("links" -> Seq(
          createLink("employee", routes.Employees.getEmployee(timeEntry.employeeId)),
          createLink("project", routes.Projects.getProject(timeEntry.projectId))
        ))

        Ok(timeEntryJsonWithLinks)
      case None => NotFound(errorJson("The requested resource could not be found"))
    }
  }

  def createTimeEntry = Action(parse.json) { implicit request =>
    request.body.validate[TimeEntryPost].fold(
      errors => BadRequest(errorJson(errors)),
      timeEntryData =>
        if(!timeEntryData.checkTime)
          BadRequest(errorJson("Start time needs to be before end time"))
        else{
          val timeEntry = timeEntryData.create
          Created.withHeaders(LOCATION -> routes.TimeEntries.getTimeEntry(timeEntry.id).absoluteURL())
        }
    )
  }

  def editTimeEntry(id: Int) = Action(parse.json) { implicit request =>
    request.body.validate[TimeEntryPost].fold(
      errors => BadRequest(errorJson(errors)),
      timeEntryData =>
        if(!timeEntryData.checkTime)
          BadRequest(errorJson("Start time needs to be before end time"))
        else
          TimeEntry.find(id) match{
            case Some(timeEntry) => timeEntryData.update(timeEntry.id); NoContent
            case None => NotFound(errorJson("The request resource could not be found"))
          }
    )
  }

  def deleteTimeEntry(id: Int) = Action { implicit request =>
    TimeEntry.find(id) match{
      case Some(timeEntry) => timeEntry.destroy(); NoContent
      case None => NotFound(errorJson("The requested resource could not be found"))
    }
  }

  def searchTimeEntries(from: Option[DateTime], to: Option[DateTime]) = Action { implicit request =>
    val timeEntriesOrError = TimeEntry.findFromAndTo(from, to)

    timeEntriesOrError fold(
      error => BadRequest(errorJson(error)),
      timeEntries => {
        val timeEntriesJson = Json.obj("time-entries" ->
          timeEntries.map(te => addSelfLink(Json.toJson(te), routes.TimeEntries.getTimeEntry(te.id)))
        )

        Ok(timeEntriesJson)
      }
    )
  }

}
