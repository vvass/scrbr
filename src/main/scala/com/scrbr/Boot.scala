package com.scrbr

import akka.actor.{ActorSystem, Props}
import com.scrbr.actors.{OAuthTwitterAuthorization, TweetContentActor, TweetStreamerActor, ScrubberSimpleActor}
import com.scrbr.config.HostConfiguration
import spray.servlet.WebBoot


/**
  * Created by vvass on 4/15/16.
  */
class Boot extends WebBoot with HostConfiguration {

  // create an actor system for application
  implicit val system = ActorSystem("simple-service")

  // create and start rest service actor
//  override val serviceActor = system.actorOf(Props[ScrubberSimpleActor])
  val contentActor = system.actorOf(Props(new TweetContentActor))
  val serviceActor = system.actorOf(Props(new TweetStreamerActor(TweetStreamerActor.twitterUri, contentActor) with OAuthTwitterAuthorization ))

  // start HTTP server with rest service actor as a handler
//  IO(Http) ! Http.Bind(service, serviceHost, servicePort)

    system.registerOnTermination {
      // put additional cleanup code here
      system.log.info("Application shut down")
    }

}
