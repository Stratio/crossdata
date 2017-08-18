package com.stratio.crossdata.common.security

import java.util.UUID

import akka.actor.ActorRef

case class Session(id: UUID, clientRef: Option[ActorRef]) extends Serializable

