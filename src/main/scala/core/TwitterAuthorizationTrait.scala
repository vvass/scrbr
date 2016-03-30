package core

import spray.http.HttpRequest

/**
  * Created by vvass on 3/29/16.
  */
trait TwitterAuthorizationTrait {
  def authorize: HttpRequest => HttpRequest
}
