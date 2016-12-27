package com.scrbr.utilities.routes

import spray.routing.HttpService

/**
  * Created by vvass on 4/15/16.
  */
trait MainServiceRoute extends HttpService { // TODO we need to remove this

  implicit val executionContext = actorRefFactory.dispatcher

  val route = {
    path("ping") {
      complete("pong")
    }
  } ~ path("index" | "") {
    complete(index)
  }

  lazy val index =
    <html>
      <body>
        <h1>Say hello to <i>spray-servlet</i>!</h1>
        <p>Defined resources:</p>
        <ul>
          <li><a href="/ping">/ping</a></li>
        </ul>
      </body>
    </html>



}
