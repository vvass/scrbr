package core.auth

import spray.http.HttpRequest

import scala.io.Source

/**
  * Created by vvass on 3/29/16.
  */
trait OAuthTwitterTrait extends TwitterAuthorizationTrait {
  import OAuth._
  val home = System.getProperty("user.home")
  val lines = Source.fromFile(s"$home/.twitter/activator").getLines().toList

  val consumer = Consumer(lines(0), lines(1))
  val token = Token(lines(2), lines(3))

  val authorize: (HttpRequest) => HttpRequest = oAuthAuthorizer(consumer, token)
}
