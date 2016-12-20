package com.scrbr.actors

import akka.actor.{Actor, ActorSystem}
import akka.io.IO
import akka.pattern.ask
import com.scrbr.core.domain.CouchClientResponseProtocol.CouchClientResponseEntity

import com.scrbr.utilities.corenlp.TweetAnnotator
import com.scrbr.core.domain.Tweet
import org.json4s.{DefaultFormats, Formats}

import org.slf4j.LoggerFactory

import scala.concurrent.duration._
import scala.util.{Success, Failure}
import scala.concurrent.Future

import spray.can.Http
import spray.client.pipelining._
import spray.http._
import spray.httpx.encoding.{Deflate, Gzip}
import spray.util._
import spray.json._

import java.net.URLEncoder

/**
  * Created by vvass on 4/28/16.
  */


class TweetContentActor(sys: ActorSystem) extends Actor
  with TweetAnnotator {
  import scala.concurrent.ExecutionContext.Implicits.global

  implicit  val system: ActorSystem = sys

  val logger = LoggerFactory.getLogger(classOf[TweetContentActor])

  def receive: Receive =
  {
    case tweet: Tweet => {

      // This is just to print everything that comes back
//      println(tweet.text.toString)

      // This is how you can print individual words
      val text: Array[String] = tweet.text.toLowerCase.split("\\s+")
      if (tweet.user.lang.equals("en")) {

        // This is useful for only printing english language texts
        println(DateTime.now +" "+tweet.text.toString)
//       text.foreach(word => {
//         if (word.matches("^[a-zA-Z0-9]*$")) {
//           print(word + " ")
//         }
//       })
      }

    }

  }
}

class TweetContentCouchbaseActor(sys: ActorSystem) extends Actor
  with TweetAnnotator {

  import scala.concurrent.ExecutionContext.Implicits.global

  implicit val system: ActorSystem = sys

  val logger = LoggerFactory.getLogger(classOf[TweetContentActor])

  // Used to figure out if a sting is empty
  def isEmpty(x: String) = x != null && x.trim.nonEmpty

  def receive = {
    case tweet: Tweet => {

      val startTimestamp = System.currentTimeMillis()

      val displayCompleteTimestamp = s"Completed in ${System.currentTimeMillis() - startTimestamp} millis. - "+System.currentTimeMillis()

      val logRequest: HttpRequest => HttpRequest = { r => logger.debug(r.toString); r }
      val logResponse: HttpResponse => HttpResponse = { r => logger.debug(r.toString); r }


      // This is the new way that reaches out to the service couch client
      // TODO make into a common class
      val pipeline = (
        addHeader("X-My-Special-Header", "fancy-value")
        ~> addHeader("Accept", "application/json") // we want to get json so that we can marshall it to CouchClientResponseEntity
//        ~> logRequest
        ~> encode(Gzip)
        ~> sendReceive
//        ~> logResponse
        ~> decode(Deflate)
        ~> unmarshal[CouchClientResponseEntity] // Unmarshalling of instances of type A into instances of type B (see akka unmarshall)
      )

      //This is done so that we can make sure we only process US traffic
      if(tweet.lang == "en") { // TODO put this in a config

        // val annotatedText = tweetCoreProccessor.annotate(tweet.text)
        lazy val response = pipeline {
          val t = URLEncoder.encode(tweet.text.toString, "UTF-8")
          // Send request to couch client to get a match from couchbase SOS
          // TODO makes sure that this is places in a configuration file
          Get(s"http://192.168.99.100:9000/getDoc?primary=$t")
        }

        response.onComplete {
          case Success(response: CouchClientResponseEntity) => {

            // TODO add to config or have a way to process
            if(false) println(displayCompleteTimestamp)
            if(true) print("*")
            if(isEmpty(response.data)){
              println("\n\n" + response.data + "\n\n\n" + tweet.toString )
            }
          }

          case Failure(error) => {
            println(System.currentTimeMillis())
            logger.info(s"You had a failure -- $error")
          }
        }
      }

      def shutdown(): Unit = {
        IO(Http).ask(Http.CloseAll)(1.second).await
        system.shutdown()
      }

    }

  }
}