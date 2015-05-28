package models

import org.scalatest._
import scalikejdbc.scalatest.AutoRollback
import scalikejdbc._


class ProjectSpec extends fixture.FlatSpec with Matchers with AutoRollback {
  val p = Project.syntax("p")

  behavior of "Project"

  it should "find by primary keys" in { implicit session =>
    val maybeFound = Project.find(123)
    maybeFound.isDefined should be(true)
  }
  it should "find by where clauses" in { implicit session =>
    val maybeFound = Project.findBy(sqls.eq(p.id, 123))
    maybeFound.isDefined should be(true)
  }
  it should "find all records" in { implicit session =>
    val allResults = Project.findAll()
    allResults.size should be >(0)
  }
  it should "count all records" in { implicit session =>
    val count = Project.countAll()
    count should be >(0L)
  }
  it should "find all by where clauses" in { implicit session =>
    val results = Project.findAllBy(sqls.eq(p.id, 123))
    results.size should be >(0)
  }
  it should "count by where clauses" in { implicit session =>
    val count = Project.countBy(sqls.eq(p.id, 123))
    count should be >(0L)
  }
  it should "create new record" in { implicit session =>
    val created = Project.create(projectName = "MyString", projectDescription = "MyString")
    created should not be(null)
  }
  it should "save a record" in { implicit session =>
    val entity = Project.findAll().head
    // TODO modify something
    val modified = entity
    val updated = Project.save(modified)
    updated should not equal(entity)
  }
  it should "destroy a record" in { implicit session =>
    val entity = Project.findAll().head
    Project.destroy(entity)
    val shouldBeNone = Project.find(123)
    shouldBeNone.isDefined should be(false)
  }

}
