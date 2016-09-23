package org.apache.spark.sql.crossdata.security.api

sealed trait Action {

  def name: String
}

case object Read extends Action {

  val name = "Read"
}

case object Write extends Action {

  val name = "Write"
}

case object Execute extends Action {

  val name = "Execute"
}

case object Create extends Action {

  val name = "Create"
}

case object Delete extends Action {

  val name = "Delete"
}

case object Alter extends Action {

  val name = "Alter"
}

case object Describe extends Action {

  val name = "Describe"
}

case object Other extends Action {

  val name = "Other"
}

case object Access extends Action {

  val name = "Access"
}

case object View extends Action {

  val name = "View"
}

case object Register extends Action {

  val name = "Register"
}

case object Unregister extends Action {

  val name = "Unregister"
}

/*object Action {

  def fromString(action: String): Action = {
    val op = values.find(op => op.name.equalsIgnoreCase(action))
    op.getOrElse(throw new RuntimeException(action + " not a valid operation name. " +
      "The valid names are " + values.mkString(",")))
  }

  def values: Seq[Action] = List(Read, Write, Execute, Create, Delete, Alter, Describe, Other, Access, View,
    Register, Unregister)
}*/