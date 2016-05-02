package com.scrbr.actors

import akka.actor.Actor
import com.scrbr.core.domain.Tweet

/**
  * Created by vvass on 4/28/16.
  */
class TweetContentActor extends Actor {

  def receive: Receive = {
    case tweet: Tweet =>
      val text = tweet.text.toLowerCase
      println(text)
  }
}