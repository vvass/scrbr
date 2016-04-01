package utlities

import actors.PropsServiceActor
import akka.actor.{ActorSystem, Props}
import akka.routing.FromConfig
import spray.servlet.WebBoot

/**
  * Created by vvass on 3/31/16.
  */

/*
    Basic web boot servlet that implements WebBoot class
 */
class BootServlet extends WebBoot {
  import BootServlet._

  def system = defaultActorSystem

  def serviceActor = system.actorOf(FromConfig.props(Props[PropsServiceActor]),"sprayActor")


  system.registerOnTermination {
    // put additional cleanup code here
    system.log.info("Application shut down")
  }

}

object BootServlet {
  val defaultActorSystem = ActorSystem("default")
}