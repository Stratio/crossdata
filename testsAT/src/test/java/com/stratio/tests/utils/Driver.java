package com.stratio.tests.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

import com.stratio.crossdata.common.data.ClusterName;
import com.stratio.crossdata.common.data.ColumnName;
import com.stratio.crossdata.common.data.ConnectorName;
import com.stratio.crossdata.common.data.DataStoreName;
import com.stratio.crossdata.common.exceptions.ApiException;
import com.stratio.crossdata.common.exceptions.ConnectionException;
import com.stratio.crossdata.common.exceptions.ExecutionException;
import com.stratio.crossdata.common.exceptions.ManifestException;
import com.stratio.crossdata.common.exceptions.ParsingException;
import com.stratio.crossdata.common.exceptions.UnsupportedException;
import com.stratio.crossdata.common.exceptions.ValidationException;
import com.stratio.crossdata.common.exceptions.validation.ExistNameException;
import com.stratio.crossdata.common.exceptions.validation.NotExistNameException;
import com.stratio.crossdata.common.manifest.ConnectorFactory;
import com.stratio.crossdata.common.manifest.ConnectorType;
import com.stratio.crossdata.common.manifest.CrossdataManifest;
import com.stratio.crossdata.common.manifest.DataStoreFactory;
import com.stratio.crossdata.common.manifest.DataStoreType;
import com.stratio.crossdata.common.metadata.ColumnType;
import com.stratio.crossdata.common.result.CommandResult;
import com.stratio.crossdata.common.result.IDriverResultHandler;
import com.stratio.crossdata.common.result.MetadataResult;
import com.stratio.crossdata.common.result.QueryResult;
import com.stratio.crossdata.common.result.Result;
import com.stratio.crossdata.driver.BasicDriver;
import com.stratio.crossdata.driver.DriverConnection;
import com.stratio.crossdata.driver.querybuilder.Query;

public class Driver {

	String user;
	DriverConnection crossdataConnection;
	Result queryResult;
	BasicDriver metaDriver;
	String catalog;
	ArrayList<Result> asyncResults = new ArrayList<Result>();;

	public void connect(String user2) throws ConnectionException {
		this.user = "TEST_USER";
		metaDriver = new BasicDriver();
		crossdataConnection = metaDriver.connect(this.user, "");
		catalog = "";
	}

	public String getUser() {
		return user;
	}

	public void setAsyncResults(Result result) {
		asyncResults.add(result);
	}

	public ArrayList<Result> getAsyncResults() {
		return asyncResults;
	}

	public void clearAsyncResults() {
		asyncResults.clear();
	}

	public void disconnect() throws ConnectionException {
		metaDriver.disconnect();
	}

	public void close() {
		metaDriver.close();
	}

	public Result addConnector(String path) throws FileNotFoundException,
			JAXBException, ExistNameException, ApiException {
		return crossdataConnection.addManifest(
				CrossdataManifest.TYPE_CONNECTOR, path);
	}

	public Result addDatastore(String path) throws FileNotFoundException,
			JAXBException, ExistNameException, ApiException {

		return crossdataConnection.addManifest(
				CrossdataManifest.TYPE_DATASTORE, path);
	}

	public CommandResult resetMetadata() {
		return (CommandResult) crossdataConnection.resetServerdata();
	}

