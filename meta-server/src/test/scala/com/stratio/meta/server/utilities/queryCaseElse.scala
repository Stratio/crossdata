package com.stratio.meta.server.utilities

import com.stratio.meta.common.result.{QueryResult, ConnectResult, CommandResult}
import com.stratio.meta.common.ask.Connect
import scala.concurrent.Await
import scala.util.Success
import akka.pattern.ask
import scala.concurrent.duration._
import com.stratio.meta.common.result.Result
/**
 * Created by aalcocer on 4/9/14.
 * To generate unit test of proxy actor
 */


class queryCaseElse{

  import akka.actor.ActorRef

  var complete:Boolean=true
  var myCommandResult :CommandResult=_
  var myQueryResult : QueryResult=_
  var myvalue:ConnectResult=_

  def queryconnect(actor:ActorRef,user:String):ConnectResult={
    val futureExecutorResponse=actor.ask(Connect(user))(2 second)
    try{
      val result = Await.result(futureExecutorResponse, 1 seconds)
    }catch{
      case ex:Exception => {
        complete=false
      }
    }
    if (complete&&futureExecutorResponse.isCompleted){
      val value_response= futureExecutorResponse.value.get

      value_response match{
        case Success(value:ConnectResult)=>{

          myvalue=value
          myvalue

        }
        case _ => ConnectResult.CreateFailConnectResult("error")
      }
    }
    else ConnectResult.CreateFailConnectResult("error")
  }

  def queryelse(actor:ActorRef):Result={
    val futureExecutorResponse=actor.ask(1)(2 second)
    try{
      val result = Await.result(futureExecutorResponse, 1 seconds)
    }catch{
      case ex:Exception => {
        complete=false
      }
    }
    if (complete&&futureExecutorResponse.isCompleted){
      val value_response= futureExecutorResponse.value.get

      value_response match{
        case Success(value:CommandResult)=>{

          myCommandResult=value
          myCommandResult

        }
        case Success(value:QueryResult)=>{
          myQueryResult=value
          myQueryResult
        }
        case _ => CommandResult.CreateFailCommanResult("error")
      }
    }
    else CommandResult.CreateFailCommanResult("error")
  }

}