package com.stratio.tests.utils;

import org.apache.spark.SparkConf;
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
        }

        public XDDataFrame getXDDataFrame(){
            return df;
        }

        public void showDataframe(){
            df.show();
        }

}
