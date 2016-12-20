package com.scrbr.actors

import java.net.URLEncoder

import akka.actor.Actor
import com.scrbr.core.domain.Tweet
import spray.client.pipelining._
import spray.http.{HttpRequest, HttpResponse}
import spray.httpx.encoding.{Deflate, Gzip}

import twitter4j.{Twitter, TwitterFactory, StatusUpdate, Status}
import twitter4j.conf.ConfigurationBuilder


/**
  * Created by vvass on 12/13/16.
  */

sealed trait TwitterInstance extends OAuthTwitterAuthorization {

  val twitter = new TwitterFactory()
    .getInstance

  /* We need this in order to make twitter4j work programatically.
   * We could also create a twitter file in user.home but this way we
   * can reuse the OAuthTwitterAuthorization trait we built. */
  // TODO bring this out somehow into it own file
  val configs = new ConfigurationBuilder()
    .setOAuthConsumerKey(lines(0))
    .setOAuthConsumerSecret(lines(1))
    .setOAuthAccessToken(lines(2))
    .setOAuthAccessTokenSecret(lines(3))
    .build

  val statusUpdate = new StatusUpdate("")
}

class TweetResponseActor(tweet: Tweet) extends Actor
  with TwitterInstance {



  def receive = {
    case response => {
      println("DID IT")



    }
    case _ => print("Nope")
  }

}

class TweetResponse(response: HttpResponse, tweet: Tweet) extends TwitterInstance {

  lazy val couchClientResponse = response.entity.data
  lazy val userTwittedText = tweet.text.toString

  // Take tweet and get tweet id as string
  def getTwitterId(tweet: Tweet): String = {
    return tweet.id
  }

  // Send status update to twitter api
  def postStatusUpdate() = {

  }

  // print done and id and what was printed

}
