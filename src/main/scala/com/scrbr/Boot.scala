package com.scrbr

import akka.actor.{ActorSystem, Props}
import com.scrbr.actors.{OAuthTwitterAuthorization, TweetContentActor, TweetStreamerActor}
import com.scrbr.config.HostConfiguration
import spray.servlet.WebBoot


/**
  * Created by vvass on 4/15/16.
  */
class Boot extends WebBoot with HostConfiguration {

  // create an actor system for application
  implicit val system = ActorSystem("simple-service")

  // create and start rest service actor
  val contentActor = system.actorOf(Props(new TweetContentActor))
  val serviceActor = system.actorOf(Props(new TweetStreamerActor(TweetStreamerActor.twitterUri_v2, contentActor) with OAuthTwitterAuthorization ))


  system.registerOnTermination {
    // put additional cleanup code here
    system.log.info("Application shut down")
  }

}
