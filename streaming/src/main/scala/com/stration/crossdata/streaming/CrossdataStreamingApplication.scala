package com.stration.crossdata.streaming

import org.apache.spark.Logging
import org.apache.spark.streaming.StreamingContext

object CrossdataStreamingApplication extends App with Logging {

  val checkpointDirectory = "/tmp/crossdata/checkpoint"

  def createContext(checkpointDirectory: String) : StreamingContext = {

  }

  val ssc = StreamingContext.getOrCreate(checkpointDirectory,
    () => {
      createContext(checkpointDirectory)
    })
  ssc.start()
  ssc.awaitTermination()
}
