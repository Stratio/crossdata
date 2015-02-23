package com.stratio.crossdata.server.actors

import akka.actor.{Actor, Props}
import spray.http.MediaTypes._
import spray.routing.HttpService

object RestActor {
  def props(): Props = Props(new RestActor())
}

class RestActor extends Actor with RestService {
  def actorRefFactory = context

  def receive = runRoute(rootPath)
}

trait RestService extends HttpService {

  val rootPath =
    path("") {
      get {
        respondWithMediaType(`text/html`) {
          // XML is marshalled to `text/xml` by default, so we simply override here
          complete {
            <html>
              <body>
                <img src="http://cdn.meme.am/instances/500x/59501523.jpg" alt="Crossdata" />
              </body>
            </html>
          }
        }
      }
    }


}
