#!/usr/bin/env bash

command="mvn -pl . exec:java -Dexec.mainClass=com.stratio.connector.ConnectorApp -Dexec.args=\"$@\""
eval $command

#sbt "run-main com.stratio.connector.ConnectorApp 2551"
