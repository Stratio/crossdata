package org.apache.spark.sql.crossdata.models

import org.apache.spark.sql.crossdata.catalog.XDCatalog.CrossdataIndex

case class IndexModel(indexId:String, crossdataIndex: CrossdataIndex)
