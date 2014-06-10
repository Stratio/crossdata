package com.stratio.meta.server.utilities

import scala.concurrent.{Future, Await}
import scala.collection.mutable.MutableList
import scala.util.{Failure, Success}
import com.stratio.meta.common.result.{CommandResult, QueryResult, Result}
import com.stratio.meta.common.data.{Cell, CassandraResultSet}
import akka.actor.ActorRef
import com.stratio.meta.core.engine.Engine
import akka.pattern.ask
import scala.concurrent.duration._
import com.stratio.meta.common.ask.Query
import scala.collection.JavaConversions._


/**
 * To generate unit test of proxy actor
 */
class queryString {

  def proccess (msg:String,actor:ActorRef, engine:Engine, typeActor:Int) : (String) ={
//typeActor is: 1 Executor, 2: Planner, 3:Validator, 4:Parser,Query,Server
    var complete:Boolean= true
    val futureExecutorResponse:Future[Any]={
    typeActor match {
      case 1=>
        val stmt = engine.getParser.parseStatement(msg)
        stmt.setSessionKeyspace("ks_demo")
        val stmt1=engine.getValidator.validateQuery(stmt)
        val stmt2=engine.getPlanner.planQuery(stmt1)
        actor.ask(stmt2)(5 second)

      case 2 =>
        val stmt = engine.getParser.parseStatement(msg)
        stmt.setSessionKeyspace("ks_demo")
        val stmt1=engine.getValidator.validateQuery(stmt)
        actor.ask(stmt1)(5 second)

      case 3 =>
        val stmt = engine.getParser.parseStatement(msg)
        stmt.setSessionKeyspace("ks_demo")
        actor.ask(stmt)(5 second)

      case 4 =>
        val stmt=Query ("demo", "ks_demo", msg, "usr_demo")
        actor.ask(stmt)(5 second)
      case _ =>
        actor.ask("error")(5 second)

      }

    }

    try{
      val result = Await.result(futureExecutorResponse, 5 seconds)
    }catch{
      case ex:Exception =>
        println("\n\n\n"+ex.getMessage+"\n\n\n")
        complete=false

    }
    if (complete&&futureExecutorResponse.isCompleted){
      val value_response= futureExecutorResponse.value.get
      var valueTable:MutableList[String]=MutableList()
      value_response match{
        case Success(value:Result)=>
          if (!value.hasError){
            value match{
              case typeQuery:QueryResult=>
              case typeQuery:CommandResult=>
            }
            if(value.isInstanceOf[QueryResult]){

              val typeQuery=value.asInstanceOf[QueryResult].getResultSet

              if (typeQuery.isInstanceOf[CassandraResultSet]){

                if(!typeQuery.asInstanceOf[CassandraResultSet].isEmpty){
                  val cells=typeQuery.asInstanceOf[CassandraResultSet].getRows.get(0).getCells
                  println("Cells: " + cells)
                  for( key <- cells.keySet()){
                    val cell:Cell=cells.get(key)
                    println("cell: " + cell)
                    val colDefs = typeQuery.asInstanceOf[CassandraResultSet].getColumnMetadata;
                    println("colDefs: " + colDefs)
                    val str = colDefs.get(0).getType.getDbClass.cast(cell.getValue)
                    valueTable += str.toString
                  }

                  valueTable.toString()

                }
                else {

                  "sucess"
                }
              }
              else{
                "it is not cassandraresult"
              }
            }else if(value.isInstanceOf[CommandResult]){
              "success"
            }else{
              "it is not a queryResult"
            }

          }
          else {


            value.getErrorMessage
          }


        case Failure(ex) => "ERROR"
        case unknown : Any => "Message not supported: " + unknown
      }


    }
    else{


      "error"
    }
  }


}
