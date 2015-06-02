package models

import org.joda.time.{DateTimeZone, DateTime}
import org.joda.time.format.ISODateTimeFormat
import play.api.libs.json._

/**
 * Created by Thomas Meijers
 * Example REST API ScalaDays 2015
 * Case class for post data of an TimeEntry
 */
case class TimeEntryData(
                          startTime: DateTime,
                          endTime: DateTime,
                          employeeId: Int,
                          projectId: Int,
                          comment: Option[String]) {

  def create: TimeEntry = TimeEntry.create(startTime, endTime, employeeId, projectId, comment)

  def update: Int => TimeEntry =
    id => TimeEntry(id, startTime, endTime, employeeId, projectId, comment).save()

  def checkTime: Boolean = startTime.isBefore(endTime)

}

object TimeEntryData {

  private lazy val ISODateTimeFormatter = ISODateTimeFormat.dateTimeNoMillis
  private lazy val ISODateTimeParser = ISODateTimeFormat.dateTimeParser

  implicit val DateTimeFormatter = new Format[DateTime] {
    def reads(j: JsValue) = JsSuccess(ISODateTimeParser.parseDateTime(j.as[String]))
    def writes(o: DateTime): JsValue = JsString(ISODateTimeFormatter.print(o))
  }

  implicit val timeEntryDataReads = Json.reads[TimeEntryData]
  implicit val timeEntryDataWrites = Json.writes[TimeEntryData]

  def fromTimeEntry(te: TimeEntry): TimeEntryData =
    TimeEntryData(te.startTime, te.endTime, te.employeeId, te.projectId, te.comment)

}
