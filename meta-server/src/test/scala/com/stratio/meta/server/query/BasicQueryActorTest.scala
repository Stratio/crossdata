package com.stratio.meta.server.query

import akka.testkit.{DefaultTimeout, TestKit}
import akka.actor.{Props, ActorSystem}
import com.typesafe.config.ConfigFactory
import org.scalatest.FunSuiteLike
import com.stratio.meta.server.actors._
import scala.concurrent.duration._
import com.stratio.meta.core.engine.Engine
import org.testng.Assert._
import com.stratio.meta.server.utilities._
import scala.collection.mutable
import com.stratio.meta.server.config.BeforeAndAfterCassandra

class BasicQueryActorTest extends TestKit(ActorSystem("TestKitUsageSpec",ConfigFactory.parseString(TestKitUsageSpec.config)))
with DefaultTimeout with FunSuiteLike with BeforeAndAfterCassandra
{

  lazy val engine:Engine =  createEngine.create()


  lazy val queryRef=system.actorOf(Props(classOf[QueryActor],engine))


  lazy val process2=new queryCaseElse
  lazy val myCommandResult=process2.queryelse(queryRef)


  override def beforeCassandraFinish() {
    shutdown(system)
  }

  override def afterAll() {
    super.afterAll()
    engine.shutdown()
  }

  test ("ServerActor Test send nothing"){

    within(5000 millis){

      assertEquals(myCommandResult.getErrorMessage, "Message not recognized")
    }
  }

  val querying= new queryString


  test ("query Test"){

    within(5000 millis){

      queryRef ! 1
      expectNoMsg()

    }
  }
  test ("queryActor create KS"){

    within(5000 millis){

      val msg= "create KEYSPACE ks_demo WITH replication = {class: SimpleStrategy, replication_factor: 1};"
      assertEquals(querying.proccess(msg,queryRef,engine,4),"sucess" )

    }
  }
  test ("queryActor create KS yet"){

    within(5000 millis){

      val msg="create KEYSPACE ks_demo WITH replication = {class: SimpleStrategy, replication_factor: 1};"
      assertEquals(querying.proccess(msg,queryRef,engine,4),"Keyspace ks_demo already exists." )
    }
  }

  test ("queryActor use KS"){

    within(5000 millis){

      val msg="use ks_demo ;"
      assertEquals(querying.proccess(msg,queryRef,engine,4),"sucess" )
    }
  }

  test ("queryActor use KS yet"){

    within(5000 millis){

      val msg="use ks_demo ;"
      assertEquals(querying.proccess(msg,queryRef,engine,4),"sucess" )
    }
  }



  test ("queryActor insert into table not create yet without error"){

    within(5000 millis){

      val msg="insert into demo (field1, field2) values ('test1','text2');"
      assertEquals(querying.proccess(msg,queryRef,engine,4),"Table demo does not exist." )
    }
  }
  test ("queryActor select without table"){

    within(5000 millis){

      val msg="select * from demo ;"
      assertEquals(querying.proccess(msg,queryRef,engine,4),"Table demo does not exist.")
    }
  }


  test ("queryActor create table not create yet"){

    within(5000 millis){

      val msg="create TABLE demo (field1 varchar PRIMARY KEY , field2 varchar);"
      assertEquals(querying.proccess(msg,queryRef,engine,4),"sucess" )
    }
  }

  test ("queryActor create table  create yet"){

    within(5000 millis){

      val msg="create TABLE demo (field1 varchar PRIMARY KEY , field2 varchar);"
      assertEquals(querying.proccess(msg,queryRef,engine,4),"Table demo already exists." )
    }
  }

  test ("queryActor insert into table  create yet without error"){

    within(5000 millis){

      val msg="insert into demo (field1, field2) values ('test1','text2');"
      assertEquals(querying.proccess(msg,queryRef,engine,4),"sucess" )
    }
  }
  test ("queryActor select"){

    within(5000 millis){

      val msg="select * from demo ;"
      assertEquals(querying.proccess(msg,queryRef,engine,4),mutable.MutableList("test1", "text2").toString() )
    }
  }
  test ("queryActor drop table "){

    within(5000 millis){

      val msg="drop table demo ;"
      assertEquals(querying.proccess(msg,queryRef,engine,4),"sucess" )
    }
  }
  test ("queryActor drop KS "){

    within(5000 millis){

      val msg="drop keyspace ks_demo ;"
      assertEquals(querying.proccess(msg,queryRef,engine,4),"sucess" )
    }
  }
  test ("queryActor drop KS  not exit"){

    within(5000 millis){

      val msg="drop keyspace ks_demo ;"
      assertEquals(querying.proccess(msg,queryRef,engine,4),"Keyspace ks_demo does not exist." )
    }
  }





}


