name := "Cubic" 

version := "1.0"  

scalaVersion := "2.11.4" 

libraryDependencies ++= Seq(
	"org.scala-lang" % "scala-swing" % "2.11.0-M7",
	"org.scalafx" %% "scalafx" % "8.0.20-R6",
	"junit" % "junit" % "4.11" % "test",
	"org.specs2" %% "specs2-core" % "2.4.15" % "test"
)

scalacOptions in Test ++= Seq("-Yrangepos")

resolvers ++= Seq("snapshots", "releases").map(Resolver.sonatypeRepo)

fork := true

connectInput in run := true

fork in Test := true
