package actors

import akka.actor.Actor
import spray.http.{HttpRequest, HttpResponse, StatusCodes, Timedout}
import utlities.twitter.routes.StreamingRoute

/**
  * Created by vvass on 3/30/16.
  */
trait PropsServiceActor extends Actor with StreamingRoute{
  val receive = handleTimeouts orElse runRoute( streamingRoute )

  def handleTimeouts: Receive = {
    case Timedout(x: HttpRequest) =>
      sender ! HttpResponse(StatusCodes.InternalServerError, "Request timed out.")
  }
}

