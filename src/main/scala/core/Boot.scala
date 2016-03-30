package core

import actors.props.PropsServiceActor
import akka.actor.{Props, ActorSystem, ActorRef}
import spray.servlet.WebBoot
import akka.routing.FromConfig

/**
  * Created by vvass on 3/30/16.
  */
class Boot extends WebBoot {
  import Boot._

  def system = defaultActorSystem

  def serviceActor = system.actorOf(FromConfig.props(Props[PropsServiceActor]),"sprayActor")
}

object Boot {
  val defaultActorSystem = ActorSystem("default")
}