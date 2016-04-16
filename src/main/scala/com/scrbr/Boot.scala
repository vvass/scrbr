package com.scrbr

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.io.IO
import com.scrbr.actors.ScrubberSimpleActor
import com.scrbr.config.Configuration
import spray.can.Http
import spray.servlet.WebBoot


/**
  * Created by vvass on 4/15/16.
  */
class Boot extends WebBoot with Configuration {

  // create an actor system for application
  implicit val system = ActorSystem("simple-service")

  // create and start rest service actor
  override val serviceActor = system.actorOf(Props[ScrubberSimpleActor])

  // start HTTP server with rest service actor as a handler
//  IO(Http) ! Http.Bind(service, serviceHost, servicePort)

  system.registerOnTermination {
    // put additional cleanup code here
    system.log.info("Application shut down")
  }

}
