package models

import scalikejdbc.specs2.mutable.AutoRollback
import org.specs2.mutable._
import scalikejdbc._


class ProjectSpec extends Specification {

  // initialize JDBC driver & connection pool
  Class.forName("org.postgresql.Driver")
  ConnectionPool.singleton("jdbc:postgresql://localhost/scaladays", "ThomasWorkBook", "")

  // ad-hoc session provider on the REPL
  implicit val session = AutoSession

  "Project" should {

    val p = Project.syntax("p")
    val te = TimeEntry.syntax("te")

    "find by primary keys" in new AutoRollback {
      val maybeFound = Project.find(123)(session)
      maybeFound.isDefined should beTrue
    }
    "find by where clauses" in new AutoRollback {
      val maybeFound = Project.findBy(sqls.eq(p.id, 123))(session)
      maybeFound.isDefined should beTrue
    }
    "find all records" in new AutoRollback {
      val allResults = Project.findAll()(session)
      allResults.size should be_>(0)
    }
    "count all records" in new AutoRollback {
      val count = Project.countAll()(session)
      count should be_>(0L)
    }
    "find all by where clauses" in new AutoRollback {
      val results = Project.findAllBy(sqls.eq(p.id, 123))(session)
      results.size should be_>(0)
    }
    "count by where clauses" in new AutoRollback {
      val count = Project.countBy(sqls.eq(p.id, 123))(session)
      count should be_>(0L)
    }
    "create new record" in new AutoRollback {
      val created = Project.create(projectName = "MyString", projectDescription = "MyString")(session)
      created should not beNull
    }
    "save a record" in new AutoRollback {
      val entity = Project.findAll()(session).head
      // TODO modify something
      val modified = entity.copy(projectName = "test")
      val updated = Project.save(modified)(session)
      updated should not equalTo(entity)
    }
    "destroy a record" in new AutoRollback {
      val entity = Project.findAll()(session).head
      TimeEntry.findAllBy(sqls.eq(te.projectId, 123))(session).foreach(_.destroy()(session))
      Project.destroy(entity)(session)
      val shouldBeNone = Project.find(123)(session)
      shouldBeNone.isDefined should beFalse
    }
  }

}
