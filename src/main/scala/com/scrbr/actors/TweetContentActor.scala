package com.scrbr.actors

import akka.actor.Actor
import akka.io.IO
import akka.actor.ActorSystem
import akka.pattern.ask
import com.sun.javafx.font.Metrics
import spray.httpx.encoding.{Deflate, Gzip}

import scala.concurrent.duration._
import scala.util.{Success, Failure}
import scala.concurrent.Future

import com.scrbr.core.domain.Tweet
import com.scrbr.utilities.corenlp.TweetAnnotator

import org.slf4j.LoggerFactory

import spray.can.Http
import spray.client.pipelining._
import spray.util._

import spray.http._
import java.net.URLEncoder;

/**
  * Created by vvass on 4/28/16.
  */


class TweetContentActor(sys: ActorSystem) extends Actor with TweetAnnotator {
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
        println(tweet.text.toString)
//       text.foreach(word => {
//         if (word.matches("^[a-zA-Z0-9]*$")) {
//           print(word + " ")
//         }
//       })
      }

    }

  }
}

class TweetContentCouchbaseActor(sys: ActorSystem) extends Actor with TweetAnnotator {
  import scala.concurrent.ExecutionContext.Implicits.global

  implicit  val system: ActorSystem = sys

  val logger = LoggerFactory.getLogger(classOf[TweetContentActor])

  def receive: Receive =
  {
    case tweet: Tweet => {

      val startTimestamp = System.currentTimeMillis()

      // This is how you can print individual words
//      val text: Array[String] = tweet.text.toLowerCase.split("\\s+")
//      if (tweet.user.lang.equals("en")) {

        // This is the new way that reaches out to the service for couchclient
        val pipeline : HttpRequest => Future[HttpResponse] = (
          addHeader("X-My-Special-Header", "fancy-value")
//          encode(Gzip)
          ~> sendReceive
//          ~> decode(Deflate)
//          ~> unmarshal[HttpResponse]
        )

//        if (tweet.text.toString.matches("^[a-zA-Z0-9]*$")){
            val responseFutures: Future[HttpResponse] = pipeline {
              val t = URLEncoder.encode(tweet.text.toString, "UTF-8")
  //            Get(s"http://192.168.99.100:9000/oldgGetDoc?primary=$t")
              Get(s"http://192.168.99.100:9000/getDoc?primary=$t")
            }

            responseFutures onComplete {
              case Success(response) => {
                println(response.entity.asString)
  //              shutdown()
                println(s"Request completed in ${System.currentTimeMillis() - startTimestamp} millis.")
              }

              case Failure(error) => {
                logger.info(s"You had a failure -- $error")
  //              shutdown()
              }

            }
//        }

        def shutdown(): Unit = {
          IO(Http).ask(Http.CloseAll)(1.second).await
          system.shutdown()
        }

//      }
    }

  }
}