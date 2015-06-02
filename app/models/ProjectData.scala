package models

import play.api.libs.json.Json

/**
 * Created by Thomas Meijers
 * Example REST API ScalaDays 2015
 * Case class for post data of an Project
 */
case class ProjectData(projectName: String, projectDescription: String) {

  def create: Project = Project.create(projectName, projectDescription)

  def update: Int => Project =
    id => Project(id, projectName, projectDescription).save()

}

object ProjectData{
  implicit val projectDataReads = Json.reads[ProjectData]
  
  implicit val projectDataWrites = Json.writes[ProjectData]
  
  def fromProject(p: Project): ProjectData = ProjectData(p.projectName, p.projectDescription)
}
