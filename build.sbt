name := "Cubic" 

version := "1.0"  

scalaVersion := "2.11.4" 

libraryDependencies += "org.scala-lang" % "scala-swing" % "2.11.0-M7"

libraryDependencies += "org.scalafx" %% "scalafx" % "8.0.20-R6"

libraryDependencies += "junit" % "junit" % "4.11" % "test"

//libraryDependencies ++= Seq(
//		"org.specs2" %% "specs2" % "2.4.9" % "test",
//		"org.specs2" %% "specs2-scalaz-core" % "6.0.1" % "test"
//)

fork := true

connectInput in run := true

fork in Test := true
