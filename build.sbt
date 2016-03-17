lazy val commonSettings = Seq(
  organization := "com.44labs",
  version      := "1.0",
  scalaVersion := "2.11.8"
)


lazy val root = (project in file(".")).
  settings(commonSettings: _*).
  settings(
    name := "scrbr"
  )

libraryDependencies ++= Seq(
  "io.spray".%%("spray-can")                  % "1.3.3",
  "io.spray".%%("spray-routing")              % "1.3.3",
  "io.spray".%%("spray-json")                 % "1.3.2",
  "io.spray".%%("spray-testkit")              % "1.3.3"   % "test",
  "com.typesafe.akka".%%("akka-actor")        % "2.3.9",
  "com.typesafe.akka".%%("akka-testkit")      % "2.3.9"   % "test",
  "org.specs2".%%("specs2-core")              % "2.3.11"  % "test",
  "com.twitter".%("util-core_2.10")           % "6.33.0"

)

logLevel := Level.Error