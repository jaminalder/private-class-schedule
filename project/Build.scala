import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "private-class-schedule"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    jdbc,
    "com.typesafe" % "slick_2.10.0-RC1" % "0.11.2",
    "org.mongodb" %% "casbah" % "2.5.0"
  )


  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here      
  )

}
