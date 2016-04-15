package com.scrbr.actors

import akka.actor.Actor
import spray.routing.{Route, HttpService}

/**
  * Created by vvass on 4/15/16.
  */
class ScrubberSimpleActor extends Actor with MainRoute {




  def actorRefFactory = context
  def receive = runRoute(route)
}

trait MainRoute extends HttpService {
  val route = {
    get {
      complete("I exist!")
    }
  }

}