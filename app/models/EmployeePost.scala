package models

import org.joda.time.LocalDate
import play.api.libs.json.Json

/**
 * Created by Thomas Meijers
 * Example REST API ScalaDays 2015
 * Case class for post data of an Employee
 */
case class EmployeePost (
  firstName: String,
  prefix: Option[String] = None,
  lastName: String,
  jobTitle: String,
  birthDate: LocalDate) extends PostData[Employee]{

  def create: Employee = Employee.create(firstName, prefix, lastName, jobTitle, birthDate)

  def update: Int => Employee =
    id => Employee(id, firstName, prefix, lastName, jobTitle, birthDate).save()

}

object EmployeePost {
  implicit val employeeDataReads = Json.reads[EmployeePost]
}