package com.stratio.crossdata.common

import com.stratio.crossdata.test.BaseXDTest
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class MessagesSpec extends BaseXDTest {

  "A SQLCommand" should "generate random uuids" in {
    SQLCommand("a").queryId should not be SQLCommand("a").queryId
  }

}
