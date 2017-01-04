package com.scrbr.actors

import akka.actor.{Actor, ActorSystem}
import com.scrbr.config.TweetContentConfiguration
import com.scrbr.utilities.corenlp.TweetAnnotator
import com.scrbr.utilities.exceptions.{CouchClientFailureException, MalformedTweetException}
import com.scrbr.core.domain.Tweet
import org.slf4j.LoggerFactory

import scala.util.{Failure, Success}
import com.scrbr.utilities.pipelines.TweetContentPipeline

/**
  * Created by vvass on 4/28/16.
  */

class TweetContentCouchbaseActor(sys: ActorSystem) extends Actor
  with TweetContentConfiguration {
    import scala.concurrent.ExecutionContext.Implicits.global
  
    implicit val system: ActorSystem = sys
  
    val logger = LoggerFactory.getLogger(classOf[TweetContentCouchbaseActor])
    logger.debug("Initializing TweetContentCouchbaseActor class")
    
    val tweetContentPipeline = new TweetContentPipeline
    
    // Used to figure out if a sting is empty
    def isEmpty(x: String) = x != null && x.trim.nonEmpty
  
    def receive = {
    
      /**
        *  All tweet end up here. This is where we use play framework to send messages
        *  to couch-client api. The return will be a JSON with status and result. Result
        *  will have information if it was found in the couchbase server (System of Storage).
        *  Status will be 'OK' if something found or 'KO' if nothing found.
        */
      case tweet: Tweet => {
    
        logger.debug("Entering case for tweets in TweetContentCouchbaseActor")
        
        //This is done so that we can make sure we only process US traffic
        if(tweet.lang == tweetLanguageFilter) {
  
          // TODO DEV we need a way to take in hashtags (@) - ex @realDonaldTrump. Might need a different query
          
          // val annotatedText = tweetCoreProccessor.annotate(tweet.text)
    
          /**
            * Returns Future with content that matches the tweet text to contents
            * found in couchbase server
            */
          val responseMap = tweetContentPipeline.getContentMatch(tweet)
          responseMap onComplete {
            case Success(response) => {
    
              logger.debug("Success in getting content")
      
  //            if(true) println(tweetContentPipeline.displayCompleteTimestamp)
      
              if(tweet.text.contains("Trump")) {
                println(response.mkString("") + "\n" + tweet.toString)
        
              }
              else print("*")
              
            }
            case Failure(error) => {
              
              logger.debug(s"You had a failure in TweetContentPipeline while trying to talk to Couchbase Client  -- \n $error")
              throw new CouchClientFailureException
              
            }
          }
        }
      }
        
      /**
        * This will happen if the tweet is malformed or doesn't match the Tweet type. Also it
        * occure if there is a problem with Actor or streaming service.
        */
      case _ => throw new MalformedTweetException
    
    }
}