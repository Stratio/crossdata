package com.stratio.crossdata.driver.querybuilder

import com.stratio.crossdata.driver.InitialSelectPhrases
import com.stratio.crossdata.driver.querybuilder.dslentities.{AsteriskExpression, XDQLStatement}

object InsertMode extends Enumeration {
  type InsertMode = Value
  val OVERWRITE = Value("OVERWRITE")
  val INTO = Value("INTO")
}

class Insert {
  def into(rel: Relation): ConfiguredInsert = new ConfiguredInsert(rel, InsertMode.INTO)
  def overwrite(rel: Relation): ConfiguredInsert = new ConfiguredInsert(rel, InsertMode.OVERWRITE)

  private[Insert] class ConfiguredInsert(val target: Relation, mode: InsertMode.InsertMode) extends InitialSelectPhrases
  {
    override protected def selectImp(projections: Seq[Expression]): ProjectedSelect =
      new ProjectedSelect(projections:_*)(qStr => s"INSERT $mode ${target.toXDQL} $qStr")
  }

  private[Insert] class RunnableInsert

}