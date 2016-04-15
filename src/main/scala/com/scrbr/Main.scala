package com.scrbr

import akka.actor.{Props, ActorSystem}
import akka.io.IO
import com.scrbr.actors.ScrubberSimpleActor
import spray.can.Http

/**
  * Created by vvass on 4/15/16.
  */
object Main extends App {
  implicit val system = ActorSystem("simple-service")
  val service = system.actorOf(Props[ScrubberSimpleActor], "simple-service")

  //If we're on cloud foundry, get's the host/port from the env vars
  lazy val host = Option(System.getenv("VCAP_APP_HOST")).getOrElse("localhost")
  lazy val port = Option(System.getenv("VCAP_APP_PORT")).getOrElse("8080").toInt

  IO(Http) ! Http.Bind(service, host, port = port)
}
