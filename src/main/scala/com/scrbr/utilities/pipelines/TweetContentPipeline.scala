package com.scrbr.utilities.pipelines

import java.net.URLEncoder

import akka.actor.ActorSystem
import com.scrbr.config.CouchClientConfiguration
import com.scrbr.core.domain.Tweet
import org.slf4j.LoggerFactory
import spray.client.pipelining.{addHeader, decode, encode, unmarshal}
import spray.http.{HttpRequest, HttpResponse}
import spray.httpx.encoding.{Deflate, Gzip}
import spray.client.pipelining._

import scala.concurrent.Future

/**
  *  This pipeline class makes it possible to reach out to couch client
  *  to get matches in couchbase database
  */
class TweetContentPipeline extends CouchClientConfiguration {
  
  val logger = LoggerFactory.getLogger(classOf[TweetContentPipeline])
  logger.debug("Initializing TweetContentPipeline class")
  
  lazy val logRequest: HttpRequest => HttpRequest = { r => logger.debug(r.toString); r }
  lazy val logResponse: HttpResponse => HttpResponse = { r => logger.debug(r.toString); r }
  
  val displayCompleteTimestamp = s"Completed in ${ System.currentTimeMillis() - startTimestamp } millis. - " + System.currentTimeMillis()
  val startTimestamp = System.currentTimeMillis() // TODO add this to config
  
  def getContentMatch(tweet: Tweet)(implicit system: ActorSystem): Future[String] = {
    import system.dispatcher // This needs to be here so that we have access to actor ref
    
    logger.debug("Entering getContent with Tweet")
     
    val pipeline : HttpRequest => Future[HttpResponse] = (
     addHeader("Accept", "application/json")
       ~> logRequest
       ~> encode(Gzip)
       ~> sendReceive
       ~> logResponse
       ~> decode(Deflate)
       ~> unmarshal[HttpResponse] // Unmarshalling of instances of type A into instances of type B (see akka unmarshall)
     )
    
    val id = tweet.id.toLong
    val sn = tweet.user.screenName
    val t = URLEncoder.encode(tweet.text.toString, "UTF-8").replaceAll("\\+","%20")
    
    // Main call to couchclient
    val response = pipeline (Get(s"http://$couchClientHost:$couchClientPort/$couchClientPath/$id/$sn/$t"))
    
    // Return repsonse Map
    response.map(_.entity.asString)
  
  }
  
  
  
}
