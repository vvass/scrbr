package com.scrbr

import akka.actor.{ActorSystem, Props}
import com.codahale.metrics.Counter
import com.scrbr.actors.{TweetContentCouchbaseActor, OAuthTwitterAuthorization, TweetStreamerActor}
import com.scrbr.config.MainConfiguration
import spray.servlet.WebBoot


/**
  * Created by vvass on 4/15/16.
  */


trait Instrumented {
  val metrics = BootLoader.metricRegistry
}

class Boot extends WebBoot with Instrumented {
  println("The starting time is " + System.currentTimeMillis())

  val bootCounter: Counter = metrics.counter("Starting boot counter.")
  bootCounter.inc() // Increments up

  // create an actor system for application
  implicit val system = ActorSystem("simple-service")

  // This actor works with couchbase in order to server up results
  lazy val contentActor = system.actorOf(Props(new TweetContentCouchbaseActor(system)))

  // This actor works with Twitter in order to start a streaming catcher
  lazy val serviceActor = system.actorOf(Props(new TweetStreamerActor(TweetStreamerActor.twitterUri_v2, contentActor) with OAuthTwitterAuthorization ))

  system.registerOnTermination {
    // put additional cleanup code here
    system.log.info("Application shut down")
  }

}

object BootLoader {
  // The application wide metrics registry.
  val metricRegistry = new com.codahale.metrics.MetricRegistry()
}

