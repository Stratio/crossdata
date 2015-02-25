package com.stratio.crossdata.server.actors

import akka.actor.{ActorRef, Actor, Props}
import com.stratio.crossdata.common.ask.Query
import spray.http.MediaTypes._
import spray.routing.HttpService
import spray.httpx.SprayJsonSupport._
import spray.json._


case class JsonQuery(query: String)

object JsonImplicits extends DefaultJsonProtocol {
  implicit val impJsonQuery = jsonFormat1(JsonQuery)
}

object RestActor {
  def props(server: ActorRef): Props = Props(new RestActor(server))
}

class RestActor(server: ActorRef) extends Actor with RestService {
  def actorRefFactory = context
  def sendToServer (q:String) = {
                server! new Query("id", "catalog", q, "user")
                "Received " + q
}
  def receive = runRoute(rootPath)
}

trait RestService extends HttpService {

  import JsonImplicits._

  def sendToServer(q: String):String
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
            query: JsonQuery =>
              complete {
                sendToServer(query.query)
              }
          }
        }
      }
}
