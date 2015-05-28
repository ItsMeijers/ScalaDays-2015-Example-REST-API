package models

import play.api.libs.json.Json

/**
 * Created by Thomas Meijers
 * Example REST API ScalaDays 2015
 * Case class for post data of an Project
 */
case class ProjectPost(projectName: String, projectDescription: String) extends PostData[Project]{

  def create: Project = Project.create(projectName, projectDescription)

  def update: Int => Project =
    id => Project(id, projectName, projectDescription).save()

}

object ProjectPost{
  implicit val projectDataReads = Json.reads[ProjectPost]
}
