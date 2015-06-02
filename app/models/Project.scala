package models

import play.api.libs.json._
import play.api.libs.functional.syntax._
import scalikejdbc._
import scalikejdbc.interpolation.SQLSyntax._

case class Project(
  id: Int,
  projectName: String,
  projectDescription: String) {

  def save()(implicit session: DBSession = Project.autoSession): Project = Project.save(this)(session)

  def destroy()(implicit session: DBSession = Project.autoSession): Unit = Project.destroy(this)(session)

}


object Project extends SQLSyntaxSupport[Project] {

  override val tableName = "project"

  override val columns = Seq("id", "project_name", "project_description")

  def apply(p: SyntaxProvider[Project])(rs: WrappedResultSet): Project = apply(p.resultName)(rs)
  def apply(p: ResultName[Project])(rs: WrappedResultSet): Project = new Project(
    id = rs.get(p.id),
    projectName = rs.get(p.projectName),
    projectDescription = rs.get(p.projectDescription)
  )

  val p = Project.syntax("p")

  val te = TimeEntry.syntax

  override val autoSession = AutoSession

  def find(id: Int)(implicit session: DBSession = autoSession): Option[Project] = {
    withSQL {
      select.from(Project as p).where.eq(p.id, id)
    }.map(Project(p.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[Project] = {
    withSQL(select.from(Project as p)).map(Project(p.resultName)).list.apply()
  }

  def findByEmployee(employeeId: Int)(implicit session: DBSession = autoSession) = {
    withSQL{
      select(distinct(p.resultAll)).from(Project as p)
        .innerJoin(TimeEntry as te)
        .on(p.id, te.projectId)
        .where.eq(te.employeeId, employeeId)
    }.map(Project(p.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls"count(1)").from(Project as p)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[Project] = {
    withSQL {
      select.from(Project as p).where.append(sqls"${where}")
    }.map(Project(p.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[Project] = {
    withSQL {
      select.from(Project as p).where.append(sqls"${where}")
    }.map(Project(p.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls"count(1)").from(Project as p).where.append(sqls"${where}")
    }.map(_.long(1)).single.apply().get
  }

  def create(
    projectName: String,
    projectDescription: String)(implicit session: DBSession = autoSession): Project = {
    val generatedKey = withSQL {
      insert.into(Project).columns(
        column.projectName,
        column.projectDescription
      ).values(
        projectName,
        projectDescription
      )
    }.updateAndReturnGeneratedKey.apply()

    Project(
      id = generatedKey.toInt, 
      projectName = projectName,
      projectDescription = projectDescription)
  }

  def save(entity: Project)(implicit session: DBSession = autoSession): Project = {
    withSQL {
      update(Project).set(
        column.id -> entity.id,
        column.projectName -> entity.projectName,
        column.projectDescription -> entity.projectDescription
      ).where.eq(column.id, entity.id)
    }.update.apply()
    entity
  }

  def destroy(entity: Project)(implicit session: DBSession = autoSession): Unit = {
    withSQL { delete.from(Project).where.eq(column.id, entity.id) }.update.apply()
  }

}
