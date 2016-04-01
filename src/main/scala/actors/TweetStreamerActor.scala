package actors

import akka.actor.{Actor, ActorRef}
import akka.io.IO
import core.auth.TwitterAuthorizationTrait
import spray.can.Http
import spray.client.pipelining._
import spray.http.{HttpRequest, _}
import utlities.twitter.TweetMarshallerUtility

/**
  * Created by vvass on 3/29/16.
  */
object TweetStreamerActor {
  val twitterUri = Uri("https://stream.twitter.com/1.1/statuses/filter.json")
}

class TweetStreamerActor(uri: Uri, processor: ActorRef) extends Actor with TweetMarshallerUtility {
  this: TwitterAuthorizationTrait =>
  val io = IO(Http)(context.system)

  def receive: Receive = {
    case query: String =>
      val body = HttpEntity(ContentType(MediaTypes.`application/x-www-form-urlencoded`), s"track=$query")
      val request = HttpRequest(HttpMethods.POST, uri = uri, entity = body) ~> authorize
      sendTo(io).withResponsesReceivedBy(self)(request)
    case ChunkedResponseStart(_) =>
    case MessageChunk(entity, _) => TweetUnmarshaller(entity).fold(_ => (), processor !)
    case _ =>
  }
}