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
  "io.spray".%%("spray-can")                                      % "1.3.3",
  "io.spray".%%("spray-routing")                                  % "1.3.3",
  "io.spray".%%("spray-json")                                     % "1.3.2",
  "io.spray".%%("spray-testkit")                                  % "1.3.3"   % "test",
  "com.typesafe.akka".%%("akka-actor")                            % "2.3.9",
  "com.typesafe.akka".%%("akka-testkit")                          % "2.3.9"   % "test",
  "com.typesafe.scala-logging".%("scala-logging_2.11")            % "3.1.0",
  "org.specs2".%%("specs2-core")                                  % "2.3.11"  % "test",
  "com.twitter".%("util-core_2.10")                               % "6.33.0",
  "com.typesafe.akka".%("akka-slf4j_2.11")                        % "2.4.2"

)

assemblyJarName in assembly := "something.jar"

packageOptions in assembly ~= { pos =>
  pos.filterNot { po =>
    po.isInstanceOf[Package.MainClass]
  }
}

//TODO figure out what other options we will need for assembling jar files, do we need dependencies - https://github.com/sbt/sbt-assembly
//TODO we need to figure if we need to have content hash at the end of file name - https://github.com/sbt/sbt-assembly
//TODO we will need to look at appending scripts (prepending shebang) to the end of assembly, might be useful for deployments - https://github.com/sbt/sbt-assembly
// This is intended to be used with a JAR that only contains your project
assemblyOption in assembly := (assemblyOption in assembly).value.copy(includeScala = false, includeDependency = false)


logLevel := Level.Error