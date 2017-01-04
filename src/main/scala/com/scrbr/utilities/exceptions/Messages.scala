package com.scrbr.utilities.exceptions

case class CouchClientFailureException() extends Exception(Messages.CouchClientFailureException)
case class MalformedTweetException() extends Exception(Messages.MalformedTweetException)

object Messages {
  val CouchClientFailureException: String = "You had a failure in TweetContentPipeline while trying to talk to Couchbase Client"
  val MalformedTweetException: String = "Tweet is malformed or never sent. Take a look at TweetCouchClientWithPlayActor"
}
