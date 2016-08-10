#Zeppelin Crossdata interpreter

To know more about Zeppelin, visit our web site [http://zeppelin.incubator.apache.org](http://zeppelin.incubator.apache.org)

## Requirements
 * Java 1.7
 * Apache Zeppelin 0.6.0
 * Maven 

## Getting Started

# Install Zeppelin

https://github.com/apache/incubator-zeppelin => git checkout v0.6.0

mvn clean package -DskipTests

#### Crossdata Interpreter

/path/to/zeppelin/bin/install-interpreter.sh --name crossdata -t org.apache.zeppelin:zeppelin-crossdata_2.11:1.6.0-SNAPSHOT

Add interpreter class name ("org.apache.zeppelin.crossdata.CrossdataInterpreter") to 'zeppelin.interpreters' property in your conf/zeppelin-site.xml file

Start Zeppelin

Create interpreter setting in 'Interpreter' menu on GUI. And then you can bind interpreter on your notebook


### Configure

TODO
