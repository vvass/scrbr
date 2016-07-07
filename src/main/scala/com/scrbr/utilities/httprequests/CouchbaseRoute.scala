package com.scrbr.utilities.httprequests

import com.scrbr.core.domain.Tweet
import spray.http.BasicHttpCredentials
import spray.httpx.encoding.{Deflate, Gzip}
import scala.concurrent.duration._
import akka.actor.ActorSystem
import akka.pattern.ask
import akka.io.IO
import spray.json.{JsonFormat, DefaultJsonProtocol}
import spray.can.Http
import spray.client.pipelining._
import spray.util._
/**
  * Created by vvass on 6/30/16.
  */

case class Result[T](status: String, results: List[T])

object CouchbaseHttpRequest {

  object MyJsonProtocol extends DefaultJsonProtocol {
    implicit def orderConfirmationFormat[T: JsonFormat] = jsonFormat2(Result.apply[T])
  }

  implicit val system = ActorSystem()
  import system.dispatcher // execution context for futures

  val pipeline = (
    addHeader("X-My-Special-Header", "fancy-value")
      ~> addCredentials(BasicHttpCredentials("bob", "secret"))
      ~> encode(Gzip)
      ~> sendReceive
      ~> decode(Deflate)
  )

  def shutdown(): Unit = {
    IO(Http).ask(Http.CloseAll)(1.second).await
    system.shutdown()
  }


}

