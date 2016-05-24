package com.scrbr.config

import com.typesafe.config.ConfigFactory

import scala.util.Try

/**
  * Created by vvass on 4/15/16.
  */
trait HostConfiguration {
  //TODO Implement this host config for components
  /**
    * Application config object.
    */
  val config = ConfigFactory.load()

  /** Host name/address to start service on. */
  lazy val serviceHost = Try(config.getString("service.host")).getOrElse("localhost")

  /** Port to start service on. */
  lazy val servicePort = Try(config.getInt("service.port")).getOrElse(8080)

}