	public CommandResult cleanMetadata() {
		return (CommandResult) crossdataConnection.cleanMetadata();
	}

	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}

	public String getCatalog() {
		return catalog;
	}

	public void executeXqlScript(String file) throws Exception {
		URL url_file = Driver.class.getResource("/scripts/" + file);
		File archivo = null;
		FileReader fr = null;
		BufferedReader br = null;
		try {
			archivo = new File(url_file.getPath());
			fr = new FileReader(archivo);
			br = new BufferedReader(fr);
			String linea;
			while ((linea = br.readLine()) != null) {
			//	System.out.println(linea);
				if ((linea.length() != 0) || (!linea.equals(""))) {
					executeQuery(linea);
				}
			}
		} catch (Exception e) {
			throw new Exception(e.toString());
		} finally {
			try {
				if (null != fr) {
					fr.close();
				}
			} catch (Exception e2) {
				throw new Exception(e2.toString());
			}
		}
	}

	public Result getDatastore(String datastore) {
		DataStoreName datastoreNamne = new DataStoreName(datastore);
		return crossdataConnection.describeDatastore(datastoreNamne);
	}

	public Result getDatastores() {
		return crossdataConnection.describeDatastores();
	}

	public Result getCluster(String cluster) {
		ClusterName clusterName = new ClusterName(cluster);
		return crossdataConnection.describeCluster(clusterName);
	}

	public Result getClusters() {
		return crossdataConnection.describeClusters();
	}

	public Result getConnector(String connector) {
		ConnectorName c_name = new ConnectorName(connector);
		return crossdataConnection.describeConnector(c_name);
	}

	public Result getConnectors() {
		return crossdataConnection.describeConnectors();
	}

	public List<String> getCatalogsAsList() {
		return ((MetadataResult) crossdataConnection.listCatalogs())
				.getCatalogList();
	}

	public Result dropDatastore(String datastore_name)
			throws NotExistNameException, ApiException {
		return crossdataConnection.dropManifest(
				CrossdataManifest.TYPE_DATASTORE, datastore_name);
	}

	public Result dropConnector(String connector_name)
			throws NotExistNameException, ApiException {
		return crossdataConnection.dropManifest(
				CrossdataManifest.TYPE_CONNECTOR, connector_name);
	}

	public MetadataResult getCatalogsAsMetaDataResult() {
		return (MetadataResult) crossdataConnection.listCatalogs();
	}

	public boolean existsCatalog(String catalog_name) {
		return getCatalogsAsList().contains(catalog_name);
	}

	public int getNumberOfRows(String catalogName, String tableName) {
		try {
			executeQuery("SELECT * FROM " + catalogName + "." + tableName + ";");
		} catch (ConnectionException | ParsingException | ExecutionException
				| UnsupportedException | ValidationException e) {
			e.printStackTrace();
		}
		QueryResult res = (QueryResult) getResult();
		return res.getResultSet().getRows().size();

	}

	public HashMap<String, ColumnType> getColumnsofTable(String catalog,
			String table) {
		HashMap<String, ColumnType> columns = new HashMap<String, ColumnType>();
		List<com.stratio.crossdata.common.metadata.TableMetadata> tabs = crossdataConnection
				.listTables(catalog).getTableList();
		for (int i = 0; i < tabs.size(); i++) {
			com.stratio.crossdata.common.metadata.TableMetadata tab = tabs
					.get(i);
			if (tab.getName().getName().equals(table)) {
				Map<ColumnName, com.stratio.crossdata.common.metadata.ColumnMetadata> aux = tab
						.getColumns();
				for (Entry<ColumnName, com.stratio.crossdata.common.metadata.ColumnMetadata> entry : aux
						.entrySet()) {
					ColumnName key = entry.getKey();
					com.stratio.crossdata.common.metadata.ColumnMetadata value = entry
							.getValue();
					columns.put(key.getName(), value.getColumnType());
				}
				System.out.println("Devuelve Columns");
				return columns;
			}
		}
		return null;
	}

	public boolean existsColumn(String catalog, String table, String column,
			String columnType) {
		HashMap<String, ColumnType> columns = getColumnsofTable(catalog, table);
		if (columns != null) {
			if (columns.containsKey(column)) {
				ColumnType type = columns.get(column);
				System.out.println("TIPO: "
						+ type.getJavaType().getSimpleName().toString());
				if (type.getJavaType().getSimpleName().toString()
						.equals(columnType)) {
					return true;
				}
			}
		}
		return false;
	}

	public List<String> getTablesOfACatalogAsList(String catalogName) {
		List<String> res = new ArrayList<String>();
		List<com.stratio.crossdata.common.metadata.TableMetadata> tabs = crossdataConnection
				.listTables(catalogName).getTableList();
		for (int i = 0; i < tabs.size(); i++) {
			res.add(tabs.get(i).getName().getName());
		}
		return res;
	}

	public boolean existsTable(String catalogName, String tableName) {
		return getTablesOfACatalogAsList(catalogName).contains(tableName);
	}

	public com.stratio.crossdata.common.metadata.TableMetadata getTable(
			String catalogName, String tableName) throws Exception {

		List<com.stratio.crossdata.common.metadata.TableMetadata> tabs = crossdataConnection
				.listTables(catalogName).getTableList();
		for (int i = 0; i < tabs.size(); i++) {
			if (tabs.get(i).getName().getName().equals(tableName)) {
				return tabs.get(i);
			}
		}
		throw new Exception("The table " + tableName
				+ " does not exists on the catalog " + catalogName);
	}

	public void executeQuery(String query, String catalog_name)
			throws UnsupportedException, ParsingException, ValidationException,
			ExecutionException, ConnectionException {
		queryResult = null;
		// queryResult = metaDriver.executeQuery(catalog_name, query);
		queryResult = crossdataConnection.executeQuery(query);
	}

	public void execueQueryBuilder(Query query) throws ConnectionException,
			ParsingException, ExecutionException, UnsupportedException,
			ValidationException {
		queryResult = crossdataConnection.executeQuery(query);
	}

	public void executeQuery(Map<?, ?> feed) throws ParsingException,
			ValidationException, ExecutionException, UnsupportedException,
			ConnectionException {
		queryResult = null;
		queryResult = crossdataConnection.executeQuery(feed.get("query")
				.toString());
		// queryResult = metaDriver.executeQuery(keyspace, query);
	}

	public void executeQuery(String query) throws ParsingException,
			ValidationException, ExecutionException, UnsupportedException,
			ConnectionException {
	//	System.out.println("QUERY " + this.user + " " + query);
		queryResult = null;
		queryResult = crossdataConnection.executeQuery(query);
	//	System.out.println("RESULTS OF QUERY " + query);
		// queryResult = metaDriver.executeQuery(keyspace, query);
	}

	public Result getResult() {
		return queryResult;
	}

	public void describeConnector(String connectorName) {
		queryResult = crossdataConnection.describeConnector(new ConnectorName(
				connectorName));
	}

	public void describeConnectors() {
		queryResult = crossdataConnection.describeConnectors();
	}

	public void describeCluster(String clusterName) {
		queryResult = crossdataConnection.describeCluster(new ClusterName(
				clusterName));
	}

	public void describeClusters() {
		queryResult = crossdataConnection.describeClusters();
	}

	public void describeDatastore(String DatastoreName) {
		queryResult = crossdataConnection.describeDatastore(new DataStoreName(
				DatastoreName));
	}

	public void describeDatastores() {
		queryResult = crossdataConnection.describeDatastores();
	}

	public void executeAsyng(String query,
			DriverResultHandler driverResultHandler) {
		crossdataConnection.executeAsyncRawQuery(query, driverResultHandler);
	}
	
	public void addManifest(CrossdataManifest manifest) throws ManifestException{
		crossdataConnection.addManifest(manifest);
	}
}
