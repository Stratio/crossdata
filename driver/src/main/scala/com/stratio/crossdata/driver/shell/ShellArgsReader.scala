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