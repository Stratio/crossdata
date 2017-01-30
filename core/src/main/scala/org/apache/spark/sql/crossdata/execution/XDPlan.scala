/*
 * Copyright (C) 2015 Stratio (http://stratio.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.spark.sql.crossdata.execution

import com.stratio.common.utils.components.logger.impl.Slf4jLoggerComponent
import com.stratio.crossdata.connector.NativeScan
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.Row
import org.apache.spark.sql.catalyst.InternalRow
import org.apache.spark.sql.catalyst.expressions.{Alias, Attribute, GetMapValue, GetStructField, Literal}
import org.apache.spark.sql.catalyst.plans.logical.{LeafNode, Limit, LogicalPlan, Project}
import org.apache.spark.sql.execution.{SQLExecution, SparkPlan}
import org.apache.spark.sql.execution.datasources.LogicalRelation


// TODO call-by-name in order to avoid do things when the native execution is available
class XDPlan(optimizedPlan: LogicalPlan, sPlan: => SparkPlan) extends SparkPlan {

  import XDPlan._

  lazy val sparkPlan: SparkPlan = sPlan

  override def executeCollect(): Array[InternalRow] = {

    // TODO Spark2.0 try native execution

    val nativeQueryExecutor: Option[NativeScan] = findNativeQueryExecutor(optimizedPlan)
    nativeQueryExecutor.flatMap(executeNativeQuery(_, optimizedPlan)).getOrElse(sPlan.executeCollect())

  }

  override def executeTake(n: Int): Array[InternalRow] = { // TODO executeTake natively??
    // TODO Spark2.0 try native execution
    val newPlan = Limit(Literal(n), optimizedPlan) // TODO check limits in optimizedPlan

    val nativeQueryExecutor: Option[NativeScan] = findNativeQueryExecutor(newPlan)
    nativeQueryExecutor.flatMap(executeNativeQuery(_, newPlan)).getOrElse(sPlan.executeTake(n))

  }

  // Use SparkPlan implementation TODO Spark2.0 override every method to improve performance ??

  override protected def sparkContext: SparkContext = sPlan.sqlContext.sparkContext

  override protected def doExecute(): RDD[InternalRow] = sPlan.execute()

  override def output: Seq[Attribute] = sPlan.output

  override def children: Seq[SparkPlan] = sPlan.children

  override def productElement(n: Int): Any = sPlan.productElement(n)

  override def productArity: Int = sPlan.productArity

  override def canEqual(that: Any): Boolean = sPlan.canEqual(that)

}


object XDPlan extends Slf4jLoggerComponent{

  /**
    * Finds a [[org.apache.spark.sql.sources.BaseRelation]] mixing-in [[NativeScan]] supporting native execution.
    *
    * The logical plan must involve only base relation from the same datasource implementation. For example,
    * if there is a join with a [[org.apache.spark.rdd.RDD]] the logical plan cannot be executed natively.
    *
    * @param optimizedLogicalPlan the logical plan once it has been processed by the parser, analyzer and optimizer.
    * @return
    */
  def findNativeQueryExecutor(optimizedLogicalPlan: LogicalPlan): Option[NativeScan] = {

    def allLeafsAreNative(leafs: Seq[LeafNode]): Boolean = {
      leafs.forall {
        case LogicalRelation(ns: NativeScan, _, _) => true
        case _ => false
      }
    }

    val leafs = optimizedLogicalPlan.collect { case leafNode: LeafNode => leafNode }

    val nativeQueryExecutor = if (!allLeafsAreNative(leafs)) {
      None
    } else {
      val nativeExecutors: Seq[NativeScan] = leafs.map { case LogicalRelation(ns: NativeScan, _, _) => ns }

      nativeExecutors match {
        case seq if seq.length == 1 =>
          nativeExecutors.headOption

        case _ =>
          if (nativeExecutors.sliding(2).forall { tuple =>
            tuple.head.getClass == tuple.head.getClass
          }) {
            nativeExecutors.headOption
          } else {
            None
          }
      }
    }

    val executionType = nativeQueryExecutor.map(_ => "Spark").getOrElse("Native")
    logger.info(s"$executionType Query: ${optimizedLogicalPlan.simpleString}")
    nativeQueryExecutor
  }

  /**
    * Executes the logical plan.
    *
    * @param provider [[org.apache.spark.sql.sources.BaseRelation]] mixing-in [[NativeScan]]
    * @return an array that contains all of [[Row]]s in this [[LogicalPlan]]
    *         or None if the provider cannot resolve the entire [[LogicalPlan]] natively.
    */
  def executeNativeQuery(provider: NativeScan, plan: LogicalPlan): Option[Array[InternalRow]] = {
    val containsSubfields = notSupportedProject(plan)
    val planSupported = !containsSubfields && plan.map(lp => lp).forall(provider.isSupported(_, plan))
    if(planSupported) {
      // TODO handle failed executions which are currently wrapped within the option, so these jobs will appear duplicated
      // TODO the plan should notice the native execution

      // TODO notSupported => withNewExecutionId{
      provider.buildScan(plan)
      //}

    } else {
      None
    }
  }

  private def notSupportedProject(optimizedLogicalPlan: LogicalPlan): Boolean = {

    optimizedLogicalPlan collectFirst {
      case a@Project(seq, _) if seq.collectFirst { case b: GetMapValue => b }.isDefined => a
      case a@Project(seq, _) if seq.collectFirst { case b: GetStructField => b }.isDefined => a
      case a@Project(seq, _) if seq.collectFirst { case Alias(b: GetMapValue, _) => a }.isDefined => a
      case a@Project(seq, _) if seq.collectFirst { case Alias(b: GetStructField, _) => a }.isDefined => a
    } isDefined
  }
}
