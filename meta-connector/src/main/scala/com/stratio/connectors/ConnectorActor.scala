/*
 * Licensed to STRATIO (C) under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  The STRATIO (C) licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.stratio.connectors

import akka.actor._
import akka.cluster.{Member, Cluster}
import akka.cluster.ClusterEvent._
import akka.util.Timeout
import com.stratio.meta.common.connector.IConnector
import com.stratio.meta.common.result.{ConnectResult, StorageResult, CommandResult, MetadataResult}
import com.stratio.meta.communication._
import com.stratio.meta2.common
import com.stratio.meta2.common.result.Result

import scala.collection.mutable.{ListMap, Map}
import scala.concurrent.duration.DurationInt

object State extends Enumeration {
  type state = Value
  val Started, Stopping, Stopped = Value
}

object ConnectorActor {
  def props(connectorName: String, connector: IConnector): Props = Props(new ConnectorActor(connectorName, connector))
}

class ConnectorActor(connectorName: String, conn: IConnector) extends HeartbeatActor with ActorLogging {
  //class ConnectorActor(connectorName:String,conn:IConnector) extends Actor with ActorLogging {

  implicit val timeout = Timeout(20 seconds)

  //TODO: test if it works with one thread and multiple threads
  val connector = conn
  var state = State.Stopped
  var parentActorRef: ActorRef = null
  var runningJobs: Map[String, ActorRef] = new ListMap[String, ActorRef]()


  override def handleHeartbeat(heartbeat: HeartbeatSig) = {
    //println("receiving heartbeat signal")
    runningJobs.foreach {
      keyval: (String, ActorRef) => keyval._2 ! IAmAlive(keyval._1)
    }
  }

  override def preStart(): Unit = {
    //#subscribe
    Cluster(context.system).subscribe(self, classOf[ClusterDomainEvent])
    //cluster.subscribe(self, initialStateMode = InitialStateAsEvents, classOf[MemberEvent], classOf[UnreachableMember])
  }

  override def receive = super.receive orElse {
    //override def receive = {


    //case RouterRoutees(routees)=> routees foreach context.watch

    //case heartbeat: HeartbeatSig =>  handleHeartbeat(heartbeat)

    case _: com.stratio.meta.communication.Start => {
      //context.actorSelection(RootActorPath(mu.member.address) / "user" / "coordinatorActor")
      parentActorRef = sender
    }

    case connectRequest: com.stratio.meta.communication.Connect => {
      log.debug("->" + "Receiving MetadataRequest")
      log.info("Received connect command")
      connector.connect(connectRequest.credentials, connectRequest.connectorClusterConfig)
      this.state = State.Started //if it doesn't connect, an exception will be thrown and we won't get here
      sender ! ConnectResult.createConnectResult("Connected successfully"); //TODO once persisted sessionId,
      // attach it in this info recover it to
    }

    case _: com.stratio.meta.communication.Shutdown => {
      log.debug("->" + "Receiving Shutdown")
      this.shutdown()
    }

    case ex: Execute => {
      log.info("Processing query: " + ex)
      try {
        runningJobs.put(ex.queryId, sender)
        val result = connector.getQueryEngine().execute(ex.workflow)
        result.setQueryId(ex.queryId)
        sender ! result
        //router forward (connector.getQueryEngine(),ex)

      } catch {
        case e: Exception => {
          val result = Result.createExecutionErrorResult(e.getStackTraceString)
          result.setQueryId(ex.queryId)
          sender ! result
        }
        case err: Error =>
          log.error("error in ConnectorActor( receiving LogicalWorkflow )")
      } finally {
        runningJobs.remove(ex.queryId)
      }
    }

    case metadataOp: MetadataOperation => {
      var qId: String = metadataOp.queryId
      var metadataOperation: Int = 0
      log.info("Received queryId = " + qId)

      try {
        val opclass = metadataOp.getClass().toString().split('.')
        val eng = connector.getMetadataEngine()

        opclass(opclass.length - 1) match {
          case "CreateTable" => {
            println("creating table from  " + self.path)
            qId = metadataOp.asInstanceOf[CreateTable].queryId
            eng.createTable(metadataOp.asInstanceOf[CreateTable].targetCluster,
              metadataOp.asInstanceOf[CreateTable].tableMetadata)
            metadataOperation = MetadataResult.OPERATION_CREATE_TABLE
          }
          case "CreateCatalog" => {
            qId = metadataOp.asInstanceOf[CreateCatalog].queryId
            eng.createCatalog(metadataOp.asInstanceOf[CreateCatalog].targetCluster,
              metadataOp.asInstanceOf[CreateCatalog].catalogMetadata)
            metadataOperation = MetadataResult.OPERATION_CREATE_CATALOG
          }
          case "CreateIndex" => {
            qId = metadataOp.asInstanceOf[CreateIndex].queryId
            eng.createIndex(metadataOp.asInstanceOf[CreateIndex].targetCluster,
              metadataOp.asInstanceOf[CreateIndex].indexMetadata)
          }
          case "DropCatalog" => {
            qId = metadataOp.asInstanceOf[DropIndex].queryId
            eng.createCatalog(metadataOp.asInstanceOf[CreateCatalog].targetCluster,
            metadataOp.asInstanceOf[CreateCatalog].catalogMetadata)

          }
          case "DropIndex" => {
            qId = metadataOp.asInstanceOf[DropIndex].queryId
            eng.dropIndex(metadataOp.asInstanceOf[DropIndex].targetCluster, metadataOp.asInstanceOf[DropIndex].indexMetadata)
            metadataOperation = MetadataResult.OPERATION_DROP_INDEX
          }
          case "DropTable" => {
            qId = metadataOp.asInstanceOf[DropTable].queryId
            eng.dropTable(metadataOp.asInstanceOf[DropTable].targetCluster, metadataOp.asInstanceOf[DropTable].tableName)
            metadataOperation = MetadataResult.OPERATION_DROP_TABLE
          }
          case "CreateTableAndCatalog" => {
            qId = metadataOp.asInstanceOf[CreateTableAndCatalog].queryId
            eng.createCatalog(metadataOp.asInstanceOf[CreateTableAndCatalog].targetCluster,
              metadataOp.asInstanceOf[CreateTableAndCatalog].catalogMetadata)
            eng.createTable(metadataOp.asInstanceOf[CreateTableAndCatalog].targetCluster,
              metadataOp.asInstanceOf[CreateTableAndCatalog].tableMetadata)
            metadataOperation = MetadataResult.OPERATION_CREATE_TABLE
          }
        }
      } catch {
        case ex: Exception => {
          val result = Result.createExecutionErrorResult(ex.getStackTraceString)
          sender ! result
        }
        case err: Error =>
          log.error("error in ConnectorActor( receiving MetaOperation)")
      }
      val result = MetadataResult.createSuccessMetadataResult(metadataOperation)
      result.setQueryId(qId)

      log.info("Sending back queryId = " + qId)

      sender ! result
    }

    case result: Result =>
      println("connectorActor receives Result with ID=" + result.getQueryId())
      parentActorRef ! result
    //TODO:  ManagementWorkflow

    case storageOp: StorageOperation => {
      val qId: String = storageOp.queryId
      try {
        val eng = connector.getStorageEngine()
        storageOp match {
          case Insert(queryId, clustername, table, row) => {
            eng.insert(clustername, table, row)
          }
          case InsertBatch(queryId, clustername, table, rows) => {
            eng.insert(clustername, table, rows)
          }
        }
        val result = StorageResult.createSuccessFulStorageResult("Inserted successfully");
        result.setQueryId(qId)
        sender ! result
      } catch {
        case ex: Exception => {
          log.debug(ex.getStackTraceString)
          val result = common.result.Result.createExecutionErrorResult(ex.getStackTraceString)
          sender ! result
        }
        case err: Error =>
          log.error("error in ConnectorActor( receiving StorageOperation)")
      }
      val result = StorageResult.createSuccessFulStorageResult("Storage operation successful");
      result.setQueryId(qId)
      sender ! result
    }

    case msg: getConnectorName => {
      log.info(sender + " asked for my name")
      sender ! replyConnectorName(connectorName)
    }

    case MemberUp(member) =>
      log.info("Member up")
      log.debug("*******Member is Up: {} {}!!!!!", member.toString, member.getRoles)
    //val actorRefe=context.actorSelection(RootActorPath(member.address) / "user" / "connectoractor" )
    //actorRefe ! "hola "+member.address+ "  "+RootActorPath(member.address)

    case state: CurrentClusterState =>
      log.debug("Current members: {}", state.members.mkString(", "))

    case UnreachableMember(member) =>
      log.debug("Member detected as unreachable: {}", member)

    case MemberRemoved(member, previousStatus) =>
      log.debug("Member is Removed: {} after {}",
        member.address, previousStatus)

    case _: MemberEvent =>
      log.debug("Receiving anything else")

  }

  def shutdown() = {
    println("ConnectorActor is shutting down")
    this.state = State.Stopping
    connector.shutdown()
    this.state = State.Stopped
  }
}

