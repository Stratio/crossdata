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
package com.stratio.crossdata.driver.shell

import com.stratio.crossdata.common.SQLCommand
import com.stratio.crossdata.driver.Driver

class BasicShell extends App {

  val driver = Driver()

  while(true){
    val line = readLine("CROSSDATA > ")
    val result = driver.syncQuery(new SQLCommand(line))
    println(s"Result for query ID: ${result.queryId}")
    if(result.hasError){
      println("ERROR")
    } else {
      println("INFO")
    }
    result.resultSet.foreach(r => println(r.mkString))
  }

}
