import sbt._
import sbt.Keys._

object CubicBuild extends Build {

  lazy val cubic = Project(
    id = "cubic",
    base = file("."),
    settings = Project.defaultSettings ++ Seq(
      name := "Cubic",
      organization := "de.htwg.modprog.cubic",
      version := "0.1-SNAPSHOT",
      scalaVersion := "2.11.4"
      // add other settings here
    )
  )
}
