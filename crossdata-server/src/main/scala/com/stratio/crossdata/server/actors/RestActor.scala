package com.stratio.crossdata.server.actors

import akka.actor.{Actor, Props}
import spray.http.MediaTypes._
import spray.routing.HttpService
import spray.httpx.SprayJsonSupport._
import spray.json._



case class JsonQuery(query: String)

object JsonImplicits extends DefaultJsonProtocol {
  implicit val impJsonQuery= jsonFormat1(JsonQuery)
}

object RestActor {
  def props(): Props = Props(new RestActor())
}

class RestActor extends Actor with RestService {
  def actorRefFactory = context

  def receive = runRoute(rootPath)
}

trait RestService extends HttpService {

  import JsonImplicits._
  val rootPath =
    path("") {
      get {
        respondWithMediaType(`text/html`) {
          // XML is marshalled to `text/xml` by default, so we simply override here
          complete {
            <html>
              <body>
                <img src="http://cdn.meme.am/instances/500x/59501523.jpg" alt="Crossdata"/>
              </body>
            </html>
          }
        }
      }
    } ~
      path("query") {
        post {
          entity(as[JsonQuery]) {
            query : JsonQuery=>
              complete {
               "Received " + query.query
              }
          }
        }
      }
}
