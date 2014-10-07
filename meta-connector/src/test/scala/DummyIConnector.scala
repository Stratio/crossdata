import com.stratio.meta.common.connector._
import com.stratio.meta.common.security.ICredentials
import com.stratio.meta2.common.data.ClusterName

/**
 * Created by carlos on 6/10/14.
 */
class DummyIConnector extends IConnector{
  override def getConnectorName: String = "myDummyConnector"
  override def getDatastoreName: Array[String] = null
  override def shutdown(): Unit = null
  override def init(configuration: IConfiguration): Unit = null
  override def getMetadataEngine: IMetadataEngine = new DummyIMetadataEngine()
  override def getQueryEngine: IQueryEngine = null
  override def isConnected(name: ClusterName): Boolean = false
  override def close(name: ClusterName): Unit = null
  override def connect(credentials: ICredentials, config: ConnectorClusterConfig): Unit = null
  override def getStorageEngine: IStorageEngine = null
}
