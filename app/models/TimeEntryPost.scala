package models

import org.joda.time.{DateTimeZone, DateTime}
import org.joda.time.format.ISODateTimeFormat
import play.api.libs.json._

/**
 * Created by Thomas Meijers
 * Example REST API ScalaDays 2015
 * Case class for post data of an TimeEntry
 */
case class TimeEntryPost(
    startTime: DateTime,
    endTime: DateTime,
    employeeId: Int,
    projectId: Int,
    comment: Option[String]) extends PostData[TimeEntry]{

  def create: TimeEntry = TimeEntry.create(startTime, endTime, employeeId, projectId, comment)

  def update: Int => TimeEntry =
    id => TimeEntry(id, startTime, endTime, employeeId, projectId, comment).save()

  def checkTime: Boolean = startTime.isBefore(endTime)
}

object TimeEntryPost {

  private lazy val ISODateTimeFormatter = ISODateTimeFormat.dateTimeNoMillis
  private lazy val ISODateTimeParser = ISODateTimeFormat.dateTimeParser

  implicit val DateTimeFormatter = new Format[DateTime] {
    def reads(j: JsValue) = JsSuccess(ISODateTimeParser.parseDateTime(j.as[String]))
    def writes(o: DateTime): JsValue = JsString(ISODateTimeFormatter.print(o))
  }

  implicit val timeEntryDataReads = Json.reads[TimeEntryPost]

}
