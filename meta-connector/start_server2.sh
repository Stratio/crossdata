#!/usr/bin/env bash

mvn -pl . exec:java -Dexec.mainClass=com.stratio.connector.ConnectorApp -Dexec.args="2552"
#sbt "run-main com.stratio.connector.ConnectorApp 2552"
