import com.stratio.meta.common.connector._
import com.stratio.meta.common.result.QueryResult
import com.stratio.meta2.common.data.{CatalogName, ClusterName, TableName}
import com.stratio.meta2.common.metadata.{CatalogMetadata, IndexMetadata, TableMetadata}

/**
 * Created by carlos on 6/10/14.
 */
class DummyIMetadataEngine extends IMetadataEngine{
  override def createCatalog(targetCluster: ClusterName, catalogMetadata: CatalogMetadata): Unit = null

  override def createTable(targetCluster: ClusterName, tableMetadata: TableMetadata): Unit = {
      println("very slow function")
      for (i <- 1 to 5) {
        Thread.sleep(1000)
        println(i + " seconds gone by ----")
      }
      println("very Slow process (end)")
      QueryResult.createSuccessQueryResult()
  }

  override def createIndex(targetCluster: ClusterName, indexMetadata: IndexMetadata): Unit = null

  override def dropCatalog(targetCluster: ClusterName, name: CatalogName): Unit = null

  override def dropTable(targetCluster: ClusterName, name: TableName): Unit = null

  override def dropIndex(targetCluster: ClusterName, indexMetadata: IndexMetadata): Unit = null
}
