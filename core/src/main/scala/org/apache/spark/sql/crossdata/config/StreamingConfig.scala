package org.apache.spark.sql.crossdata.config

import com.stratio.common.utils.components.config.impl.TypesafeConfigComponent
import org.apache.log4j.Logger
import org.apache.spark.sql.crossdata.XDContext
import org.apache.spark.sql.crossdata.models._
import org.apache.spark.sql.crossdata.config.StreamingConstants._

object StreamingConfig extends TypesafeConfigComponent{

  def createEphemeralTableModel(ident: String, opts : Map[String, String]) : EphemeralTableModel = {

    val finalOptions = getEphemeralTableOptions(opts)

    val connections = finalOptions(kafkaConnection)
      .split(",").map(_.split(":")).map{
      case c if c.size == 3 => ConnectionHostModel(c(0), c(1), c(2))
    }.toSeq

    val topics = finalOptions(kafkaTopic)
      .split(",").map(_.split(":")).map{
      case l if l.size == 2 => TopicModel(l(0), l(1).toInt)
    }.toSeq

    val groupId = finalOptions(kafkaGroupId)
    val partition = finalOptions.get(kafkaPartition)
    val kafkaAdditionalOptions = finalOptions.filter{case (k, v) => k.startsWith(kafkaAdditionalOptionsKey)}
    val storageLevel = finalOptions.getOrElse(kafkaStorageLevel, defaultKafkaStorageLevel)
    val kafkaOptions = KafkaOptionsModel(connections, topics, groupId, partition, kafkaAdditionalOptions, storageLevel)
    val minW = finalOptions(atomicWindow).toInt
    val maxW = finalOptions(maxWindow).toInt
    val outFormat = finalOptions.getOrElse(outputFormat, defaultOutputFormat) match {
      case "JSON" => EphemeralOutputFormat.JSON
      case other => EphemeralOutputFormat.ROW
    }

    val checkpoint = finalOptions.getOrElse(checkpointDirectory, defaultCheckpointDirectory)
    val sparkOpts = finalOptions.filter{case (k, v) => k.startsWith(sparkOptionsKey)}
    val ephemeralOptions = EphemeralOptionsModel(kafkaOptions, minW, maxW, outFormat, checkpoint, sparkOpts)

    EphemeralTableModel(ident, ephemeralOptions)

  }

  // Options with order preference
  private def getEphemeralTableOptions(opts : Map[String, String]): Map[String, String] = {
    // first if opts has confFilePath, we take configFile, and then opts if there is no confFilePath , we take opts
    val filePath = opts.get(streamingConfFilePath)
    val options = filePath.fold(opts)(extractConfigFromFile) ++ opts

    options ++ listMandatoryEphemeralTableKeys
      .map(key => key -> options.getOrElse(key, notFound(key, defaultEphemeralTableMapConfig))).toMap

  }

  def createEphemeralQueryModel(ident: String, opts : Map[String, String]) : Either[Seq[String], EphemeralQueryModel] = {

    val finalOptions = getOptions(opts)

    val notPresent = Seq(queryAlias, querySql) filterNot (finalOptions contains _)
    val parametersOk = notPresent.isEmpty

    parametersOk match {
      case true   =>
        val alias = finalOptions(queryAlias)
        val sql = finalOptions(querySql)
        val window = finalOptions.getOrElse(queryWindow, "5").toInt
        val queryOptions = finalOptions.filter{case (k, v) => k.startsWith(queryOptionsKey)}
        Right(EphemeralQueryModel(ident, alias, sql, window, queryOptions))

      case false  =>
        Left(notPresent.map(notFoundMandatory))
    }
  }

  // Options with order preference
  private def getOptions(opts : Map[String, String]): Map[String, String] = {
    val filePath = opts.get(streamingConfFilePath)
    filePath.fold(opts)(extractConfigFromFile) ++ opts
  }


  private def extractConfigFromFile(filePath : String): Map[String, String] = {

    val configFile =
      new TypesafeConfig(
        None,
        None,
        Some(filePath),
        Some(CoreConfig.ParentConfigName + "." + XDContext.StreamingConfigKey)
      )

    configFile.toStringMap
  }

  // Return default value
  def notFound(key: String, mandatoryOptions: Map[String, String]): String = {
    val logger= Logger.getLogger(loggerName)
    logger.warn(s"Mandatory parameter $key not specified, this could cause errors.")

    mandatoryOptions(key)
  }

  // Return a message
  def notFoundMandatory(key: String): String = {
    val logger= Logger.getLogger(loggerName)
    val message = s"Mandatory parameter $key not specified, you have to specify it."
    logger.error(message)

    "ERROR: " +  message
  }

}
