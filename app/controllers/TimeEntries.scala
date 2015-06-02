package controllers

import models._
import org.joda.time.DateTime
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import utils.JsonUtils

/**
 * Created by ThomasWorkBook on 30/05/15.
 */
class TimeEntries extends Controller with JsonUtils{

  def getTimeEntries = Action { implicit request =>
    val timeEntries = TimeEntry.findAll()

    val timeEntriesJson = Json.obj("time-entries" ->
      timeEntries.map{ te =>
        val timeEntryData = TimeEntryData.fromTimeEntry(te)
        addSelfLink(Json.toJson(timeEntryData), routes.TimeEntries.getTimeEntry(te.id))
      }
    )

    Ok(timeEntriesJson)
  }

  def getTimeEntry(id: Int) = Action { implicit request =>
    TimeEntry.find(id) match{
      case Some(timeEntry) =>
        val timeEntryData = TimeEntryData.fromTimeEntry(timeEntry)

        val timeEntryJson = addSelfLink(Json.toJson(timeEntryData), routes.TimeEntries.getTimeEntry(id))

        val timeEntryJsonWithLinks = timeEntryJson ++ Json.obj("links" -> Seq(
          createLink("employee", routes.Employees.getEmployee(timeEntry.employeeId)),
          createLink("project", routes.Projects.getProject(timeEntry.projectId))
        ))

        Ok(timeEntryJsonWithLinks)
      case None => NotFound(errorJson("The requested resource could not be found"))
    }
  }

  def createTimeEntry = Action(parse.json) { implicit request =>
    request.body.validate[TimeEntryData].fold(
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
    request.body.validate[TimeEntryData].fold(
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

  def deleteTimeEntry(id: Int) = Action(parse.empty) { implicit request =>
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
          timeEntries.map{ te =>
            val timeEntryData = TimeEntryData.fromTimeEntry(te)
            addSelfLink(Json.toJson(timeEntryData), routes.TimeEntries.getTimeEntry(te.id))
          }
        )

        Ok(timeEntriesJson)
      }
      )
  }
}
