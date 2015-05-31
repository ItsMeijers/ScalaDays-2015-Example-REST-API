package models

import org.joda.time.format.ISODateTimeFormat
import play.api.libs.json._
import play.api.libs.functional.syntax._
import scalikejdbc._
import org.joda.time.{DateTime}
import scalikejdbc.interpolation.SQLSyntax._

case class TimeEntry(
  id: Int,
  startTime: DateTime,
  endTime: DateTime,
  employeeId: Int,
  projectId: Int,
  comment: Option[String] = None) {

  def save()(implicit session: DBSession = TimeEntry.autoSession): TimeEntry = TimeEntry.save(this)(session)

  def destroy()(implicit session: DBSession = TimeEntry.autoSession): Unit = TimeEntry.destroy(this)(session)

}


object TimeEntry extends SQLSyntaxSupport[TimeEntry] {

  private val ISODateTimeFormatter = ISODateTimeFormat.dateTime
  private val ISODateTimeParser = ISODateTimeFormat.dateTimeParser

  implicit val DateTimeFormatter = new Format[DateTime] {
    def reads(j: JsValue) = JsSuccess(ISODateTimeParser.parseDateTime(j.as[String]))
    def writes(o: DateTime): JsValue = JsString(ISODateTimeFormatter.print(o))
  }

  implicit val timeEntryWrites = (
      (JsPath \ "id").write[Int] and
      (JsPath \ "startTime").write[DateTime] and
      (JsPath \ "endTime").write[DateTime] and
      (JsPath \ "employeeId").write[Int] and
      (JsPath \ "projectId").write[Int] and
      (JsPath \ "comment").writeNullable[String]
    )(unlift(TimeEntry.unapply))

  override val tableName = "timeentry"

  override val columns = Seq("id", "start_time", "end_time", "employee_id", "project_id", "comment")

  def apply(te: SyntaxProvider[TimeEntry])(rs: WrappedResultSet): TimeEntry = apply(te.resultName)(rs)
  def apply(te: ResultName[TimeEntry])(rs: WrappedResultSet): TimeEntry = new TimeEntry(
    id = rs.get(te.id),
    startTime = rs.get(te.startTime),
    endTime = rs.get(te.endTime),
    employeeId = rs.get(te.employeeId),
    projectId = rs.get(te.projectId),
    comment = rs.get(te.comment)
  )

  val te = TimeEntry.syntax("te")
  val e = Employee.syntax

  override val autoSession = AutoSession

  def find(id: Int)(implicit session: DBSession = autoSession): Option[TimeEntry] = {
    withSQL {
      select.from(TimeEntry as te).where.eq(te.id, id)
    }.map(TimeEntry(te.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[TimeEntry] = {
    withSQL(select.from(TimeEntry as te)).map(TimeEntry(te.resultName)).list.apply()
  }

  def findFromAndTo(fromOpt: Option[DateTime], toOpt: Option[DateTime])(implicit session: DBSession = autoSession) = {
    if(fromOpt.isEmpty && toOpt.isEmpty) Left("No query parameters supplied")
    else Right(
      withSQL {
        select.from(TimeEntry as te)
          .where(sqls.toAndConditionOpt(
          fromOpt.map(from => sqls.gt(te.startTime, from)),
          toOpt.map(to => sqls.lt(te.endTime, to))
        )).orderBy(te.startTime desc)
      }.map(TimeEntry(te.resultName)).list.apply()
    )
  }

  def findByEmployee(id: Int)(implicit session: DBSession = autoSession) = {
    withSQL{
      select(distinct(te.resultAll)).from(TimeEntry as te)
        .innerJoin(Employee as e)
        .on(e.id, te.employeeId)
        .where.eq(e.id, id)
        .orderBy(te.startTime desc)
    }.map(TimeEntry(te.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls"count(1)").from(TimeEntry as te)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[TimeEntry] = {
    withSQL {
      select.from(TimeEntry as te).where.append(sqls"${where}")
    }.map(TimeEntry(te.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[TimeEntry] = {
    withSQL {
      select.from(TimeEntry as te).where.append(sqls"${where}")
    }.map(TimeEntry(te.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls"count(1)").from(TimeEntry as te).where.append(sqls"${where}")
    }.map(_.long(1)).single.apply().get
  }

  def create(
    startTime: DateTime,
    endTime: DateTime,
    employeeId: Int,
    projectId: Int,
    comment: Option[String] = None)(implicit session: DBSession = autoSession): TimeEntry = {
    val generatedKey = withSQL {
      insert.into(TimeEntry).columns(
        column.startTime,
        column.endTime,
        column.employeeId,
        column.projectId,
        column.comment
      ).values(
        startTime,
        endTime,
        employeeId,
        projectId,
        comment
      )
    }.updateAndReturnGeneratedKey.apply()

    TimeEntry(
      id = generatedKey.toInt, 
      startTime = startTime,
      endTime = endTime,
      employeeId = employeeId,
      projectId = projectId,
      comment = comment)
  }

  def save(entity: TimeEntry)(implicit session: DBSession = autoSession): TimeEntry = {
    withSQL {
      update(TimeEntry).set(
        column.id -> entity.id,
        column.startTime -> entity.startTime,
        column.endTime -> entity.endTime,
        column.employeeId -> entity.employeeId,
        column.projectId -> entity.projectId,
        column.comment -> entity.comment
      ).where.eq(column.id, entity.id)
    }.update.apply()
    entity
  }

  def destroy(entity: TimeEntry)(implicit session: DBSession = autoSession): Unit = {
    withSQL { delete.from(TimeEntry).where.eq(column.id, entity.id) }.update.apply()
  }

}
