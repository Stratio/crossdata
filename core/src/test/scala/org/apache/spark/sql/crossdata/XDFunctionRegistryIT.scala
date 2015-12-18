/**
 * Copyright (C) 2015 Stratio (http://stratio.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.spark.sql.crossdata

import java.nio.file.Paths

import org.apache.spark.sql.AnalysisException
import org.apache.spark.sql.catalyst.TableIdentifier
import org.apache.spark.sql.crossdata.test.CoreWithSharedContext
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class XDFunctionRegistryIT extends CoreWithSharedContext {

  "XD Function registry" should "throw an analysis exception when a native udf cannot be resolved" in {

    try {
      sql(s"CREATE TEMPORARY TABLE jsonTable USING org.apache.spark.sql.json OPTIONS (path '${Paths.get(getClass.getResource("/catalog-reference.conf").toURI()).toString}')")

      val missingUDFName = "missingFunction"
      val thrown = the[AnalysisException] thrownBy sql(s"SELECT $missingUDFName() FROM jsonTable")
      thrown.getMessage() should startWith(s"Undefined function $missingUDFName")
    } finally {
      _xdContext.dropTable(TableIdentifier("jsonTable"))
    }

  }

}
