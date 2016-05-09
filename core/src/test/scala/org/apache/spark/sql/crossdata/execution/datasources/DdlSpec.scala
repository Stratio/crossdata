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
package org.apache.spark.sql.crossdata.execution.datasources

import java.sql.Timestamp
import java.text.SimpleDateFormat

import com.stratio.crossdata.test.BaseXDTest
import org.apache.spark.sql.types._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.mock.MockitoSugar

@RunWith(classOf[JUnitRunner])
class DdlSpec extends BaseXDTest with MockitoSugar{


  "Ddl" should  """successfully convert from ByteType to Byte""" in {

    DDLUtils.convertSparkDatatypeToScala("4", ByteType) shouldBe (4 : Byte)

  }

  "Ddl" should  """successfully convert from ShortType to Short""" in {

    DDLUtils.convertSparkDatatypeToScala("6", ShortType) shouldBe (6 : Short)

  }

  "Ddl" should  """successfully convert from IntegerType to Integer""" in {

    DDLUtils.convertSparkDatatypeToScala("25", IntegerType) shouldBe (25 : Int)

  }

  "Ddl" should  """successfully convert from LongType to Long""" in {

    DDLUtils.convertSparkDatatypeToScala("-127", LongType) shouldBe (-127 : Long)

  }

  "Ddl" should  """successfully convert from FloatType to Float""" in {

    DDLUtils.convertSparkDatatypeToScala("-1.01", FloatType) shouldBe (-1.01f : Float)

  }

  "Ddl" should  """successfully convert from DoubleType to Double""" in {

    DDLUtils.convertSparkDatatypeToScala("3.75", DoubleType) shouldBe (3.75 : Double)

  }

  "Ddl" should  """successfully convert from DecimalType to BigDecimal""" in {

    DDLUtils.convertSparkDatatypeToScala("-106.75", DecimalType.SYSTEM_DEFAULT) shouldBe BigDecimal(-106.75)

  }

  "Ddl" should  """successfully convert from StringType to String""" in {

    DDLUtils.convertSparkDatatypeToScala("abcde", StringType) shouldBe "abcde"

  }

  "Ddl" should  """successfully convert from BooleanType to Boolean""" in {

    DDLUtils.convertSparkDatatypeToScala("false", BooleanType) shouldBe (false : Boolean)

  }

  "Ddl" should  """successfully convert from DateType to Date""" in {

    DDLUtils.convertSparkDatatypeToScala("2010-01-02", DateType) shouldBe new SimpleDateFormat().parse("2010-01-02")

  }

  "Ddl" should  """successfully convert from TimestampType to Timestamp""" in {

    DDLUtils.convertSparkDatatypeToScala("1988-08-11 11:12:13", TimestampType) shouldBe Timestamp.valueOf("1988-08-11 11:12:13")

  }


}
