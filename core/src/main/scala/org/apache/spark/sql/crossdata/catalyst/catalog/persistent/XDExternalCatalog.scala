package org.apache.spark.sql.crossdata.catalyst.catalog.persistent

import com.typesafe.config.Config
import org.apache.spark.sql.catalyst.catalog.ExternalCatalog
import org.apache.spark.sql.crossdata.catalyst.catalog.persistent.XDExternalCatalog._

object XDExternalCatalog {

  trait ExternalCatalogSettings

  case class TypesafeConfigSettings(config: Config) extends ExternalCatalogSettings
  case object NoSettings extends ExternalCatalogSettings

}

abstract class XDExternalCatalog[S <: ExternalCatalogSettings](settings: S = NoSettings) extends ExternalCatalog
