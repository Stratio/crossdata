@Library('libpipelines@feature/multibranch') _

hose {
    MAIL = 'crossdata'
    SLACKTEAM = 'stratiocrossdata'
    MODULE = 'crossdata'
    REPOSITORY = 'crossdata'
    DEVTIMEOUT = 65
    RELEASETIMEOUT = 50
    MAXITRETRIES = 2
    EXPOSED_PORTS = [13420,13422]

    PKGMODULES = ['dist']
    PKGMODULESNAMES = ['stratio-crossdata-scala211']
    DEBARCH = 'all'
    RPMARCH = 'noarch'

    ITSERVICES = [
        ['ZOOKEEPER': [
            'image': 'jplock/zookeeper:3.5.2-alpha'],
            'sleep': 30,
            'healthcheck': 2181
            ],
        ['MONGODB':[
            'image': 'stratio/mongo:3.0.4',
            'sleep': 30,
            'healthcheck': 27017]],
        ['ELASTICSEARCH':[
            'image': 'elasticsearch:2.0.2',
            'sleep': 30,
            'healthcheck': 9300,
            'env': ['ES_JAVA_OPTS="-Des.cluster.name=%%JUID -Des.network.host=%%OWNHOSTNAME"']]],
        ['CASSANDRA':[
            'image':'stratio/cassandra-lucene-index:2.2.5.3',
            'sleep': 30,
            'healthcheck': 9042,
            'env':['MAX_HEAP=256M']]],
        ['HADOOP':[
            'image':'stratio/hadoop:2.7.2',
            'sleep': 30,
            'healthcheck': 8020,
            'env': ['MASTER=true',
                    'FUNC_MODE=hdfs']]],
        ['KAFKA':[
            'image': 'stratio/kafka:0.8.2.1',
            'sleep': 30,
            'healthcheck': 9092,
            'env': ['ZOOKEEPER_HOSTS=%%ZOOKEEPER:2181']]]
    ]

    ITPARAMETERS = """
        |    -Dcassandra.hosts.0=%%CASSANDRA
        |    -Dmongo.hosts.0=%%MONGODB
        |    -Delasticsearch.hosts.0=%%ELASTICSEARCH
        |    -Delasticsearch.cluster=%%JUID
        |    -Dhdfs.namenode=hdfs://%%HADOOP:8020
        |    -Dhdfs.user="Stratio"
        |    -Djars.externalJars="/root"
        |    -Dcatalog.zookeeper.connectionString=%%ZOOKEEPER:2181
        |    -Dstreaming.catalog.zookeeper.connectionString=%%ZOOKEEPER:2181
        |   -Dstreaming.receiver.kafka.connection=%%KAFKA:9092
        |    -Dstreaming.receiver.zookeeper.connectionString=%%ZOOKEEPER:2181
        |    -Dlauncher.sparkHome=/opt/sds/spark/
        | """

    ATSERVICES = [
        ['MONGODB':[
            'image': 'stratio/mongo:3.0.4',
            'sleep': 30,
            'healthcheck': 27017]],
        ['ELASTICSEARCH':[
            'image': 'elasticsearch:2.0.2',
            'sleep': 30,
            'healthcheck': 9300,
            'env': ['ES_JAVA_OPTS="-Des.cluster.name=%%JUID -Des.network.host=%%OWNHOSTNAME"']]],
        ['CASSANDRA':[
            'image':'stratio/cassandra-lucene-index:2.2.5.3',
            'sleep': 30,
            'healthcheck': 9042,
            'env':['MAX_HEAP=256M']]],
        ['CROSSDATA':[
            'image': 'stratio/crossdata-scala211:%%VERSION',
            'sleep': 30,
            'healthcheck': 13422
            ]]
    ]

    ATPARAMETERS = """
        |    -DCROSSDATA_HOST=%%CROSSDATA:13420
        |    -DCASSANDRA_HOST=%%CASSANDRA
        |    -DCASSANDRA_CLUSTER="Stratio cluster"
        |    -DMONGO_HOST=%%MONGODB
        |    -DMONGO_PORT=27017
        |    -DES_NODE=%%ELASTICSEARCH
        |    -DES_PORT=9200
        |    -DES_NATIVE_PORT=9300
        |    -DES_CLUSTER=%%JUID
        | """

    DEV = { config ->
        doCompile(conf: config, crossbuild: 'scala-2.11')

        parallel(UT: {
            doUT(conf: config, crossbuild: 'scala-2.11')
        }, IT: {
            doIT(conf: config, crossbuild: 'scala-2.11')
        }, failFast: config.FAILFAST)

        doPackage(conf: config, crossbuild: 'scala-2.11')

        parallel(DOC: {
           doDoc(conf: config, crossbuild: 'scala-2.11')
         },QC: {
            doStaticAnalysis(conf: config, crossbuild: 'scala-2.11')
        }, DEPLOY: {
            doDeploy(conf: config, crossbuild: 'scala-2.11')
        }, DOCKER: {
            doDocker(conf: config, crossbuild: 'scala-2.11')
        }, failFast: config.FAILFAST)

        doAT(conf: config, groups: ['micro-cassandra'])
     }
}
