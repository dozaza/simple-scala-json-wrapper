import sbt._
import Keys._

name := "simple-scala-json-warpper"

scalaVersion in ThisBuild := "2.10.4"

version in ThisBuild := "SNAPSHOT"

javacOptions in ThisBuild ++= Seq("-encoding", "UTF8")

scalacOptions in ThisBuild ++= Seq("-unchecked", "-deprecation", "-feature",
  "-language:reflectiveCalls", "-language:implicitConversions", "-language:postfixOps", "-language:higherKinds")

transitiveClassifiers in Global := Seq(Artifact.SourceClassifier)

libraryDependencies ++= {
  Seq(
    "ch.qos.logback" % "logback-classic" % "1.1.2",
    "com.google.code.gson" % "gson" % "2.2.4",
    "junit" % "junit" % "4.11",
    "org.apache.commons" % "commons-lang3" % "3.1",
    "org.slf4j" % "slf4j-api" % "1.7.2",
    "org.apache.poi" % "poi" % "3.11",
    "org.apache.poi" % "poi-ooxml" % "3.11",
    "com.typesafe.akka" %% "akka-actor" % "2.2.0",
    "com.typesafe.akka" %% "akka-slf4j" % "2.2.0",
    "com.typesafe.akka" %% "akka-remote" % "2.2.0",
    "joda-time" % "joda-time" % "2.2",
    "org.joda" % "joda-convert" % "1.5",
    "com.github.tototoshi" %% "scala-csv" % "1.2.2"
  )
}

sources in (Compile, doc) := Seq.empty
