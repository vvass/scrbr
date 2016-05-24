package com.scrbr.utilities.couchbase

// first import the implicit execution context
import scala.concurrent.ExecutionContext.Implicits.global
import org.reactivecouchbase.ReactiveCouchbaseDriver
import play.api.libs.json.Json
import scala.collection.mutable.ArrayBuffer
import scala.concurrent.Future
/**
  * Created by vvass on 5/21/16.
  */

case class Campaign(campaign_id: String, primary_word: String, secondary_words: ArrayBuffer[String])


object Campaign {

  // get a driver instance driver
  val driver = ReactiveCouchbaseDriver()
  // get the default bucket
  val bucket = driver.bucket("default")

  // provide implicit Json formatters
  implicit val campaignFmt = Json.format[Campaign]

//  def findById(campaign_id: String): Future[Option[Campaign]] = {
    bucket.get[Campaign]("1").map { opt =>
      println("hello")
    }

  // shutdown the driver (only at app shutdown)
  driver.shutdown()
//  }
//
//  def findByPrimaryWord(primary_word: String): Future[Option[Campaign]] = {
//    bucket.get[Campaign](primary_word)
//  }

//  def shutDownDriver(){
//    driver.shutdown()
//  }

}
