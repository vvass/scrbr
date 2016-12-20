package com.scrbr.core.domain

/**
  * Created by vvass on 12/16/16.
  */

import org.json4s.{Formats, DefaultFormats}
import spray.httpx.Json4sSupport

object CouchClientResponseProtocol extends Json4sSupport {

  override implicit def json4sFormats: Formats = DefaultFormats

  case class CouchClientResponseEntity (contentType: String,data: String)

}




