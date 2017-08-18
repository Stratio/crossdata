package com.stratio.crossdata.streaming.test

import com.stratio.crossdata.test.BaseXDTest
import org.scalatest._
import org.scalatest.mock.MockitoSugar

trait BaseStreamingXDTest extends BaseXDTest
with ShouldMatchers
with BeforeAndAfterAll
with BeforeAndAfter
with MockitoSugar

