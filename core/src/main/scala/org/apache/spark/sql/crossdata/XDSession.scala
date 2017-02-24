package org.apache.spark.sql.crossdata

import com.stratio.common.utils.components.logger.impl.Slf4jLoggerComponent
import java.beans.Introspector
import java.io.File
import java.util.UUID
import java.util.concurrent.atomic.AtomicReference

import com.typesafe.config.{Config, ConfigFactory, ConfigValue}

import scala.collection.JavaConverters._
import scala.reflect.ClassTag
import scala.reflect.runtime.universe.TypeTag
import scala.util.control.NonFatal
import org.apache.spark.{SPARK_VERSION, SparkConf, SparkContext}
import org.apache.spark.annotation.{DeveloperApi, Experimental}
import org.apache.spark.api.java.JavaRDD
import org.apache.spark.internal.Logging
import org.apache.spark.rdd.RDD
import org.apache.spark.scheduler.{SparkListener, SparkListenerApplicationEnd}
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}
import org.apache.spark.sql.catalog.Catalog
import org.apache.spark.sql.catalyst._
import org.apache.spark.sql.catalyst.encoders._
import org.apache.spark.sql.catalyst.expressions.AttributeReference
import org.apache.spark.sql.catalyst.plans.logical.{LocalRelation, Range}
import org.apache.spark.sql.crossdata.session.{XDSessionState, XDSharedState}
import org.apache.spark.sql.execution._
import org.apache.spark.sql.execution.datasources.LogicalRelation
import org.apache.spark.sql.execution.ui.SQLListener
import org.apache.spark.sql.internal.{CatalogImpl, SessionState, SharedState}
import org.apache.spark.sql.sources.BaseRelation
import org.apache.spark.sql.streaming._
import org.apache.spark.sql.types.{DataType, LongType, StructType}
import org.apache.spark.sql.util.ExecutionListenerManager
import org.apache.spark.util.Utils

import scala.util.Try

// TODO add implicits => flattenCollect and collect(ExecutionType)
class XDSession private(
                         @transient override val sparkContext: SparkContext,
                         @transient private val existingSharedState: Option[XDSharedState],
                         //TODO: Make this attribute just a parameter
                         @transient private[crossdata] val catalogConfig: Config
                       )
  extends SparkSession(sparkContext) with Serializable with Slf4jLoggerComponent { self =>


  import XDSession.SessionId

  /**
    * Unique session id
    */
  val id: SessionId = UUID.randomUUID() /* Note that `randomUUID` is thread safe:
                                         * http://bugs.java.com/view_bug.do?bug_id=6611830
                                         */

  private[sql] def this(sc: SparkContext, catalogConf: Config = ConfigFactory.empty()) {
    this(sc, None, catalogConf)
  }

  /**
    * State shared across sessions, including the [[SparkContext]], cached data, listener,
    * and a catalog that interacts with external systems.
    */
  @transient
  private[sql] override lazy val sharedState: XDSharedState =
    existingSharedState.getOrElse(new XDSharedState(sparkContext, catalogConfig))


  /**
    * State isolated across sessions, including SQL configurations, temporary tables, registered
    * functions, and everything else that accepts a [[org.apache.spark.sql.internal.SQLConf]].
    */
  @transient
  private[sql] override lazy val sessionState: SessionState =
    new XDSessionState(self)


  /* ----------------------- *
   |  Session-related state  |
   * ----------------------- */

   /*

    /**
      * Runtime configuration interface for Spark.
      *
      * This is the interface through which the user can get and set all Spark and Hadoop
      * configurations that are relevant to Spark SQL. When getting the value of a config,
      * this defaults to the value set in the underlying [[SparkContext]], if any.
      *
      * @since 2.0.0
      */
    @transient override lazy val conf: RuntimeConfig = new RuntimeConfig(sessionState.conf)


    /**
      * :: Experimental ::
      * A collection of methods that are considered experimental, but can be used to hook into
      * the query planner for advanced functionality.
      *
      * @since 2.0.0
      */
    @Experimental
  override def experimental: ExperimentalMethods = sessionState.experimentalMethods

    /**
      * A collection of methods for registering user-defined functions (UDF).
      * Note that the user-defined functions must be deterministic. Due to optimization,
      * duplicate invocations may be eliminated or the function may even be invoked more times than
      * it is present in the query.
      *
      * The following example registers a Scala closure as UDF:
      * {{{
      *   sparkSession.udf.register("myUDF", (arg1: Int, arg2: String) => arg2 + arg1)
      * }}}
      *
      * The following example registers a UDF in Java:
      * {{{
      *   sparkSession.udf().register("myUDF",
      *       new UDF2<Integer, String, String>() {
      *           @Override
      *           public String call(Integer arg1, String arg2) {
      *               return arg2 + arg1;
      *           }
      *      }, DataTypes.StringType);
      * }}}
      *
      * Or, to use Java 8 lambda syntax:
      * {{{
      *   sparkSession.udf().register("myUDF",
      *       (Integer arg1, String arg2) -> arg2 + arg1,
      *       DataTypes.StringType);
      * }}}
      *
      * @since 2.0.0
      */
  override def udf: UDFRegistration = sessionState.udf


    /**
      * Start a new session with isolated SQL configurations, temporary tables, registered
      * functions are isolated, but sharing the underlying [[SparkContext]] and cached data.
      *
      * Note: Other than the [[SparkContext]], all shared state is initialized lazily.
      * This method will force the initialization of the shared state to ensure that parent
      * and child sessions are set up with the same shared state. If the underlying catalog
      * implementation is Hive, this will initialize the metastore, which may take some time.
      *
      * @since 2.0.0
      */
  override def newSession(): SparkSession = {
      new XDSession(sparkContext, Some(sharedState))
    }

    /* ------------------------- *
     |  Catalog-related methods  |
     * ------------------------- */

    /**
      * Interface through which the user may create, drop, alter or query underlying
      * databases, tables, functions etc.
      *
      * @since 2.0.0
      */
    @transient override lazy val catalog: Catalog = new CatalogImpl(self)

    /**
      * Returns the specified table as a [[DataFrame]].
      *
      * @since 2.0.0
      */
  override def table(tableName: String): DataFrame = {
      table(sessionState.sqlParser.parseTableIdentifier(tableName))
    }

/*    private[sql] def table(tableIdent: TableIdentifier): DataFrame = {
      Dataset.ofRows(self, sessionState.catalog.lookupRelation(tableIdent))
    }*/

    /* ----------------- *
     |  Everything else  |
     * ----------------- */

    /**
      * Executes a SQL query using Spark, returning the result as a [[DataFrame]].
      * The dialect that is used for SQL parsing can be configured with 'spark.sql.dialect'.
      *
      * @since 2.0.0
      */
  override def sql(sqlText: String): DataFrame = {
      Dataset.ofRows(self, sessionState.sqlParser.parsePlan(sqlText))
    }

    /**
      * Stop the underlying [[SparkContext]].
      *
      * @since 2.0.0
      */
    override def stop(): Unit = {
      sparkContext.stop()
    }
    */
}


