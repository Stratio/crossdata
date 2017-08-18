package org.apache.spark.sql.crossdata.session

import org.apache.spark.sql.crossdata.XDSQLConf
import org.apache.spark.sql.crossdata.catalog.interfaces.XDTemporaryCatalog

class XDSessionState(val sqlConf: XDSQLConf, val temporaryCatalogs: Seq[XDTemporaryCatalog])
