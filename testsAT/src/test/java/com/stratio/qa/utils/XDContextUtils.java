/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
 */
package com.stratio.qa.utils;

import java.util.HashMap;

import scala.Predef;
import scala.Tuple2;
import scala.collection.JavaConverters;
import scala.collection.immutable.Map;
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
            this.master = "local[4]";
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

        public void executeQuery(String query)
        {
                df = (XDDataFrame)xdContext.sql(query);
        }

        public void importTables(String datasource, HashMap<String,String> options){
          xdContext.importTables(datasource, JavaConverters.mapAsScalaMapConverter(options).asScala().toMap(Predef
                           .<Tuple2<String, String>>conforms()
           ));
        }

        public void dropTables(){
           xdContext.dropAllTables();
        }

        public XDDataFrame getXDDataFrame(){
            return df;
        }

        public void showDataframe(){
            df.show(false);
        }

        public void clearXDF(){
            df = null;
        }
}
