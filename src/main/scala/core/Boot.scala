package core

import akka.actor.{ActorSystem, ActorRef}
import spray.servlet.WebBoot

/**
  * Created by vvass on 3/30/16.
  */
class Boot extends WebBoot {
  override def system: ActorSystem = ???

  override def serviceActor: ActorRef = ???
}
