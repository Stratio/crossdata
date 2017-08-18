/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
 */
package org.apache.spark.sql.crossdata.models

import org.apache.spark.sql.crossdata.models.EphemeralOptionsModel._

case class EphemeralOptionsModel(kafkaOptions: KafkaOptionsModel,
                                 atomicWindow: Int = DefaultAtomicWindow,
                                 maxWindow: Int = DefaultMaxWindow,
                                 outputFormat: EphemeralOutputFormat.Value = EphemeralOutputFormat.ROW,
                                 checkpointDirectory: String = DefaultCheckpointDirectory,
                                 sparkOptions: Map[String, String] = Map.empty) {

  def toPrettyString : String = ModelUtils.modelToJsonString(this)
}

object EphemeralOptionsModel {

  /**
   * Default minimum Time in Seconds for the Batch Interval in SparkStreaming.
   * This parameter mark the the minimum time for the windowed queries
   */
  val DefaultAtomicWindow = 5
  val DefaultMaxWindow = 60
  val DefaultCheckpointDirectory = "checkpoint/crossdata"
}