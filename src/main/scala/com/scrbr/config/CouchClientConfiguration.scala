package com.scrbr.config

import scala.util.Try

trait CouchClientConfiguration extends MainConfiguration {
  lazy val couchClientHost = Try(config.getString("couchclient.host")).getOrElse("localhost")
  lazy val couchClientPort = Try(config.getString("couchclient.port")).getOrElse("8080")
  lazy val couchClientPath = Try(config.getString("couchclient.path")).getOrElse("getDoc")
}
