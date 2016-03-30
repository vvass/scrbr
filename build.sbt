lazy val commonSettings = Seq(
  organization := "com.labs",
  version      := "1.0",
  scalaVersion := "2.11.8"
)


lazy val root = (project in file(".")).
  settings(commonSettings: _*).
  settings(
    name := "scrbr"
    // Cached resolution feature is akin to incremental compilation,
    // which only recompiles the sources that have been changed since the last compile.
//    updateOptions := updateOptions.value.withCachedResolution(true)
  )

libraryDependencies ++= {

  val sprayversion = "1.3.3"
  val akkaversion = "2.3.14"

  Seq(
    "io.spray"                      .%%("spray-can")                % sprayversion,
    "io.spray"                      .%%("spray-servlet")            % sprayversion,
    "io.spray"                      .%%("spray-routing")            % sprayversion,
    "io.spray"                      .%%("spray-json")               % "1.3.2",
    "io.spray"                      .%%("spray-client")             % sprayversion,
    "io.spray"                      .%%("spray-testkit")            % sprayversion   % "test",
    "com.typesafe.akka"             .%%("akka-actor")               % akkaversion,
    "com.typesafe.akka"             .%%("akka-testkit")             % akkaversion  % "test",
    "com.typesafe.scala-logging"    .%("scala-logging_2.11")        % "3.1.0",
    "org.specs2"                    .%%("specs2-core")              % "2.3.13"  % "test",
    "com.twitter"                   .%("util-core_2.10")            % "6.33.0",
    "com.typesafe.akka"             .%("akka-slf4j_2.11")           % "2.4.2"
  )
}

//packageOptions in assembly ~= { pos =>
//  pos.filterNot { po =>
//    po.isInstanceOf[Package.MainClass]
//  }
//}

//TODO figure out what other options we will need for assembling jar files, do we need dependencies - https://github.com/sbt/sbt-assembly
//TODO we need to figure if we need to have content hash at the end of file name - https://github.com/sbt/sbt-assembly
//TODO we will need to look at appending scripts (prepending shebang) to the end of assembly, might be useful for deployments - https://github.com/sbt/sbt-assembly
// This is intended to be used with a JAR that only contains your project
assemblyOption in assembly := (assemblyOption in assembly).value.copy(includeScala = true, includeDependency = true)

assemblyJarName in assembly := "scrbr-"+version+".jar"
//

logLevel := Level.Error