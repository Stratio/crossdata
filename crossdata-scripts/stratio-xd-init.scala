//Commmons
import org.apache.spark.sql.crossdata.XDContext
import org.apache.spark.{SparkContext, SparkConf}

//Scala
import scala.collection.JavaConversions._

val xdContext = new XDContext(sc)