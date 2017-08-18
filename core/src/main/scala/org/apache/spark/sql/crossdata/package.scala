package org.apache.spark.sql

import scala.io.Source

package object crossdata {

  lazy val CrossdataVersion = Source.fromInputStream(getClass.getResourceAsStream("/crossdata.version")).mkString

}
