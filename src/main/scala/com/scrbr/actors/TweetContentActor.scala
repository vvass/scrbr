package com.scrbr.actors

import akka.actor.Actor
import com.scrbr.core.domain.Tweet
import com.scrbr.utilities.corenlp.TweetAnnotator

/**
  * Created by vvass on 4/28/16.
  */
class TweetContentActor extends Actor with TweetAnnotator {


  def receive: Receive = {
    case tweet: Tweet => {
      val text: Array[String] = tweet.text.toLowerCase.split("\\s+")
      if (tweet.user.lang.equals("en")) {
        text.foreach(word => {
          if (word.matches("^[a-zA-Z0-9]*$")) {
            print(word + " ")
          }
        })
      }
    }

  }
}