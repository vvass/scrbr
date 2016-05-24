package com.scrbr.actors

import org.slf4j.LoggerFactory
import com.scrbr.core.OAuth
import com.scrbr.core.domain.{Place, Tweet, User}
import spray.httpx.unmarshalling.{MalformedContent, Deserialized, Unmarshaller}
import spray.http._
import spray.json._
import spray.client.pipelining._
import akka.actor.{ActorRef, Actor}
import scala.io.Source
import scala.util.Try
import spray.can.Http
import akka.io.IO

/**
  * Created by vvass on 4/16/16.
  */

class TweetStreamerActor(uri: Uri, processor: ActorRef) extends Actor with TweetMarshaller{
  this: TwitterAuthorization =>
  val io = IO(Http)(context.system)

  val logger = LoggerFactory.getLogger(classOf[TweetStreamerActor])

  //Initial state of the Actor
  def receive = ready

  def ready: Receive = {
    case _ =>
      logger.debug("I got a message")
      val body = HttpEntity(ContentType(MediaTypes.`application/x-www-form-urlencoded`), s"stall_warnings=true")
      val request = HttpRequest(HttpMethods.POST, uri = uri, entity = body) ~> authorize
      sendTo(io).withResponsesReceivedBy(self)(request)
      //As soon as you get the data you should change state to "connected" by using a "become"
      context become connected
  }

  def connected: Receive = {
    case ChunkedResponseStart(_) => logger.info("Chunked Response started.")
    case MessageChunk(entity, _) => TweetUnmarshaller(entity).fold(_ => (), processor !)
    case ChunkedMessageEnd(_, _) => {
      logger.info("Chunked Message Ended")
      // shutdown the driver
      //      shutDownDriver

    }
    case Http.Closed => logger.info("HTTP closed")
    case Timedout(request: HttpRequest) => sender ! HttpResponse(200, "You have started the scrubber service! Congrats!")
    case _ =>
  }
}

trait TwitterAuthorization {
  def authorize: HttpRequest => HttpRequest
}

object TweetStreamerActor {
  val twitterUri = Uri("https://stream.twitter.com/1.1/statuses/filter.json")
  val twitterUri_v2 = Uri("https://stream.twitter.com/1.1/statuses/sample.json")
}

trait TweetMarshaller {

  implicit object TweetUnmarshaller extends Unmarshaller[Tweet] {

    def mkUser(user: JsObject): Deserialized[User] = {
      (user.fields("id_str"), user.fields("lang"), user.fields("followers_count")) match {
        case (JsString(id), JsString(lang), JsNumber(followers)) => Right(User(id, lang, followers.toInt))
        case (JsString(id), _, _)                                => Right(User(id, "", 0))
        case _                                                   => Left(MalformedContent("bad user"))
      }
    }

    def mkPlace(place: JsValue): Deserialized[Option[Place]] = place match {
      case JsObject(fields) =>
        (fields.get("country"), fields.get("name")) match {
          case (Some(JsString(country)), Some(JsString(name))) => Right(Some(Place(country, name)))
          case _                                               => Left(MalformedContent("bad place"))
        }
      case JsNull => Right(None)
      case _ => Left(MalformedContent("bad tweet"))
    }

    def apply(entity: HttpEntity): Deserialized[Tweet] = {
      Try {
        val json = JsonParser(entity.asString).asJsObject
//        println(json.fields.get("place").toString)
        (json.fields.get("id_str"), json.fields.get("text"), json.fields.get("place"), json.fields.get("user")) match {
          case (Some(JsString(id)), Some(JsString(text)), Some(place), Some(user: JsObject)) =>
            val x = mkUser(user).fold(x => Left(x), { user =>
              mkPlace(place).fold(x => Left(x), { place =>
                Right(Tweet(id, user, text, place))
              })
            })
            x
          case _ => Left(MalformedContent("bad tweet"))
        }
      }
    }.getOrElse(Left(MalformedContent("bad json")))
  }
}


trait OAuthTwitterAuthorization extends TwitterAuthorization {
  import OAuth._
  val home = System.getProperty("user.home")
  val lines = Source.fromFile(s"$home/.twitter/activator").getLines().toList

  val consumer = Consumer(lines(0), lines(1))
  val token = Token(lines(2), lines(3))

  val authorize: (HttpRequest) => HttpRequest = oAuthAuthorizer(consumer, token)
}