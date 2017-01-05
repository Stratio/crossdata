/*
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

// TODO make this class generic; then should be moved to a util package
class ShellArgsReader(private val arglist: List[String]) {

  type OptionMap = Map[String, Any]

  val booleanOptions = Seq("tcp", "async")
  val options = nextOption(Map(), arglist)

  object BooleanOptionDisabledByDefault {

    type StringKey = String
    type BooleanValue = Boolean

    def unapply(list: List[String]): Option[(StringKey, BooleanValue, List[String])] = {
      list match {
        case strOptName :: "true" :: tail =>
          Some((strOptName, true, tail))
        case strOptName :: "false" :: tail =>
          Some((strOptName, false, tail))
        case strOptName :: tail =>
          Some((strOptName, true, tail))
      }
    } collect {
      case (strOptName, anyBool, anyList) if strOptName.startsWith("--") =>
        (strOptName.substring("--".length), anyBool, anyList)
    }
  }

  private def nextOption(map: OptionMap, list: List[String]): OptionMap = {

    list match {
      case Nil => map
      case "--user" :: username :: tail =>
        nextOption(map ++ Map("user" -> username), tail)
      case "--timeout" :: timeout :: tail =>
        nextOption(map ++ Map("timeout" -> timeout.toInt), tail)
      case "--query" :: query :: tail =>
        nextOption(map ++ Map("query" -> query), tail)
      case BooleanOptionDisabledByDefault(optName, boolValue, tail) if booleanOptions contains optName =>
        nextOption(map ++ Map(optName -> boolValue), tail)
    }
  }

}