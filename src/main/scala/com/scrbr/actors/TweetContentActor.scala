package com.scrbr.actors

import akka.actor.Actor
import akka.io.IO
import akka.actor.ActorSystem
import akka.pattern.ask
import com.scrbr.utilities.httprequests.CouchbaseHttpRequest.MyJsonProtocol._
import spray.http.BasicHttpCredentials
import spray.httpx.encoding.{Deflate, Gzip}
import spray.json.{JsonFormat, DefaultJsonProtocol}

import scala.concurrent.duration._
import scala.util.{Success, Failure}
import scala.concurrent.Future

import com.scrbr.core.domain.Tweet
import com.scrbr.utilities.corenlp.TweetAnnotator
import com.scrbr.utilities.httprequests.CouchbaseHttpRequest

import org.slf4j.LoggerFactory

import spray.can.Http
import spray.client.pipelining._
import spray.httpx.SprayJsonSupport
import spray.util._

import spray.http._
import HttpMethods._
import HttpHeaders._
import MediaTypes._


/**
  * Created by vvass on 4/28/16.
  */


class TweetContentActor extends Actor with TweetAnnotator {
  import scala.concurrent.ExecutionContext.Implicits.global

  val logger = LoggerFactory.getLogger(classOf[TweetContentActor])

  def receive: Receive = {
    case tweet: Tweet => {

      val pipeline : HttpRequest => Future[HttpResponse] = (
          encode(Gzip)
          ~> sendReceive
          ~> decode(Deflate)
          ~> unmarshal[HttpResponse]
        )

      val responseFutures: Future[HttpResponse] = pipeline {
        Get("http://192.168.99.100:9000/getDoc?primary=Trump")
      }

      responseFutures onComplete {
        case Success(response) =>
          logger.info(response.entity.asString)

        case Failure(error) =>
          logger.info(s"You had a failure -- $error")

      }
    }

  }
}
