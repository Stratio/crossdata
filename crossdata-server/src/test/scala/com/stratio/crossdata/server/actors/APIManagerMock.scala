package com.stratio.crossdata.server.actors

import com.stratio.crossdata.common.ask.Command
import com.stratio.crossdata.common.result.{CommandResult, Result}
import com.stratio.crossdata.core.api.APIManager

class APIManagerMock extends APIManager {
  override def processRequest(cmd:Command):Result={
    CommandResult.createCommandResult("OK MOTHER FUCKER")
  }
}
