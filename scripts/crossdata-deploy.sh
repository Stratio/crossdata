#!/usr/bin/env bash

if [ "$#" -lt 1 ]; then
    echo "Usage: crossdata-deploy.sh /path/to/confFolder/<configFile>.conf"
    exit 1
fi

configFile="$1"

echo "Configuration File: $1"

COMMAND="bin/spark-submit"

function process_line {
    #echo "Args: $#"
    if [ "$#" -lt 2 ]
    then
        echo "ERROR: Bad line format"
        exit 1
    else
        KEY=$1
        echo "KEY: $KEY"
        VALUE="${2%\"}"
        VALUE="${VALUE#\"}"
        echo "VALUE: $VALUE"
        if [[ $KEY == spark.path ]]
        then
            COMMAND="$VALUE/$COMMAND"
        fi
        if [[ $KEY == spark.mainClass ]]
        then
            COMMAND="$COMMAND --class $VALUE"
        fi
        if [[ $KEY == spark.master ]]
        then
            COMMAND="$COMMAND --master $VALUE"
        fi
        if [[ $KEY == server.name ]]
        then
            COMMAND="$COMMAND --name $VALUE"
        fi
        if [[ $KEY == spark.proxyUser ]]
        then
            COMMAND="$COMMAND --proxy-user $VALUE"
        fi
        if [[ $KEY == spark.packages ]]
        then
            COMMAND="$COMMAND --packages $VALUE"
        fi
        if [[ $KEY == spark.driverMemory ]]
        then
            COMMAND="$COMMAND --driver-memory $VALUE"
        fi
        if [[ $KEY == spark.executorMemory ]]
        then
            COMMAND="$COMMAND --executor-memory $VALUE"
        fi
        if [[ $KEY == spark.totalCores ]]
        then
            COMMAND="$COMMAND --total-executor-cores $VALUE"
        fi
        if [[ $KEY == spark.executorCores ]]
        then
            COMMAND="$COMMAND --executor-cores $VALUE"
        fi
        if [[ $KEY == yarn.numExecutors ]]
        then
            COMMAND="$COMMAND --num-executors $VALUE"
        fi
        if [[ $KEY == server.config ]]
        then
            COMMAND="$COMMAND --files $VALUE"
        fi
        if [[ $KEY == spark.appJar ]]
        then
            COMMAND="$COMMAND --jars $VALUE"
            COMMAND="$COMMAND $VALUE"
        fi
        if [[ $KEY == spark.args ]]
        then
            COMMAND="$COMMAND $VALUE"
        fi
    fi
}

while read line
do
    if [[ $line == crossdata-deploy* ]] ;
    then
        #echo "CONFIG: $line"
        tmp1=${line/crossdata-deploy./}
        tmp1="${tmp1/=/}"
        #echo "tmp1 = $tmp1"
        process_line $tmp1
    fi
done < $1

echo "Executing: $COMMAND"

$COMMAND

