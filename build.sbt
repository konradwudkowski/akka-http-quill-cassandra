name := """akka-http-quill-cassandra"""

version := "1.0"

scalaVersion := "2.11.8"

// Change this to another test framework if you prefer
libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.4" % "test"
libraryDependencies += "io.getquill" %% "quill-cassandra" % "0.5.0"
libraryDependencies += "com.typesafe.akka" %% "akka-http-experimental" % "2.4.3"
libraryDependencies += "com.typesafe.akka" %% "akka-http-testkit-experimental" % "2.4.2-RC3"



// Uncomment to use Akka
//libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.3.11"

