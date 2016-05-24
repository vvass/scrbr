package com.scrbr.core.domain

import net.spy.memcached.ops.OperationStatus
import play.api.libs.json.Json

import scala.collection.mutable.ArrayBuffer
import scala.concurrent.Future

/**
  * Created by vvass on 5/21/16.
  */

case class Campaign(campaign_id: String, primary_word: String, secondary_words: ArrayBuffer[String]){
  implicit val campaignFmt = Json.format[Campaign]
}