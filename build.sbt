name := """akka-http-quill-cassandra"""

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= {
  val akkaV = "2.4.3"
  Seq(
    "io.getquill" %% "quill-cassandra" % "0.5.0",
    "io.circe" %% "circe-core" % "0.4.0-RC1",
    "io.circe" %% "circe-generic" % "0.4.0-RC1",
    "io.circe" %% "circe-parser" % "0.4.0-RC1",

    "com.typesafe.akka" %% "akka-http-experimental" % akkaV,
    "com.typesafe.akka" %% "akka-http-core" % akkaV,
    "com.typesafe.akka" %% "akka-http-spray-json-experimental" % akkaV,
    "com.typesafe.akka" %% "akka-actor" % akkaV,

    "com.typesafe.akka" %% "akka-testkit" % akkaV % "test",
    "com.typesafe.akka" %% "akka-http-testkit" % akkaV,
    "com.typesafe.akka" %% "akka-http-testkit-experimental" % "2.4.2-RC3",

    "org.scalatest" %% "scalatest" % "2.2.4" % "test"
  )
}
