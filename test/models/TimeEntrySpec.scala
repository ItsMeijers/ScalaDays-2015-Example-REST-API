package models

import org.scalatest._
import scalikejdbc.scalatest.AutoRollback
import scalikejdbc._
import org.joda.time.{DateTime}


class TimeEntrySpec extends fixture.FlatSpec with Matchers with AutoRollback {
  val te = TimeEntry.syntax("te")

  behavior of "TimeEntry"

  it should "find by primary keys" in { implicit session =>
    val maybeFound = TimeEntry.find(123)
    maybeFound.isDefined should be(true)
  }
  it should "find by where clauses" in { implicit session =>
    val maybeFound = TimeEntry.findBy(sqls.eq(te.id, 123))
    maybeFound.isDefined should be(true)
  }
  it should "find all records" in { implicit session =>
    val allResults = TimeEntry.findAll()
    allResults.size should be >(0)
  }
  it should "count all records" in { implicit session =>
    val count = TimeEntry.countAll()
    count should be >(0L)
  }
  it should "find all by where clauses" in { implicit session =>
    val results = TimeEntry.findAllBy(sqls.eq(te.id, 123))
    results.size should be >(0)
  }
  it should "count by where clauses" in { implicit session =>
    val count = TimeEntry.countBy(sqls.eq(te.id, 123))
    count should be >(0L)
  }
  it should "create new record" in { implicit session =>
    val created = TimeEntry.create(startTime = DateTime.now, endTime = DateTime.now, employeeId = 123, projectId = 123)
    created should not be(null)
  }
  it should "save a record" in { implicit session =>
    val entity = TimeEntry.findAll().head
    // TODO modify something
    val modified = entity
    val updated = TimeEntry.save(modified)
    updated should not equal(entity)
  }
  it should "destroy a record" in { implicit session =>
    val entity = TimeEntry.findAll().head
    TimeEntry.destroy(entity)
    val shouldBeNone = TimeEntry.find(123)
    shouldBeNone.isDefined should be(false)
  }

}
