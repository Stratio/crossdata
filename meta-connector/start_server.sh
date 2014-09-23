#!/usr/bin/env bash

command="mvn -pl . exec:java -Dexec.mainClass=com.stratio.connectors.ConnectorApp -Dexec.args=\"$@\""
echo $command
eval $command

#sbt "run-main com.stratio.connector.ConnectorApp 2551"
