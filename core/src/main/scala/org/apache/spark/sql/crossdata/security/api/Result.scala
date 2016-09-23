package org.apache.spark.sql.crossdata.security.api

sealed trait Result {

  def name: String
}

case object Success extends Result {

  override val name = "Success"
}

case object Fail extends Result {

  override val name = "Fail"
}

case object ResourceNotFound extends Result {

  override val name = "Resource not found"
}

case object PluginNotRegistered extends Result {

  override val name = "The plugin is not registered in the System"
}