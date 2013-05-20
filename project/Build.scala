import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "private-class-schedule"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    "org.mongodb" %% "casbah" % "2.5.0",
    "com.github.nscala-time" %% "nscala-time" % "0.4.0"
  )


  val main = play.Project(appName, appVersion, appDependencies).settings(
    sbt.Keys.fork in Test := false
  )

}
