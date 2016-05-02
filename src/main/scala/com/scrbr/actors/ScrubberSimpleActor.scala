package com.scrbr.actors

import akka.actor.{ActorLogging, Actor}
import com.scrbr.utilities.routes.MainServiceRoute

/**
  * Created by vvass on 4/15/16.
  */
class ScrubberSimpleActor extends Actor with MainServiceRoute with ActorLogging {

  override def actorRefFactory = context
  def receive = runRoute(route)

}

