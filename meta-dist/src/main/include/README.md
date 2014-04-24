# Stratio Meta #

## Project description ##

Stratio META brings batch and streaming queries into a single language. META provides a distributed fault-tolerant server architecture that has the unique ability to execute queries in Cassandra, Spark, and/or Stratio Streaming.



## Start/stop/restart the Server as a daemon ##
[jsvc](http://commons.apache.org/proper/commons-daemon/jsvc.html) needs to be installed 

```
  > ./meta-server-daemon start
  > ./meta-server-daemon stop
  > ./meta-server-daemon restart
```


### Alternative manual server start (runs in foreground) ###

```
  > ./meta-server
```

## Start the Meta shell ##

```
  > ./meta-sh
```

## Directory structure ##

Describe the distribution directories: bin, conf, and lib
  * bin : Contains the aforementioned scripts to start/stop the server or shell
  * conf : 
    * driver-application.conf  
    * driver-log.properties  
    * meta-env.sh  
    * server-application.conf  
    * server-log.properties
    * driver-application.conf  
    * driver-log.properties  
    * meta-env.sh  
    * server-application.conf  
    * server-log.properties
  * lib : Contains the necesary jar files to run the scripts in ***bin***

# License #

Stratio Meta is licensed as [LGPL](https://www.gnu.org/licenses/gpl-howto.html)

Copyright (c) 2014, Stratio, All rights reserved.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 3.0 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library.
