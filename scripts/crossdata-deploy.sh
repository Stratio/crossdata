#!/usr/bin/env bash

if [ "$#" -lt 1 ]; then
    echo "Usage: crossdata-deploy.sh /path/to/confFolder/<configFile>.conf"
    exit 1
fi

configFile="$1"

echo "Configuration File: $1"

sparkVersion="spark-1.5.2-bin-hadoop2.6"

CWD=$(pwd)
echo "cwd: $CWD"
CWD="${CWD%scripts}"
echo "cwd: $CWD"

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
            VALUE=$(cd $(dirname "$VALUE") && pwd -P)/$(basename "$VALUE")
            COMMAND="$VALUE/$COMMAND"
            spark_path_found=true
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
            VALUE=$(cd $(dirname "$VALUE") && pwd -P)/$(basename "$VALUE")
            COMMAND="$COMMAND --files $VALUE"
        fi
        if [[ $KEY == spark.appJar ]]
        then
            VALUE=$(cd $(dirname "$VALUE") && pwd -P)/$(basename "$VALUE")
            COMMAND="$COMMAND --jars $VALUE"
            COMMAND="$COMMAND $VALUE"
        fi
        if [[ $KEY == spark.args ]]
        then
            COMMAND="$COMMAND $VALUE"
        fi
    fi
}

spark_path_found=false

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

echo "$spark_path_found"

if [ $spark_path_found == false ]
then
    #echo "Downloading Spark..."
    wget http://d3kbcqa49mib13.cloudfront.net/$sparkVersion.tgz
    tar -zxvf $sparkVersion.tgz
    VALUE=$(pwd)
    VALUE="$VALUE/$sparkVersion"
    COMMAND="$VALUE/$COMMAND"
fi

echo "Executing: $COMMAND"

$COMMAND

