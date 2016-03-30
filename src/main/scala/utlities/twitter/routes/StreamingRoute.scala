package utlities.twitter.routes

import shapeless.get
import spray.http.MediaTypes._
import spray.routing.HttpService


/**
  * Created by vvass on 3/30/16.
  */
trait StreamingRoute extends HttpService {
  def streamingRoute =
    get {
      path("") {
        respondWithMediaType(`text/html`) {
          // XML is marshalled to `text/xml` by default, so we simply override here
          complete(<html></html>)
        }
      }
    }
}

abstract class MainStreamingRoute extends StreamingRoute {
  
}