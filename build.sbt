name := """edia-test"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file("."))
  .enablePlugins(PlayJava, PlayEbean)

scalaVersion := "2.10.3"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  evolutions,
  jdbc,
  javaWs,
  "org.elasticsearch" % "elasticsearch" % "2.1.0",
  "com.google.code.gson" % "gson" % "2.5"
)

libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.34"
// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator

ideaExcludeFolders += ".idea"

ideaExcludeFolders += ".idea_modules"
