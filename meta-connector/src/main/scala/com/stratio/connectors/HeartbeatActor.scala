package com.stratio.connectors

import java.util.concurrent.{Executors, TimeUnit}

import akka.actor.{Actor, ActorLogging, Props}
import com.stratio.meta.communication.HeartbeatSig


object HeartbeatActor {
   def props ():Props = Props (new HeartbeatActor() )
 }

class HeartbeatActor() extends Actor with ActorLogging {

   private val scheduler = Executors.newSingleThreadScheduledExecutor()

   private val callback = new Runnable{
     def run = { self ! new HeartbeatSig() }
   }

   scheduler.scheduleAtFixedRate(callback, 0, 500, TimeUnit.MILLISECONDS)

   override def receive: Receive = {
     case hearbeat:HeartbeatSig=>{
       println("HeartbeatActor receives a heartbeat message")

     }
     case _ =>{
       println("HeartbeatActor receives anything else")
     }

   }
 }
