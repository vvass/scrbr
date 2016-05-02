import sbt.Keys._
import sbt.Level
import sbt.dsl._

lazy val commonSettings = Seq(
  organization := "com.44labs",
  version      := "1.0",
  scalaVersion := "2.11.8"
)


lazy val root = (project in file(".")).
  settings(commonSettings: _*).
  enablePlugins(TomcatPlugin).
  settings(
    name := "scrbr",
    logLevel := Level.Debug
  )

resolvers ++= Seq(
  "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
  "Sonatype snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/",
  "Spray Repository" at "http://repo.spray.io",
  "Spray Nightlies" at "http://nightlies.spray.io/"
)


libraryDependencies ++= {

  val akkaVersion = "2.3.11"
  val iospray = "1.3.3"

  Seq(
    // -- logging --
//    "com.typesafe.scala-logging".%("scala-logging_2.11")            % "3.1.0",
    "ch.qos.logback".%("logback-classic")                           % "1.1.7",
    // -- spray --
    "io.spray".%%("spray-can")                                      % iospray,
    "io.spray".%%("spray-http")                                     % iospray,
    "io.spray".%%("spray-util")                                     % iospray,
    "io.spray".%%("spray-routing")                                  % iospray,
    "io.spray".%%("spray-client")                                   % iospray,
    "io.spray".%%("spray-servlet")                                  % iospray,
    "io.spray".%%("spray-testkit")                                  % iospray       % "test",
    "io.spray".%%("spray-json")                                     % "1.3.2",
    // -- akka --
    "com.typesafe.akka".%%("akka-actor")                            % akkaVersion,
    "com.typesafe.akka".%%("akka-slf4j")                            % akkaVersion,
    "com.typesafe.akka".%%("akka-testkit")                          % akkaVersion     % "test",
    // -- twitter --
    "com.twitter".%("util-core_2.10")                               % "6.33.0",
    "javax.servlet".%("javax.servlet-api")                          % "3.0.1"       % "provided",
    // -- testing --
    "org.specs2".%%("specs2-core")                                  % akkaVersion   % "test",
    "org.scalatest" %%("scalatest")                                 % "2.2.6"       % "test"

  )
}
assemblyJarName in assembly := "scrbrdev.jar"

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


//Adds tomcat container and remote debugging
def debugTomcat = Command.command("debugTomcat") { state =>
  import com.earldouglas.xwp.ContainerPlugin.start
  val javaOpts =
    Seq(
      "-Xdebug",
      "-Xrunjdwp:server=y,transport=dt_socket,address=8080,suspend=n"
    )
  val state2 =
    Project.extract(state).append(
      Seq(javaOptions in Tomcat ++= javaOpts),
      state
    )
  Project.extract(state2).runTask(start in Tomcat, state2)
  state2
}

commands += debugTomcat

