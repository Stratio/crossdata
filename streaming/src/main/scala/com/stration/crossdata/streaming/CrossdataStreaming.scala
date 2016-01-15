package com.stration.crossdata.streaming

import com.typesafe.config.Config
import org.apache.spark.{SparkContext, SparkConf}
import org.apache.spark.sql.crossdata.daos.EphemeralTableDAOComponent
import org.apache.spark.sql.crossdata.models.EphemeralOptionsModel
import org.apache.spark.streaming.{Seconds, StreamingContext}

class CrossdataStreaming(ephemeralTableId : String,
                         zookeeperConfiguration: Option[Config],
                        sparkConf : SparkConf)
  extends EphemeralTableDAOComponent {

  override val config = new TypesafeConfig(zookeeperConfiguration)
  val CheckpointDirectory = "/tmp/crossdata/checkpoint"

  def init() : Unit = {

    val ephemeralTable = dao.get(ephemeralTableId).getOrElse(throw new Exception("no table found"))

    val ssc = StreamingContext.getOrCreate(CheckpointDirectory,
      () => {
        createContext(ephemeralTable.options, CheckpointDirectory)
      })

    ssc.start()
    ssc.awaitTermination()
  }

  /**
   * Crear contexto y toda la logica
   */
  def createContext(ephemeralOptionsModel: EphemeralOptionsModel, checkpointDirectory: String) :
  StreamingContext = {
    val sparkStreamingWindow = ephemeralOptionsModel.atomicWindow
    val sc = new SparkContext(sparkConf)
    val ssc = new StreamingContext(sc, Seconds(sparkStreamingWindow))
    ssc.checkpoint(checkpointDirectory)

    val kafkaInput = new KafkaInput(ephemeralOptionsModel.kafkaOptions
    )
    ssc
  }
}
