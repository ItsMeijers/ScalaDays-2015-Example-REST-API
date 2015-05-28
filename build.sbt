import play.PlayImport.PlayKeys._

name := """HourRegistry"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.4"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  "postgresql"      % "postgresql"                         % "9.1-901-1.jdbc4",
  "org.scalikejdbc" %% "scalikejdbc"                       % "2.2.5",
  "org.scalikejdbc" %% "scalikejdbc-config"                % "2.2.5",
  "org.scalikejdbc" %% "scalikejdbc-play-plugin"           % "2.3.6",
  "org.scalikejdbc" %% "scalikejdbc-test"                  % "2.2.5" % "test",
  "org.scalikejdbc" %% "scalikejdbc-play-fixture-plugin"   % "2.3.6"
)

scalikejdbcSettings

fork in run := false

routesImport += "utils.QueryBinders._"