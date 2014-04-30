/*
 * Stratio Meta
 *
 * Copyright (c) 2014, Stratio, All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 */

package com.stratio.meta.server.server

import akka.testkit.{DefaultTimeout, TestKit}
import akka.actor.{Props, ActorSystem}
import com.typesafe.config.ConfigFactory
import org.scalatest.FunSuiteLike
import com.stratio.meta.server.actors._
import scala.concurrent.duration._
import com.stratio.meta.core.engine.Engine
import org.testng.Assert._
import com.stratio.meta.server.utilities._
import com.stratio.meta.server.config.BeforeAndAfterCassandra
import scala.collection


/**
 * Created by aalcocer on 4/4/14.
 * To generate unit test of query actor
 */
class BasicServerActorTest extends TestKit(ActorSystem("TestKitUsageSpec",ConfigFactory.parseString(TestKitUsageSpec.config)))
with DefaultTimeout with FunSuiteLike with BeforeAndAfterCassandra
{

  lazy val engine:Engine =  createEngine.create()


  lazy val serverRef=system.actorOf(Props(classOf[ServerActor],engine),"test")

  lazy val process=new queryCaseElse

  lazy val queryConnect=process.queryconnect(serverRef,"testactor")
  lazy val CommandResult=process.queryelse(serverRef)


  override def beforeCassandraFinish() {
    shutdown(system)
  }

  override def afterAll() {
    super.afterAll()
    engine.shutdown()
  }


  test ("ServerActor Test"){

    within(5000 millis){

      serverRef ! 1
      expectNoMsg()

    }
  }



  test ("ServerActor Test connect without error"){


    within(5000 millis){


      assertEquals(queryConnect.hasError,false)
    }
  }
  test ("ServerActor Test connect error message"){

    within(5000 millis){

      assertEquals(queryConnect.getErrorMessage,null)
    }
  }
  test ("ServerActor Test connect ks changed"){

    within(5000 millis){

      assertEquals(queryConnect.isKsChanged,false)
    }
  }
  test ("ServerActor Test connect current ks"){

    within(5000 millis){

      assertEquals(queryConnect.getCurrentKeyspace,null)
    }
  }



  test ("ServerActor Test send nothing"){

    within(5000 millis){

      assertEquals(CommandResult.getErrorMessage,"Not recognized object")
    }
  }

  val querying= new queryString


  test ("server Test"){

    within(5000 millis){

      serverRef ! 1
      expectNoMsg()

    }
  }
  test ("ServerActor create KS"){

    within(5000 millis){

      val msg= "create KEYSPACE ks_demo WITH replication = {class: SimpleStrategy, replication_factor: 1};"
      assertEquals(querying.proccess(msg,serverRef,engine,4),"sucess" )

    }
  }
  test ("ServerActor create KS yet"){

    within(5000 millis){

      val msg="create KEYSPACE ks_demo WITH replication = {class: SimpleStrategy, replication_factor: 1};"
      assertEquals(querying.proccess(msg,serverRef,engine,4),"Keyspace ks_demo already exists." )
    }
  }

  test ("ServerActor use KS"){

    within(5000 millis){

      val msg="use ks_demo ;"
      assertEquals(querying.proccess(msg,serverRef,engine,4),"sucess" )
    }
  }

  test ("ServerActor use KS yet"){

    within(5000 millis){

      val msg="use ks_demo ;"
      assertEquals(querying.proccess(msg,serverRef,engine,4),"sucess" )
    }
  }



  test ("ServerActor insert into table not create yet without error"){

    within(5000 millis){

      val msg="insert into demo (field1, field2) values ('test1','text2');"
      assertEquals(querying.proccess(msg,serverRef,engine,4),"Table demo does not exists." )
    }
  }
  test ("ServerActor select without table"){

    within(5000 millis){

      val msg="select * from demo ;"
      assertEquals(querying.proccess(msg,serverRef,engine,4),"Table demo does not exists.")
    }
  }


  test ("ServerActor create table not create yet"){

    within(5000 millis){

      val msg="create TABLE demo (field1 varchar PRIMARY KEY , field2 varchar);"
      assertEquals(querying.proccess(msg,serverRef,engine,4),"sucess" )
    }
  }

  test ("ServerActor create table  create yet"){

    within(5000 millis){

      val msg="create TABLE demo (field1 varchar PRIMARY KEY , field2 varchar);"
      assertEquals(querying.proccess(msg,serverRef,engine,4),"Table demo already exists." )
    }
  }

  test ("ServerActor insert into table  create yet without error"){

    within(5000 millis){

      val msg="insert into demo (field1, field2) values ('test1','text2');"
      assertEquals(querying.proccess(msg,serverRef,engine,4),"sucess" )
    }
  }
  test ("ServerActor select"){

    within(5000 millis){

      val msg="select * from demo ;"
      assertEquals(querying.proccess(msg,serverRef,engine,4),collection.mutable.MutableList("test1", "text2").toString() )
    }
  }
  test ("ServerActor drop table "){

    within(5000 millis){

      val msg="drop table demo ;"
      assertEquals(querying.proccess(msg,serverRef,engine,4),"sucess" )
    }
  }
  test ("ServerActor drop KS "){

    within(5000 millis){

      val msg="drop keyspace ks_demo ;"
      assertEquals(querying.proccess(msg,serverRef,engine,4),"sucess" )
    }
  }
  test ("ServerActor drop KS  not exit"){

    within(5000 millis){

      val msg="drop keyspace ks_demo ;"
      assertEquals(querying.proccess(msg,serverRef,engine,4),"Keyspace ks_demo does not exists." )
    }
  }





}









