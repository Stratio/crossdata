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
package com.stratio.crossdata.driver.querybuilder.dslentities

import java.sql.{Date, Timestamp}

import com.stratio.crossdata.driver.querybuilder.Expression

case class Literal(value: Any) extends Expression {
  override private[querybuilder] def toXDQL: String = value match {
    // TODO http://spark.apache.org/docs/latest/sql-programming-guide.html
    case _: String => s"'$value'"
    case _: Date => s"'$value'"
    case _: Timestamp => s"'$value'"
    case _ => value.toString
  }
}
