package com.stratio.meta.server.executor

import com.stratio.meta.core.engine.{Engine, EngineConfig}
import akka.actor.{ActorSystem, Props}
import com.stratio.meta.server.actors.{TimeTracker, ExecutorActor}
import akka.testkit._
import com.typesafe.config.ConfigFactory
import org.scalatest.{BeforeAndAfterAll, FunSuiteLike}
import com.stratio.meta.core.utils.MetaQuery
import com.stratio.meta.common.result.{QueryResult, Result}
import com.stratio.meta.common.data.{Cell, CassandraResultSet}
import scala.concurrent.duration._
import com.stratio.meta.common.ask.Query
import scala.concurrent.Await
import scala.collection.mutable.MutableList
import scala.util.{Failure, Success}
import akka.pattern.ask
import org.testng.Assert._
import scala.util.Failure
import scala.util.Success
import scala.collection.JavaConversions._
import com.stratio.meta.server.config.BeforeAndAfterCassandra


/**
 * Created by aalcocer on 4/8/14.
 * To generate unit test of proxy actor
 */
class BasicExecutorActorTest extends TestKit(ActorSystem("TestKitUsageExectutorActorSpec",
  ConfigFactory.parseString(TestKitUsageSpec.config)))
with DefaultTimeout with BeforeAndAfterCassandra
{

  val engineConfig: EngineConfig = {
    val result=new EngineConfig
    result.setCassandraHosts(Array[String]("127.0.0.1"))
    result.setCassandraPort(9042)
    result
  }
  lazy val engine: Engine = new Engine(engineConfig)

  val executorRef = system.actorOf(ExecutorActor.props(engine.getExecutor),"TestExecutorActor")

  //val executorRef=system.actorOf(Props(classOf[ExecutorActor],engine.getExecutor),"testExecutorActor")

  override def beforeCassandraFinish() {
    shutdown(system)
  }


  test ("executor Test"){

    within(2000 millis){

      executorRef ! 1
      expectNoMsg

    }
  }
  test ("QueryActor create KS"){

    within(2000 millis){

      var msg= "create KEYSPACE ks_demo WITH replication = {class: SimpleStrategy, replication_factor: 1};"
      assertEquals(proccess(msg),"Create KS" )
    }
  }
  test ("QueryActor create KS yet"){

    within(2000 millis){

      var msg="create KEYSPACE ks_demo WITH replication = {class: SimpleStrategy, replication_factor: 1};"
      assertEquals(proccess(msg),"Keyspace ks_demo already exists" )
    }
  }

  test ("QueryActor use KS"){

    within(2000 millis){

      var msg="use ks_demo ;"
      assertEquals(proccess(msg),"use KS" )
    }
  }

  test ("QueryActor use KS yet"){

    within(2000 millis){

      var msg="use ks_demo ;"
      assertEquals(proccess(msg),"use KS" )
    }
  }


  test ("QueryActor use KS not create"){

    within(2000 millis){

      var msg="use ks_demo_not ;"
      assertEquals(proccess(msg),"Keyspace 'ks_demo_not' does not exist" )
    }
  }
  test ("QueryActor insert into table not create yet without error"){

    within(2000 millis){

      var msg="insert into demo (field1, field2) values ('test1','text2');"
      assertEquals(proccess(msg),"unconfigured columnfamily demo" )
    }
  }
  test ("QueryActor select without table"){

    within(2000 millis){

      var msg="select * from demo ;"
      assertEquals(proccess(msg),"unconfigured columnfamily demo")
    }
  }


  test ("QueryActor create table not create yet"){

    within(2000 millis){

      var msg="create TABLE demo (field1 text PRIMARY KEY , field2 text);"
      assertEquals(proccess(msg),"create table" )
    }
  }

  test ("QueryActor create table  create yet"){

    within(2000 millis){

      var msg="create TABLE demo (field1 text PRIMARY KEY , field2 text);"
      assertEquals(proccess(msg),"Table ks_demo.demo already exists" )
    }
  }

  test ("QueryActor insert into table  create yet without error"){

    within(2000 millis){

      var msg="insert into demo (field1, field2) values ('test1','text2');"
      assertEquals(proccess(msg),"insert into" )
    }
  }
  test ("QueryActor select"){

    within(2000 millis){

      var msg="select * from demo ;"
      assertEquals(proccess(msg),MutableList("'test1'", "'text2'").toString() )
    }
  }
  test ("QueryActor drop table "){

    within(2000 millis){

      var msg="drop table demo ;"
      assertEquals(proccess(msg),"drop table")
    }
  }
  test ("QueryActor drop KS "){

    within(2000 millis){

      var msg="drop keyspace ks_demo ;"
      assertEquals(proccess(msg),"error")
    }
  }
  test ("QueryActor drop KS  not exit"){

    within(2000 millis){

      var msg="drop keyspace ks_demo ;"
      assertEquals(proccess(msg),"Cannot drop non existing keyspace 'ks_demo'." )
    }
  }

  def proccess (msg:String) : (String) ={

    var complete:Boolean= true
    val stmt = engine.getParser.parseStatement(msg)
    stmt.setTargetKeyspace("ks_demo")
    val stmt1=engine.getValidator.validateQuery(stmt)
    val stmt2=engine.getPlanner.planQuery(stmt1)
    val futureExecutorResponse=executorRef.ask(stmt2)(2 second)
    try{
      val result = Await.result(futureExecutorResponse, 1 seconds)
    }catch{
      case ex:Exception => {
        println("\n\n\n"+ex.getMessage+"\n\n\n")
        complete=false
      }
    }
    if (complete&&futureExecutorResponse.isCompleted){
      val value_response= futureExecutorResponse.value.get
      var valueTable:MutableList[String]=MutableList()
      value_response match{
        case Success(value:Result)=>{
          if (!value.hasError){
            if(value.isInstanceOf[QueryResult]){

              val typeQuery=value.asInstanceOf[QueryResult].getResultSet

              if (typeQuery.isInstanceOf[CassandraResultSet]){

                if(!typeQuery.asInstanceOf[CassandraResultSet].isEmpty){
                  val cells=value.asInstanceOf[QueryResult].getResultSet.asInstanceOf[CassandraResultSet].getRows.get(0).getCells
                  for( key <- cells.keySet()){
                    var cell:Cell=cells.get(key)
                    val str= cell.getDatatype.cast(cell.getValue)

                    valueTable += str.toString

                  }

                  valueTable.toString()

                }
                else{
                  if (msg.contains("create KEYSPACE")){
                    "Create KS"
                  }
                  else if(msg.contains("use")){
                    "use KS"
                  }

                  else if (msg.contains("create TABLE")){
                    "create table"
                  }
                  else if (msg.contains("insert into")){
                    "insert into"
                  }
                  else if (msg.contains("drop table")){
                    "drop table"
                  }
                  else if (msg.contains("drop keyspace")){
                    "error"
                  }

                  else {
                    println("\n\n\n esto es lo que no vemos" + msg+ "\n\n")
                    "error"
                  }
                }
              }
              else{
                "it is not cassandraresult"
              }



            }
            else{
              "it is not a queryResult"
            }

          }
          else {


            value.getErrorMessage.toString
          }

        }
        case Failure(ex) => "ERROR"
        case _ => "not sopported"
      }


    }
    else{

      println("\n\n\n esto es lo que no vemos 2" + "\n\n")

      "error"
    }
  }
}




object TestKitUsageSpec {
  val config = """
    akka {
    loglevel = "WARNING"
    }
    akka.remote.netty.tcp.port=13331
               """
}