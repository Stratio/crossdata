/**
 * Copyright (C) 2015 Stratio (http://stratio.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
        """.format("1.1.0", SPARK_VERSION))

val xdContext = new XDContext(sc)
