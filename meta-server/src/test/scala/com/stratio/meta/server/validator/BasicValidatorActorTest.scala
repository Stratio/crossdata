package com.stratio.meta.server.validator

import com.stratio.meta.core.engine.Engine
import akka.actor.ActorSystem
import com.stratio.meta.server.actors.{ValidatorActor, PlannerActor, ExecutorActor}
import akka.testkit._
import org.scalatest.FunSuiteLike
import com.stratio.meta.common.result.Result
import scala.concurrent.duration._
import scala.concurrent.Await
import akka.pattern.ask
import org.testng.Assert._
import scala.util.Success
import com.stratio.meta.server.utilities._
import com.stratio.meta.server.config.BeforeAndAfterCassandra
import com.typesafe.config.ConfigFactory
import scala.collection.mutable


/**
 * Created by aalcocer on 4/8/14.
 * To generate unit test of proxy actor
 */
class BasicValidatorActorTest extends TestKit(ActorSystem("TestKitUsageExectutorActorSpec",ConfigFactory.parseString(TestKitUsageSpec.config)))
with DefaultTimeout with FunSuiteLike with BeforeAndAfterCassandra
{
  lazy val engine:Engine =  createEngine.create()

  lazy val executorRef = system.actorOf(ExecutorActor.props(engine.getExecutor),"TestExecutorActor")
  lazy val plannerRef = system.actorOf(PlannerActor.props(executorRef,engine.getPlanner),"TestPlanerActor")
  lazy val validatorRef = system.actorOf(ValidatorActor.props(plannerRef,engine.getValidator),"TestValidatorActor")

  lazy val validatorRefTest= system.actorOf(ValidatorActor.props(testActor,engine.getValidator),"TestPlanerActorTest")


  override def beforeCassandraFinish() {
    shutdown(system)
  }



  test("validator resend to planner message 1"){
    within(2000 millis){

      val query="create KEYSPACE ks_demo1 WITH replication = {class: SimpleStrategy, replication_factor: 1};"
      val stmt = engine.getParser.parseStatement(query)
      stmt.setTargetKeyspace("ks_demo1")
      validatorRefTest ! stmt
      expectMsg(engine.getValidator.validateQuery(stmt))
    }
  }
  test("validator resend to planner message 2"){
    within(2000 millis){

      val query="create KEYSPACE ks_demo1 WITH replication = {class: SimpleStrategy, replication_factor: 1};"
      val stmt = engine.getParser.parseStatement(query)
      stmt.setTargetKeyspace("ks_demo1")
      stmt.setError()
      validatorRefTest ! stmt
      expectNoMsg()
    }
  }
  test("validator resend to planner message 3"){
    within(2000 millis){

      val query="create KEYSPACE ks_demo1 WITH replication = {class: SimpleStrategy, replication_factor: 1};"
      val stmt = engine.getParser.parseStatement(query)
      stmt.setTargetKeyspace("ks_demo1")
      stmt.setError()
      stmt.setErrorMessage("it is a test of error")
      var complete:Boolean=true
      val futureExecutorResponse=validatorRefTest.ask(stmt)(2 second)
      try{
        val result = Await.result(futureExecutorResponse, 1 seconds)
      }catch{
        case ex:Exception =>
          println("\n\n\n"+ex.getMessage+"\n\n\n")
          complete=false

      }
      if (complete&&futureExecutorResponse.isCompleted){
        val value_response= futureExecutorResponse.value.get

        value_response match{
          case Success(value:Result)=>
            if (value.hasError){
              assertEquals(value.getErrorMessage,"it is a test of error")

          }
          case _ =>
        }

      }
    }
  }

  val querying= new queryString

  test ("validator Test"){

    within(3000 millis){

      validatorRef ! 1
      expectNoMsg()

    }
  }
  test ("validatorActor create KS"){

    within(3000 millis){

      val msg= "create KEYSPACE ks_demo WITH replication = {class: SimpleStrategy, replication_factor: 1};"
      assertEquals(querying.proccess(msg,validatorRef,engine,3),"sucess" )

    }
  }
  test ("validatorActor create KS yet"){

    within(3000 millis){

      val msg="create KEYSPACE ks_demo WITH replication = {class: SimpleStrategy, replication_factor: 1};"
      assertEquals(querying.proccess(msg,validatorRef,engine,3),"Keyspace ks_demo already exists." )
    }
  }

  test ("validatorActor use KS"){

    within(3000 millis){

      val msg="use ks_demo ;"
      assertEquals(querying.proccess(msg,validatorRef,engine,3),"sucess" )
    }
  }

  test ("validatorActor use KS yet"){

    within(3000 millis){

      val msg="use ks_demo ;"
      assertEquals(querying.proccess(msg,validatorRef,engine,3),"sucess" )
    }
  }



  test ("validatorActor insert into table not create yet without error"){

    within(3000 millis){

      val msg="insert into demo (field1, field2) values ('test1','text2');"
      assertEquals(querying.proccess(msg,validatorRef,engine,3),"Table demo does not exists." )
    }
  }
  test ("validatorActor select without table"){

    within(3000 millis){

      val msg="select * from demo ;"
      assertEquals(querying.proccess(msg,validatorRef,engine,3),"Table demo does not exists.")
    }
  }


  test ("validatorActor create table not create yet"){

    within(3000 millis){

      val msg="create TABLE demo (field1 varchar PRIMARY KEY , field2 varchar);"
      assertEquals(querying.proccess(msg,validatorRef,engine,3),"sucess" )
    }
  }

  test ("validatorActor create table  create yet"){

    within(3000 millis){

      val msg="create TABLE demo (field1 varchar PRIMARY KEY , field2 varchar);"
      assertEquals(querying.proccess(msg,validatorRef,engine,3),"Table demo already exists." )
    }
  }

  test ("validatorActor insert into table  create yet without error"){

    within(3000 millis){

      val msg="insert into demo (field1, field2) values ('test1','text2');"
      assertEquals(querying.proccess(msg,validatorRef,engine,3),"sucess" )
    }
  }
  test ("validatorActor select"){

    within(3000 millis){

      val msg="select * from demo ;"
      assertEquals(querying.proccess(msg,validatorRef,engine,3),mutable.MutableList("test1", "text2").toString() )
    }
  }
  test ("validatorActor drop table "){

    within(3000 millis){

      val msg="drop table demo ;"
      assertEquals(querying.proccess(msg,validatorRef,engine,3),"sucess" )
    }
  }
  test ("validatorActor drop KS "){

    within(3000 millis){

      val msg="drop keyspace ks_demo ;"
      assertEquals(querying.proccess(msg,validatorRef,engine,3),"sucess" )
    }
  }
  test ("validatorActor drop KS  not exit"){

    within(3000 millis){

      val msg="drop keyspace ks_demo ;"
      assertEquals(querying.proccess(msg,validatorRef,engine,3),"Keyspace ks_demo does not exists." )
    }
  }

}