// TODO XDSession => Remove Hive => Require user(vs default??) or throw a new exception
// TODO XDSession => XDSessionProvider => builder => it is in charge of create new XDSession
object XDSession {

  import SparkSession.{Builder => SparkSessionBuilder}

  type SessionId = UUID

  /**
    * Builder for [[XDSession]].
    */
  class Builder extends SparkSessionBuilder with BuilderEnhancer {

    private[this] val options = new scala.collection.mutable.HashMap[String, String]
    private[this] var userSuppliedContext: Option[SparkContext] = None

    override def config(key: String, value: String): Builder = synchronized {
      options += key -> value
      this
    }

    override def config(key: String, value: Long): Builder = config(key, value)
    override def config(key: String, value: Double): Builder = config(key, value)
    override def config(key: String, value: Boolean): Builder = config(key, value)

    override def config(conf: SparkConf): Builder = synchronized {
      conf.getAll.foreach { case (k, v) => options += k -> v }
      this
    }


    override def master(master: String): Builder = config("spark.master", master)

    override def enableHiveSupport(): Builder =
      throw new RuntimeException("Crossdata is not compatible with Hive")

    override private[spark] def sparkContext(sparkContext: SparkContext): Builder = synchronized {
      userSuppliedContext = Option(sparkContext)
      this
    }

    override def getOrCreate(): SparkSession = {
      log.warn("Avoid calling `XDSession#getOrCreate` when using Crossdata as a library")
      super.getOrCreate()
    }

    /**
      * Builds a new session for a given user id.
      *
      */
    def create(userId: String): XDSession = synchronized {
      /*
       * TODO: Analyse the security risks derived from having the user
       * in the config and move its value to a XDSession attribute if
       * those risks are of real importance.
       */
      config("crossdata.security.user", userId)

      // Extracted from [[SparkSession]]'s getOrCreate:
      // No active nor global default session. Create a new one.
      val sparkContext = userSuppliedContext.getOrElse {
        // set app name if not given
        val randomAppName = java.util.UUID.randomUUID().toString
        val sparkConf = new SparkConf()
        options.foreach { case (k, v) => sparkConf.set(k, v) }
        if (!sparkConf.contains("spark.app.name")) {
          sparkConf.setAppName(randomAppName)
        }
        val sc = SparkContext.getOrCreate(sparkConf)
        // maybe this is an existing SparkContext, update its SparkConf which maybe used
        // by SparkSession
        options.foreach { case (k, v) => sc.conf.set(k, v) }
        if (!sc.conf.contains("spark.app.name")) {
          sc.conf.setAppName(randomAppName)
        }
        sc
      }

      val catalogConf = extractCatalogConf(options)
      val session = new XDSession(sparkContext, catalogConf)
      options.foreach { case (k, v) => session.sessionState.conf.setConfString(k, v) }

      session
    }

  }

  /**
    * Creates a [[SparkSession.Builder]] for constructing a [[SparkSession]].
    *
    * @since 2.0.0
    */
  def builder(): Builder = new Builder

}

