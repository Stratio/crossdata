package org.apache.spark.sql.crossdata.daos.impl

import com.typesafe.config.Config
import org.apache.spark.sql.crossdata.daos.TableDAOComponent


class TableTypesafeDAO(configuration: Config) extends TableDAOComponent {

  override val config = new TypesafeConfig(Option(configuration))

}
