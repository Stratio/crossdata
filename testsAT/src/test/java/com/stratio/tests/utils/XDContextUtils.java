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
package com.stratio.tests.utils;

import org.apache.spark.SparkConf;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.crossdata.ExecutionType;
import org.apache.spark.sql.crossdata.XDContext;
import org.apache.spark.api.java.*;
import org.apache.spark.sql.crossdata.XDDataFrame;

/**
 * Created by hdominguez on 13/10/15.
 */
public class XDContextUtils {

        private String appName;
        private String master;
        private SparkConf sparkConf;
        private XDContext xdContext;
        private JavaSparkContext sparkContext;
        private XDDataFrame df;
        private DataFrame df_spark;
        public XDContextUtils(){
            this.appName = System.getProperty("SPARK_APP_NAME", "appTests");
            this.master = System.getProperty("SPARK_MASTER", "local[4]");
        }

        public void setSparkConf(){
            sparkConf = new SparkConf().setAppName(this.appName).setMaster(this.master);
        }

        public SparkConf getSparkConf(){
            return sparkConf;
        }

        public void setSparkContext(){
             sparkContext = new JavaSparkContext(sparkConf);
        }

        public JavaSparkContext getSparkContext(){
            return sparkContext;
        }

        public void closeSparkContext(){
            sparkContext.stop();
        }

        public void setXDContext(){
            xdContext = new XDContext(sparkContext.sc());
        }

        public XDContext getXDContext(){
            return xdContext;
        }

        public void executeQuery(String query){
            df = (XDDataFrame)xdContext.sql(query);
            if(query.equals("SHOW TABLES")){
                System.out.print("a");
                df.show();
            }

        }

        public void dropTables(){
            xdContext.dropAllTables();
        }

        public XDDataFrame getXDDataFrame(){
            return df;
        }

        public void showDataframe(){
            df.show();
        }


}
