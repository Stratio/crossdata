CROSSDATA AS A SPARK PACKAGE
=============================
If you want to use crossdata as a Spark Package into your Spark distribution, just follow these steps:

    > mvn clean install -DskipITs -DskipUTs
    > mvn package -Ppackage -DskipITs -DskipUTs

Once the package phase is done, you can find the Spark-Crossdata-1.2.1.jar at the spark-crossdata/target directory.

Now, you can start your spark-shell as:

    SPARK-HOME> bin/spark-shell --jars $CROSSDATA-HOME/spark-crossdata/target/Spark-Crossdata-1.2.1.jar
    

Inside the spark shell, you can import our crossdata datasources:

    scala> import com.stratio.crossdata.connectors._
    
You can create a Crossdata context (XDContext) as follows:

    scala> import org.apache.spark.sql.crossdata._
    scala> val xdcontext=new XDContext(sc)
    
Next, you can import data from the Cassandra source:

    scala> xdcontext.sql("IMPORT TABLES USING com.stratio.crossdata.connector.cassandra OPTIONS ( cluster \"Test Cluster\", spark_cassandra_connection_host '127.0.0.1')")

And then check if the Cassandra tables are in Crossdata Catalog:

    scala> xdcontext.sql("SHOW TABLES").show(false)
    
Finally you can execute your queries:

    scala> xdContext.sql("SELECT....")
    
    

