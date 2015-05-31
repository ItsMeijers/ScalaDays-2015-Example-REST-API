package models

import scalikejdbc.specs2.mutable.AutoRollback
import org.specs2.mutable._
import scalikejdbc._
import org.joda.time.{DateTime}


class TimeEntrySpec extends Specification {

  // initialize JDBC driver & connection pool
  Class.forName("org.postgresql.Driver")
  ConnectionPool.singleton("jdbc:postgresql://localhost/scaladays", "ThomasWorkBook", "")

  // ad-hoc session provider on the REPL
  implicit val session = AutoSession

  "TimeEntry" should {

    val te = TimeEntry.syntax("te")

    "find by primary keys" in new AutoRollback {
      val maybeFound = TimeEntry.find(123)(session)
      maybeFound.isDefined should beTrue
    }
    "find by where clauses" in new AutoRollback {
      val maybeFound = TimeEntry.findBy(sqls.eq(te.id, 123))
      maybeFound.isDefined should beTrue
    }
    "find all records" in new AutoRollback {
      val allResults = TimeEntry.findAll()(session)
      allResults.size should be_>(0)
    }
    "count all records" in new AutoRollback {
      val count = TimeEntry.countAll()(session)
      count should be_>(0L)
    }
    "find all by where clauses" in new AutoRollback {
      val results = TimeEntry.findAllBy(sqls.eq(te.id, 123))(session)
      results.size should be_>(0)
    }
    "count by where clauses" in new AutoRollback {
      val count = TimeEntry.countBy(sqls.eq(te.id, 123))(session)
      count should be_>(0L)
    }
    "create new record" in new AutoRollback {
      val created = TimeEntry.create(startTime = DateTime.now, endTime = DateTime.now, employeeId = 123, projectId = 123)(session)
      created should not beNull
    }
    "save a record" in new AutoRollback {
      val entity = TimeEntry.findAll()(session).head
      // TODO modify something
      val modified = entity.copy(projectId = 4)
      val updated = TimeEntry.save(modified)(session)
      updated should not equalTo(entity)
    }
    "destroy a record" in new AutoRollback {
      val entity = TimeEntry.findAll()(session).head
      TimeEntry.destroy(entity)(session)
      val shouldBeNone = TimeEntry.find(123)(session)
      shouldBeNone.isDefined should beFalse
    }
  }

}
