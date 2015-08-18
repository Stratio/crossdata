//Commmons
import org.apache.spark.sql.crossdata.XDContext
import org.apache.spark.{SparkContext, SparkConf}
import org.apache.spark.SPARK_VERSION
//Scala
import scala.collection.JavaConversions._


println("""Welcome to Stratio

   __________  ____  __________ ____  ___  _________ 
  / ____/ __ \/ __ \/ ___/ ___// __ \/   |/_  __/   |
 / /   / /_/ / / / /\__ \\__ \/ / / / /| | / / / /| |
/ /___/ _, _/ /_/ /___/ /__/ / /_/ / ___ |/ / / ___ |
\____/_/ |_|\____//____/____/_____/_/  |_/_/ /_/  |_| version %s

 Powered By Apache:                                                    
      ____              __
     / __/__  ___ _____/ /__
    _\ \/ _ \/ _ `/ __/  '_/
   /___/ .__/\_,_/_/ /_/\_\   version %s
      /_/
        """.format("1.0.0", SPARK_VERSION))

val xdContext = new XDContext(sc)
