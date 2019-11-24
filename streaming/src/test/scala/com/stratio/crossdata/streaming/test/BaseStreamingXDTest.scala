/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
 */
package com.stratio.crossdata.streaming.test

import com.stratio.crossdata.test.BaseXDTest
import org.scalatest._
import org.scalatest.mock.MockitoSugar

trait BaseStreamingXDTest extends BaseXDTest
with ShouldMatchers
with BeforeAndAfterAll
with BeforeAndAfter
with MockitoSugar

