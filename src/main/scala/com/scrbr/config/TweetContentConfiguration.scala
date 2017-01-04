package com.scrbr.config

import scala.util.Try

/**
  * Created by vvass on 1/4/17.
  */
trait TweetContentConfiguration extends MainConfiguration {
  lazy val tweetLanguageFilter  = Try(config.getString("tweetcontent.language-filter")).getOrElse("en")
  lazy val testWord             = Try(config.getString("tweetcontent.trump-test")).getOrElse("trump")
  
  lazy val twitterFilterUri     = Try(config.getString("twitterAPIs.filter-uri")).getOrElse("http://localhost")
  lazy val twitterSampleUri     = Try(config.getString("twitterAPIs.sample-uri")).getOrElse("http://localhost")
  
  lazy val homeDirectory        = Try(config.getString("twitterAPIs.home-dir")).getOrElse("user.home")
  lazy val twitterDirectory     = Try(config.getString("twitterAPIs.twitter-dir")).getOrElse("/.twitter/activator")
  lazy val printEntity          = Try(config.getBoolean("twitterAPIs.print-entity")).getOrElse(false)
  lazy val badUserMessage       = Try(config.getString("twitterAPIs.bad-user")).getOrElse("Bad User")
  lazy val badPlaceMessage      = Try(config.getString("twitterAPIs.bad-place")).getOrElse("Bad Place")
  lazy val badPlaceJsonMessage  = Try(config.getString("twitterAPIs.bad-place-json")).getOrElse("Bad Place Json")
  lazy val badTweetMessage      = Try(config.getString("twitterAPIs.bad-tweet")).getOrElse("Bad Tweet")
  lazy val badTweetJsonMessage  = Try(config.getString("twitterAPIs.bad-tweet-json")).getOrElse("Bad Tweet")
}
