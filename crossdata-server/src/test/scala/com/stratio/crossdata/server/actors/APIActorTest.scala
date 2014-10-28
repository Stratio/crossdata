
package com.stratio.crossdata.server.actors
import com.stratio.crossdata.common.ask.{APICommand, Command}
import com.stratio.crossdata.common.result.Result
import com.stratio.crossdata.server.config.{ServerConfig, ActorReceiveUtils}
import org.apache.log4j.Logger
import org.scalatest.{Suite, FunSuiteLike}
import akka.testkit.TestActorRef
import akka.pattern.ask
import scala.concurrent.ExecutionContext.Implicits.global



class APIActorTest extends ActorReceiveUtils with FunSuiteLike with ServerConfig {
  this: Suite =>

  override lazy val logger = Logger.getLogger(classOf[APIActorTest])


  val actorRef= TestActorRef(APIActor.props(new APIManagerMock()))


  test("Send COMMAND must WORK"){
    val cmd=new Command(APICommand.LIST_CONNECTORS,null);
    val future = (actorRef ? cmd).mapTo[Result]
    future.onSuccess {
      case r => {
        assert(!r.hasError)
      }
    }
  }

  test("Send other object must FAIL"){
    val cmd=6
    val future = (actorRef ? cmd).mapTo[Result]
    future.onSuccess {
      case r => {
        assert(r.hasError)
      }
    }
  }

}
