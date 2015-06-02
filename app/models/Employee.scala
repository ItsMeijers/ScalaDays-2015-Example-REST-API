package models

import play.api.libs.functional.syntax._
import play.api.libs.json.JsPath
import scalikejdbc._
import org.joda.time.{LocalDate}

case class Employee(
  id: Int,
  firstName: String,
  prefix: Option[String] = None,
  lastName: String,
  jobTitle: String,
  birthDate: LocalDate) {

  def save()(implicit session: DBSession = Employee.autoSession): Employee = Employee.save(this)(session)

  def destroy()(implicit session: DBSession = Employee.autoSession): Unit = Employee.destroy(this)(session)

}


object Employee extends SQLSyntaxSupport[Employee] {

  override val tableName = "employee"

  override val columns = Seq("id", "first_name", "prefix", "last_name", "job_title", "birth_date")

  def apply(e: SyntaxProvider[Employee])(rs: WrappedResultSet): Employee = apply(e.resultName)(rs)
  def apply(e: ResultName[Employee])(rs: WrappedResultSet): Employee = new Employee(
    id = rs.get(e.id),
    firstName = rs.get(e.firstName),
    prefix = rs.get(e.prefix),
    lastName = rs.get(e.lastName),
    jobTitle = rs.get(e.jobTitle),
    birthDate = rs.get(e.birthDate)
  )

  val e = Employee.syntax("e")

  override val autoSession = AutoSession

  def find(id: Int)(implicit session: DBSession = autoSession): Option[Employee] = {
    withSQL {
      select.from(Employee as e).where.eq(e.id, id)
    }.map(Employee(e.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[Employee] = {
    withSQL(select.from(Employee as e)).map(Employee(e.resultName)).list.apply()
  }

  def findByNameAndJobTitle(nameOpt: Option[String], jobTitleOpt: Option[String])(implicit session: DBSession = autoSession) =
    if(nameOpt.isEmpty && jobTitleOpt.isEmpty) Left("No query parameters supplied")
    else Right(
      withSQL {
        select.from(Employee as e)
          .where(sqls.toAndConditionOpt(
          nameOpt.map(name => sqls.eq(e.firstName, name)),
          jobTitleOpt.map(jobTitle => sqls.eq(e.jobTitle, jobTitle))
        ))
      }.map(Employee(e.resultName)).list.apply()
    )

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls"count(1)").from(Employee as e)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[Employee] = {
    withSQL {
      select.from(Employee as e).where.append(sqls"${where}")
    }.map(Employee(e.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[Employee] = {
    withSQL {
      select.from(Employee as e).where.append(sqls"${where}")
    }.map(Employee(e.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls"count(1)").from(Employee as e).where.append(sqls"${where}")
    }.map(_.long(1)).single.apply().get
  }

  def create(
    firstName: String,
    prefix: Option[String] = None,
    lastName: String,
    jobTitle: String,
    birthDate: LocalDate)(implicit session: DBSession = autoSession): Employee = {
    val generatedKey = withSQL {
      insert.into(Employee).columns(
        column.firstName,
        column.prefix,
        column.lastName,
        column.jobTitle,
        column.birthDate
      ).values(
        firstName,
        prefix,
        lastName,
        jobTitle,
        birthDate
      )
    }.updateAndReturnGeneratedKey.apply()

    Employee(
      id = generatedKey.toInt, 
      firstName = firstName,
      prefix = prefix,
      lastName = lastName,
      jobTitle = jobTitle,
      birthDate = birthDate)
  }

  def save(entity: Employee)(implicit session: DBSession = autoSession): Employee = {
    withSQL {
      update(Employee).set(
        column.id -> entity.id,
        column.firstName -> entity.firstName,
        column.prefix -> entity.prefix,
        column.lastName -> entity.lastName,
        column.jobTitle -> entity.jobTitle,
        column.birthDate -> entity.birthDate
      ).where.eq(column.id, entity.id)
    }.update.apply()
    entity
  }

  def destroy(entity: Employee)(implicit session: DBSession = autoSession): Unit = {
    withSQL { delete.from(Employee).where.eq(column.id, entity.id) }.update.apply()
  }

}
