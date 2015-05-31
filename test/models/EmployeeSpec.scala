package models

import scalikejdbc.specs2.mutable.AutoRollback
import org.specs2.mutable._
import scalikejdbc._
import org.joda.time.{LocalDate}


class EmployeeSpec extends Specification {

  // initialize JDBC driver & connection pool
  Class.forName("org.postgresql.Driver")
  ConnectionPool.singleton("jdbc:postgresql://localhost/scaladays", "ThomasWorkBook", "")

  // ad-hoc session provider on the REPL
  implicit val session = AutoSession

  "Employee" should {

    val e = Employee.syntax("e")
    val te = TimeEntry.syntax("te")

    "find by primary keys" in new AutoRollback {
      val maybeFound = Employee.find(123)(session)
      maybeFound.isDefined should beTrue
    }
    "find by where clauses" in new AutoRollback {
      val maybeFound = Employee.findBy(sqls.eq(e.id, 123))(session)
      maybeFound.isDefined should beTrue
    }
    "find all records" in new AutoRollback {
      val allResults = Employee.findAll()(session)
      allResults.size should be_>(0)
    }
    "count all records" in new AutoRollback {
      val count = Employee.countAll()(session)
      count should be_>(0L)
    }
    "find all by where clauses" in new AutoRollback {
      val results = Employee.findAllBy(sqls.eq(e.id, 123))(session)
      results.size should be_>(0)
    }
    "count by where clauses" in new AutoRollback {
      val count = Employee.countBy(sqls.eq(e.id, 123))(session)
      count should be_>(0L)
    }
    "create new record" in new AutoRollback {
      val created = Employee.create(firstName = "MyString", lastName = "MyString", jobTitle = "MyString", birthDate = LocalDate.now)(session)
      created should not beNull
    }
    "save a record" in new AutoRollback {
      val entity = Employee.findAll()(session).head
      // TODO modify something
      val modified = entity.copy(firstName = "Test")
      val updated = Employee.save(modified)(session)
      updated should not equalTo(entity)
    }
    "destroy a record" in new AutoRollback {
      val entity = Employee.findAll()(session).head
      TimeEntry.findAllBy(sqls.eq(te.employeeId, 123))(session).foreach(_.destroy()(session))
      Employee.destroy(entity)(session)
      val shouldBeNone = Employee.find(123)(session)
      shouldBeNone.isDefined should beFalse
    }
  }

}
