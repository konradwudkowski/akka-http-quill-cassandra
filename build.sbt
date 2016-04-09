name := """akka-http-quill-cassandra"""

version := "1.0"

scalaVersion := "2.11.8"


libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "2.2.4" % "test",
  "io.getquill" %% "quill-cassandra" % "0.5.0",
  "com.typesafe.akka" %% "akka-http-experimental" % "2.4.3",
  "com.typesafe.akka" %% "akka-http-testkit-experimental" % "2.4.2-RC3",
  "io.circe" %% "circe-core" % "0.4.0-RC1",
  "io.circe" %% "circe-generic" % "0.4.0-RC1",
  "io.circe" %% "circe-parser" % "0.4.0-RC1"
)



// Uncomment to use Akka
//libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.3.11"

