package actors.props

import akka.actor.Actor
import akka.actor.Actor.Receive
import spray.http.{Timedout, HttpRequest, HttpResponse, StatusCodes}
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

