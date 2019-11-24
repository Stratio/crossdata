/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
 */
package org.apache.spark.sql.crossdata

import java.nio.file.Paths

import org.apache.spark.sql.AnalysisException
import org.apache.spark.sql.catalyst.TableIdentifier
import org.apache.spark.sql.crossdata.test.SharedXDContextTest
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class XDFunctionRegistryIT extends SharedXDContextTest {

  "XD Function registry" should "throw an analysis exception when a native udf cannot be resolved" in {

    try {
      xdContext.sql(s"CREATE TEMPORARY TABLE jsonTable USING org.apache.spark.sql.json OPTIONS (path '${Paths.get(getClass.getResource("/core-reference.conf").toURI()).toString}')")

      val missingUDFName = "missingFunction"
      val thrown = the[AnalysisException] thrownBy sql(s"SELECT $missingUDFName() FROM jsonTable")
      thrown.getMessage() should startWith(s"Undefined function $missingUDFName")
    } finally {
      _xdContext.dropTable(TableIdentifier("jsonTable"))
    }

  }

}
