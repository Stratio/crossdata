package com.stratio.crossdata.server.actors


import java.util.UUID

import akka.actor.{Actor, ActorRef, Props}
import akka.pattern.ask
import akka.util.Timeout
import com.stratio.crossdata.common.ask.Query
import com.stratio.crossdata.common.data.{ResultSet, Row}
import com.stratio.crossdata.common.metadata.ColumnMetadata
import com.stratio.crossdata.common.result.{ErrorResult, QueryResult, Result}
import org.apache.log4j.Logger
import spray.http.MediaTypes._
import spray.httpx.SprayJsonSupport._
import spray.json._
import spray.routing.HttpService

import scala.collection.JavaConversions._
import scala.concurrent.Await
import scala.concurrent.duration._

case class JsonQuery(query: String)

object JsonImplicits extends DefaultJsonProtocol {
  implicit val impJsonQuery = jsonFormat1(JsonQuery)
}

object RestActor {
  def props(server: ActorRef): Props = Props(new RestActor(server))
}

class RestActor(server: ActorRef) extends Actor with RestService {
  /**
   * Class logger.
   */
  lazy val logger = Logger.getLogger(getClass)


  def actorRefFactory = context

  def sendToServer(q: String): String = {

    logger.info("SENT QUERY " + q)
    implicit val timeout = Timeout(5 seconds)
    val future = server.ask(new Query(UUID.randomUUID.toString, "", q, System.getProperty("user.name"),
      "sessionTest"))
    val result = Await.result(future, timeout.duration).asInstanceOf[Result]

    result match {
      case error: ErrorResult =>
        val sb: StringBuilder = new StringBuilder("The operation for query ")
        sb.append(error.getQueryId).append(" cannot be executed:").append(System.lineSeparator)
        sb.append(error.getErrorMessage).append(System.lineSeparator)
        sb.toString()

      case query: QueryResult =>
        val sb: StringBuilder = new StringBuilder
        if (query.getResultSet.isEmpty) {
          return "EMPTY result"
        }

        val resultSet: ResultSet = query.getResultSet
        for (c: ColumnMetadata <- resultSet.getColumnMetadata) {
          sb.append(c.getName.getColumnNameToShow).append("\t")
        }
        sb.replace(sb.length - 1, sb.length, "\n")

        for (r: Row <- resultSet.getRows) {
          for ((k,v)<- r.getCells)
          {
            sb.append(v).append("\t")
          }
          sb.replace(sb.length - 1, sb.length, "\n")
        }

        sb.toString()

      case _ => result.toString
    }
  }

  def receive = runRoute(rootPath)

}

trait RestService extends HttpService {

  import com.stratio.crossdata.server.actors.JsonImplicits._

  def sendToServer(q: String): String

  val rootPath =
    path("") {
      get {
        respondWithMediaType(`text/html`) {
          // XML is marshalled to `text/xml` by default, so we simply override here
          complete {
            "Crossdata API Rest online"
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
