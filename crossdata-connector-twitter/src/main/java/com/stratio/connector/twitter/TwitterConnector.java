package com.stratio.connector.twitter;

import static com.codahale.metrics.MetricRegistry.name;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.codahale.metrics.Timer;
import com.stratio.crossdata.common.connector.AbstractExtendedConnector;
import com.stratio.crossdata.common.connector.ConnectorClusterConfig;
import com.stratio.crossdata.common.connector.IConfiguration;
import com.stratio.crossdata.common.connector.IConnectorApp;
import com.stratio.crossdata.common.connector.IMetadataEngine;
import com.stratio.crossdata.common.connector.IQueryEngine;

import com.stratio.crossdata.common.connector.IStorageEngine;
import com.stratio.crossdata.common.data.ClusterName;
import com.stratio.crossdata.common.exceptions.ConnectionException;
import com.stratio.crossdata.common.exceptions.ExecutionException;
import com.stratio.crossdata.common.exceptions.InitializationException;
import com.stratio.crossdata.common.exceptions.UnsupportedException;
import com.stratio.crossdata.common.security.ICredentials;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterConnector extends AbstractExtendedConnector {

    /**
     * Class logger.
     */
    private static final Logger LOG = Logger.getLogger(TwitterConnector.class);

    private final Timer connectTimer;

    //https://apps.twitter.com/app/8096283/show
    private static final String CONSUMER_KEY = "consumer_key";
    private static final String CONSUMER_SECRET = "consumer_secret";
    private static final String ACCESS_TOKEN = "access_token";
    private static final String ACCESS_TOKEN_SECRET = "access_token_secret";

    private final Map<ClusterName, Twitter> sessions = new HashMap<>();

    /**
     * Class constructor.
     *
     * @param connectorApp parent connector app.
     */
    public TwitterConnector(IConnectorApp connectorApp) {
        super(connectorApp);
        connectTimer = new Timer();
        String timerName = name(TwitterConnector.class, "connect");
        registerMetric(timerName, connectTimer);
    }

    /**
     * Get the name of the connector.
     *
     * @return A name.
     */
    @Override
    public String getConnectorName() {
        return "TwitterConnector";
    }

    /**
     * Get the names of the datastores supported by the connector.
     * Several connectors may declare the same datastore name.
     *
     * @return The names.
     */
    @Override
    public String[] getDatastoreName() {
        return new String[]{"TwitterDatastore"};
    }

    /**
     * Initialize the connector service.
     *
     * @param configuration The configuration.
     * @throws com.stratio.crossdata.common.exceptions.InitializationException If the connector initialization fails.
     */
    @Override
    public void init(IConfiguration configuration) throws InitializationException {
        LOG.info("TwitterConnector launched");
    }

    /**
     * Connect to a datastore using a set of options.
     *
     * @param credentials The required credentials
     * @param config      The cluster configuration.
     * @throws com.stratio.crossdata.common.exceptions.ConnectionException If the connection could not be established.
     */
    @Override
    public void connect(ICredentials credentials, ConnectorClusterConfig config) throws ConnectionException {
        //Init Metric
        Timer.Context connectTimerContext = connectTimer.time();

        // Connection
        ClusterName targetCluster = config.getName();
        Map<String, String> options = config.getClusterOptions();
        LOG.info("clusterOptions: " + config.getClusterOptions().toString() + " connectorOptions: " + config.getConnectorOptions());
        if((!options.isEmpty()) && (options.get(CONSUMER_KEY) != null) && (options.get(CONSUMER_SECRET) != null)
                    && (options.get(ACCESS_TOKEN) != null) && (options.get(ACCESS_TOKEN_SECRET) != null)){
            ConfigurationBuilder cb = new ConfigurationBuilder();
            cb.setDebugEnabled(true)
                    .setOAuthConsumerKey(options.get(CONSUMER_KEY))
                    .setOAuthConsumerSecret(options.get(CONSUMER_KEY))
                    .setOAuthAccessToken(options.get(ACCESS_TOKEN))
                    .setOAuthAccessTokenSecret(options.get(ACCESS_TOKEN_SECRET));
            TwitterFactory tf = new TwitterFactory(cb.build());
            Twitter twitter = tf.getInstance();
            sessions.put(targetCluster, twitter);
        } else {
            long millis = connectTimerContext.stop();
            LOG.info("Connection took " + millis + " nanoseconds");
            throw new ConnectionException("Invalid options, cannot login twitter");
        }

        //End Metric
        long millis = connectTimerContext.stop();
        LOG.info("Connection took " + millis + " nanoseconds");
    }

    /**
     * Close the connection with the underlying cluster.
     *
     * @param name The Cluster name.
     * @throws com.stratio.crossdata.common.exceptions.ConnectionException If the close operation cannot be performed.
     */
    @Override
    public void close(ClusterName name) throws ConnectionException {
        //TODO: close session with Twitter
    }

    /**
     * Shuts down and then close all cluster's connections.
     *
     * @throws com.stratio.crossdata.common.exceptions.ExecutionException If the shutdown operation cannot be performed.
     */
    @Override
    public void shutdown() throws ExecutionException {
        LOG.info("Shutting down TwitterConnector");
    }

    /**
     * Retrieve the connectivity status with the datastore.
     *
     * @param name The Cluster name.
     * @return Whether it is connected or not.
     */
    @Override
    public boolean isConnected(ClusterName name) {
        return sessions.containsKey(name);
    }

    /**
     * Get the storage engine.
     *
     * @return An implementation of {@link com.stratio.crossdata.common.connector.IStorageEngine}.
     * @throws com.stratio.crossdata.common.exceptions.UnsupportedException If the connector does not provide this functionality.
     */
    @Override
    public IStorageEngine getStorageEngine() throws UnsupportedException {
        return null;
    }

    /**
     * Get the query engine.
     *
     * @return An implementation of {@link com.stratio.crossdata.common.connector.IQueryEngine}.
     * @throws com.stratio.crossdata.common.exceptions.UnsupportedException If the connector does not provide this functionality.
     */
    @Override
    public IQueryEngine getQueryEngine() throws UnsupportedException {
        return null;
    }

    /**
     * Get the metadata engine.
     *
     * @return An implementation of {@link com.stratio.crossdata.common.connector.IMetadataEngine}.
     * @throws com.stratio.crossdata.common.exceptions.UnsupportedException If the connector does not provide this functionality.
     */
    @Override
    public IMetadataEngine getMetadataEngine() throws UnsupportedException {
        return null;
    }


}
