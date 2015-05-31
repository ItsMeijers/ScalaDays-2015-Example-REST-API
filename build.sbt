import play.PlayImport.PlayKeys._

name := """TimeEntries"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  specs2 % Test,
  "postgresql"      % "postgresql"                          % "9.1-901-1.jdbc4",
  "org.scalikejdbc" %% "scalikejdbc"                        % "2.2.6",
  "org.scalikejdbc" %% "scalikejdbc-config"                 % "2.2.6",
  "org.scalikejdbc" %% "scalikejdbc-play-initializer"       % "2.4.0.RC1",
  "org.scalikejdbc" %% "scalikejdbc-test"                   % "2.2.5" % "test",
  "org.scalikejdbc" %% "scalikejdbc-play-fixture"           % "2.4.0.RC1"
)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator

scalikejdbcSettings

routesImport += "utils.QueryBinders._"
