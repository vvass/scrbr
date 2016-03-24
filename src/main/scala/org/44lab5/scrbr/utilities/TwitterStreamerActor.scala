import akka.actor.{Actor, ActorRef}
import akka.io.IO
import core.TweetMarshaller
import spray.can.Http
import spray.http._
import spray.client.pipelining._


object TweetStreamerActor {
  val twitterUri = Uri("https://stream.twitter.com/1.1/statuses/filter.json")
}

class TweetStreamerActor(uri: Uri, processor: ActorRef) extends Actor with TweetMarshaller {
  this: TwitterAuthorization =>
  val io = IO(Http)(context.system)

  def receive: Receive = {
    case query: String =>
      //Todo might need to expand body in order to get more results back
      val body = HttpEntity(ContentType(MediaTypes.`application/x-www-form-urlencoded`), s"track=$query")
      val request = HttpRequest(HttpMethods.POST, uri = uri, entity = body) ~> authorize
      sendTo(io).withResponsesReceivedBy(self)(request)
    case ChunkedResponseStart(_) =>
    case MessageChunk(entity, _) => TweetUnmarshaller(entity).fold(_ => (), processor !)
    case _ =>
  }
}