package com.stratio.meta.server.query

import akka.testkit.{DefaultTimeout, TestKit}
import akka.actor.{Props, ActorSystem}
import com.typesafe.config.ConfigFactory
import org.scalatest.{BeforeAndAfterAll, FunSuiteLike}
import com.stratio.meta.server.actors._
import scala.concurrent.duration._
import org.apache.log4j.Logger
import com.stratio.meta.common.result.{QueryResult, Result}
import com.stratio.meta.core.engine.{Engine, EngineConfig}
import com.stratio.meta.common.ask.Query
import com.stratio.meta.common.ask.Query
import akka.pattern.ask
import scala.concurrent.Await
import scala.util.{Failure, Success}
import com.stratio.meta.common.data.{Cell, CassandraResultSet}
import scala.collection.JavaConversions._
import scala.collection.mutable.MutableList
import org.testng.Assert._



/**
 * Created by aalcocer on 4/4/14.
 * To generate unit test of query actor
 */
class BasicQueryActorTest extends TestKit(ActorSystem("TestKitUsageSpec",ConfigFactory.parseString(TestKitUsageSpec.config)))
with DefaultTimeout with FunSuiteLike with BeforeAndAfterAll
{

    val engineConfig: EngineConfig = {
    val result=new EngineConfig
    result.setCassandraHosts(Array[String]("127.0.0.1"))
    result.setCassandraPort(9042)
    result
  }
  lazy val engine: Engine = new Engine(engineConfig)


  val queryRef=system.actorOf(Props(classOf[QueryActor],engine))


  override def afterAll() {
    shutdown(system)
  }


  test ("QueryActor Test"){

    within(2000 millis){

      queryRef ! 1
      expectNoMsg

    }
  }
  test ("QueryActor create KS"){

    within(2000 millis){

      var msg=Query ("ks_demo", "create KEYSPACE ks_demo WITH replication = {class: SimpleStrategy, replication_factor: 1};", "usr_demo")
      assertEquals(proccess(msg),"Create KS" )
    }
  }
  test ("QueryActor create KS yet"){

    within(2000 millis){

      var msg=Query ("ks_demo", "create KEYSPACE ks_demo WITH replication = {class: SimpleStrategy, replication_factor: 1};", "usr_demo")
      assertEquals(proccess(msg),"Keyspace ks_demo already exists" )
    }
  }

  test ("QueryActor use KS"){

    within(2000 millis){

      var msg=Query ("ks_demo", "use ks_demo ;", "usr_demo")
      assertEquals(proccess(msg),"use KS" )
    }
  }

  test ("QueryActor use KS yet"){

    within(2000 millis){

      var msg=Query ("ks_demo", "use ks_demo ;", "usr_demo")
      assertEquals(proccess(msg),"use KS" )
    }
  }


  test ("QueryActor use KS not create"){

    within(2000 millis){

      var msg=Query ("ks_demo", "use ks_demo_not ;", "usr_demo")
      assertEquals(proccess(msg),"Keyspace 'ks_demo_not' does not exist" )
    }
  }
  test ("QueryActor insert into table not create yet without error"){

    within(2000 millis){

      var msg=Query ("ks_demo", "insert into demo (field1, field2) values ('test1','text2');", "usr_demo")
      assertEquals(proccess(msg),"unconfigured columnfamily demo" )
    }
  }
  test ("QueryActor select without table"){

    within(2000 millis){

      var msg=Query ("ks_demo", "select * from demo ;", "usr_demo")
      assertEquals(proccess(msg),"unconfigured columnfamily demo")
    }
  }


    test ("QueryActor create table not create yet"){

    within(2000 millis){

      var msg=Query ("ks_demo", "create TABLE demo (field1 text PRIMARY KEY , field2 text);", "usr_demo")
      assertEquals(proccess(msg),"create table" )
    }
  }

  test ("QueryActor create table  create yet"){

    within(2000 millis){

      var msg=Query ("ks_demo", "create TABLE demo (field1 text PRIMARY KEY , field2 text);", "usr_demo")
      assertEquals(proccess(msg),"Table ks_demo.demo already exists" )
    }
  }
  test ("QueryActor insert into table  create yet with error"){

    within(2000 millis){

      var msg=Query ("ks_demo", "insert into demo (field1, field2) value ('test1','text2');", "usr_demo")
      assertEquals(proccess(msg),"\u001B[31mParser exception: \u001B[0m\n\tError recognized: line 1:34: no viable alternative at input 'value'\n\tinsert into demo (field1, field2) \u001B[35m|\u001B[0mvalue ('test1','text2');\n\tDid you mean: \"VALUES\"?\n\t" )
    }
  }
  test ("QueryActor insert into table  create yet without error"){

    within(2000 millis){

      var msg=Query ("ks_demo", "insert into demo (field1, field2) values ('test1','text2');", "usr_demo")
      assertEquals(proccess(msg),"insert into" )
    }
  }
  test ("QueryActor select"){

    within(2000 millis){

      var msg=Query ("ks_demo", "select * from demo ;", "usr_demo")
      assertEquals(proccess(msg),MutableList("'test1'", "'text2'").toString() )
    }
  }
  test ("QueryActor drop table "){

    within(2000 millis){

      var msg=Query ("ks_demo", "drop table demo ;", "usr_demo")
      assertEquals(proccess(msg),"drop table")
    }
  }
  test ("QueryActor drop KS "){

    within(2000 millis){

      var msg=Query ("ks_demo", "drop keyspace ks_demo ;", "usr_demo")
      assertEquals(proccess(msg),"error")
    }
  }
  test ("QueryActor drop KS  not exit"){

    within(2000 millis){

      var msg=Query ("ks_demo", "drop keyspace ks_demo ;", "usr_demo")
      assertEquals(proccess(msg),"Cannot drop non existing keyspace 'ks_demo'." )
    }
  }



  def proccess (msg:Query) : (String) ={

    var complete:Boolean= true
    val futureExecutorResponse=queryRef.ask(msg)(1 second)
    try{
      val result = Await.result(futureExecutorResponse, 2 seconds)
    }catch{
      case ex:Exception => {
        ex.getMessage
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
                  if (msg.statement.contains("create KEYSPACE")){
                    "Create KS"
                  }
                  else if(msg.statement.contains("use")){
                    "use KS"
                  }

                    else if (msg.statement.contains("create TABLE")){
                    "create table"
                  }
                  else if (msg.statement.contains("insert into")){
                    "insert into"
                  }
                  else if (msg.statement.contains("drop table")){
                    "drop table"
                  }
                  else if (msg.statement.contains("drop keyspace")){
                    "error"
                  }

                  else {
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
    else "error"
  }
}
object TestKitUsageSpec {
  val config = """
    akka {
    loglevel = "WARNING"
    }
               """
}

