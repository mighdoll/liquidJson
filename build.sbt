import AssemblyKeys._ 

name := "liquidJson"

version := "0.1"

organization := "liquidj"

// set the Scala version used for the project
scalaVersion := "2.9.1"

// fork a new JVM for 'run' and 'test:run'
//fork := true

// only show warnings and errors on the screen for all tasks (the default is Info)
//  individual tasks can then be more verbose using the previous setting
//logLevel in compile := Level.Debug

resolvers ++= Seq(
  "Scala Tools Snapshots" at "http://scala-tools.org/repo-snapshots/"
)

libraryDependencies ++=  Seq(
  "cc.spray.json"      %% "spray-json"      %  "1.1.0-SNAPSHOT",             // json 
  "org.scalatest"      %% "scalatest"       % "1.6.1"             % "test"   // unit tests
)

seq(assemblySettings: _*)

scalacOptions ++= Seq("-deprecation", "-unchecked", "-Xexperimental", "-encoding", "utf8")

