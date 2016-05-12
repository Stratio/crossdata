#Zeppelin Crossdata interpreter

To know more about Zeppelin, visit our web site [http://zeppelin.incubator.apache.org](http://zeppelin.incubator.apache.org)

## Requirements
 * Java 1.7
 * Apache Zeppelin 0.6.0-incubating-SNAPSHOT
 * Maven 

## Getting Started

# Install Zeppelin

https://github.com/apache/incubator-zeppelin => master

git checkout 4c269e6d860320e2612eb6b77785c6d1ff3ef106

mvn clean install -DskipTests

#### Crossdata Interpreter

From /path/to/crossdata/zeppelin:

mvn clean install

scripts/add-crossdata-interpreter.sh /path/to/zeppelin_home 

### Configure

TODO
