organization := "com.ocado"

name := "akka-stream-intro"

version := "1.0"

scalaVersion := "2.12.0"

val AkkaVersion = "2.4.13"


libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
  "com.typesafe.akka" %% "akka-stream-testkit" % AkkaVersion
)

resolvers += "Ocado Nexus" at "http://maven.ocado.com/nexus/content/groups/all-repos/"