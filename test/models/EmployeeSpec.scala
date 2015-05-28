package models

import org.scalatest._
import scalikejdbc.scalatest.AutoRollback
import scalikejdbc._
import org.joda.time.{LocalDate}


class EmployeeSpec extends fixture.FlatSpec with Matchers with AutoRollback {
  val e = Employee.syntax("e")

  behavior of "Employee"

  it should "find by primary keys" in { implicit session =>
    val maybeFound = Employee.find(123)
    maybeFound.isDefined should be(true)
  }
  it should "find by where clauses" in { implicit session =>
    val maybeFound = Employee.findBy(sqls.eq(e.id, 123))
    maybeFound.isDefined should be(true)
  }
  it should "find all records" in { implicit session =>
    val allResults = Employee.findAll()
    allResults.size should be >(0)
  }
  it should "count all records" in { implicit session =>
    val count = Employee.countAll()
    count should be >(0L)
  }
  it should "find all by where clauses" in { implicit session =>
    val results = Employee.findAllBy(sqls.eq(e.id, 123))
    results.size should be >(0)
  }
  it should "count by where clauses" in { implicit session =>
    val count = Employee.countBy(sqls.eq(e.id, 123))
    count should be >(0L)
  }
  it should "create new record" in { implicit session =>
    val created = Employee.create(firstName = "MyString", lastName = "MyString", jobTitle = "MyString", birthDate = LocalDate.now)
    created should not be(null)
  }
  it should "save a record" in { implicit session =>
    val entity = Employee.findAll().head
    // TODO modify something
    val modified = entity
    val updated = Employee.save(modified)
    updated should not equal(entity)
  }
  it should "destroy a record" in { implicit session =>
    val entity = Employee.findAll().head
    Employee.destroy(entity)
    val shouldBeNone = Employee.find(123)
    shouldBeNone.isDefined should be(false)
  }

}
