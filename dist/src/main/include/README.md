# Stratio Meta #

## Project description ##

Stratio META brings batch and streaming queries into a single language. META provides a distributed fault-tolerant server architecture that has the unique ability to execute queries in Cassandra, Spark, and/or Stratio Streaming.



## Start/stop/restart the Server as a daemon ##
[jsvc](http://commons.apache.org/proper/commons-daemon/jsvc.html) needs to be installed 

```
  > ./com.stratio.crossdata-server-daemon start
  > ./com.stratio.crossdata-server-daemon stop
  > ./com.stratio.crossdata-server-daemon restart
```


### Alternative manual server start (runs in foreground) ###

```
  > ./com.stratio.crossdata-server
```

## Start the Meta shell ##

```
  > ./com.stratio.crossdata-sh
```

## Directory structure ##

  * bin : Contains the aforementioned scripts to start/stop the server or shell
  * conf : 
    * driver-application.conf : driver config file
    * driver-log.properties : config for the driver logs
    * com.stratio.crossdata-env.sh : will be executed by com.stratio.crossdata-sh,com.stratio.crossdata-server and com.stratio.crossdata-server-daemon to get the necesary environment variables (such as, for example, $META_HOME) for their correct execution
    * server-application.conf : server config file
    * server-log.properties : config for the server logs
  * lib : Contains the necesary jar files to run the scripts in ***bin***


# License #

Stratio Meta is licensed as [Apache2](http://www.apache.org/licenses/LICENSE-2.0.txt)

Licensed to STRATIO (C) under one or more contributor license agreements.  
See the NOTICE file distributed with this work for additional information 
regarding copyright ownership.  The STRATIO (C) licenses this file
to you under the Apache License, Version 2.0 (the
"License"); you may not use this file except in compliance
with the License.  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
KIND, either express or implied.  See the License for the
specific language governing permissions and limitations
under the License.
