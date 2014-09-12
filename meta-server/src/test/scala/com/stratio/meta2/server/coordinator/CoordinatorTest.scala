package com.stratio.meta2.server.coordinator

import org.scalatest.FunSuite
import org.scalatest.FlatSpec
import com.stratio.meta.common.connector.IConnector
import org.scalamock._
import org.scalamock.scalatest.MockFactory
 
class CoordinatorTest extends FunSuite with MockFactory{
	test("Basic Connector Mock") {
		val m = mock[IConnector]
		(m.getConnectorName _).expects().returning("My New Connector")
		assert(m.getConnectorName().equals("My New Connector"))
	}
}