package com.stratio.crossdata.driver.querybuilder.dslentities

import com.stratio.crossdata.driver.querybuilder.CrossdataSQLStatement
import com.stratio.crossdata.driver.querybuilder.RunnableQuery


object CombineType extends Enumeration {
  type CombineType = Value
  val UnionAll = Value("UNION ALL")
  val Intersect = Value("INTERSECT")
  val Except = Value("EXCEPT")
  val UnionDistinct = Value("UNION DISTINCT")
}


import com.stratio.crossdata.driver.querybuilder.dslentities.CombineType.CombineType

case class CombinationInfo(combineType: CombineType, runnableQuery: RunnableQuery) extends CrossdataSQLStatement {
  override private[querybuilder] def toXDQL: String = s" ${combineType.toString} ${runnableQuery.toXDQL}"
}