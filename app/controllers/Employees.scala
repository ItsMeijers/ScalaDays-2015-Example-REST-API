package controllers

import models.{Project, TimeEntry, EmployeePost, Employee}
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import utils.JsonUtils

/**
 * Created by ThomasWorkBook on 30/05/15.
 */
class Employees extends Controller with JsonUtils{

  def getEmployees = Action { implicit request =>
    val employees = Employee.findAll()

    val employeesJson = Json.obj("employees" ->
      employees.map(e => addSelfLink(Json.toJson(e), routes.Employees.getEmployee(e.id)))
    )

    Ok(employeesJson)
  }

  def getEmployee(id: Int) = Action { implicit request =>
    Employee.find(id) match{
      case Some(employee) =>
        val employeeJson = addSelfLink(Json.toJson(employee), routes.Employees.getEmployee(id))

        val employeeJsonWithLinks = employeeJson ++ Json.obj("links" -> Seq(
          createLink("time-entries", routes.Employees.getTimeEntriesForEmployee(id)),
          createLink("projects", routes.Employees.getProjectsForEmployee(id))
        ))

        Ok(employeeJsonWithLinks)
      case None => NotFound(errorJson("The requested resource could not be found"))
    }
  }

  def createEmployee = Action(parse.json) { implicit request =>
    request.body.validate[EmployeePost].fold(
      errors => BadRequest(errorJson(errors)),
      employeeData => {
        val employee = employeeData.create

        Created.withHeaders(LOCATION -> routes.Employees.getEmployee(employee.id).absoluteURL())
      }
    )
  }

  def editEmployee(id: Int) = Action(parse.json) { implicit request =>
    request.body.validate[EmployeePost].fold(
      errors => BadRequest(errorJson(errors)),
      employeeData => {
        Employee.find(id) match {
          case Some(employee) => employeeData.update(id); NoContent
          case None => NotFound(errorJson("The requested resource could not be found"))
        }
      }
    )
  }

  def deleteEmployee(id: Int) = Action(parse.empty) { implicit request =>
    Employee.find(id) match {
      case Some(employee) =>
        TimeEntry.findByEmployee(id).foreach{ timeEntry =>
          timeEntry.destroy()
        }

        employee.destroy()

        NoContent
      case None => NotFound(errorJson("The requested resource could not be found"))
    }
  }

  def searchEmployees(firstNameOpt: Option[String], jobTitleOpt: Option[String]) = Action { implicit request =>
    val employees: Either[String, List[Employee]] = Employee.findByNameAndJobTitle(firstNameOpt, jobTitleOpt)

    employees.fold(
      error => BadRequest(errorJson(error)),
      employees => {
        val employeesJson = Json.obj("employees" ->
          employees.map(e => addSelfLink(Json.toJson(e), routes.Employees.getEmployee(e.id)))
        )

        Ok(employeesJson)
      }
    )
  }

  def getProjectsForEmployee(id: Int) = Action { implicit request =>
    Employee.find(id).map{ employee =>
      val projects = Project.findByEmployee(id)

      val projectsJson = Json.obj("projects" ->
        projects.map(p => addSelfLink(Json.toJson(p), routes.Projects.getProject(p.id)))
      )

      Ok(projectsJson)
    } getOrElse NotFound(errorJson("The requested resource could not be found"))

  }

  def getTimeEntriesForEmployee(id: Int) = Action { implicit request =>
    Employee.find(id).map{ employee =>
      val timeEntries = TimeEntry.findByEmployee(id)

      val timeEntriesJson = Json.obj("time-entries" ->
        timeEntries.map(te => addSelfLink(Json.toJson(te), routes.TimeEntries.getTimeEntry(te.id)))
      )

      Ok(timeEntriesJson)
    } getOrElse NotFound(errorJson("The requested resource could not be found"))
  }

}
