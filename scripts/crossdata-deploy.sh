#!/usr/bin/env bash

if [ "$#" -lt 1 ]; then
    echo "Usage: crossdata-deploy.sh /path/to/conf/server-application.conf"
    exit 1
fi

configFile="$1"

echo "Configuration File: $1"

function process_line {
    echo "Args: $#"
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
    fi
}

while read line
do
    if [[ $line == crossdata-server* ]] ;
    then
        echo "CONFIG: $line"
        tmp1=${line/crossdata-server./}
        tmp1="${tmp1/=/}"
        echo "tmp1 = $tmp1"
        process_line $tmp1
    fi

done < $1

echo "VALUE=$VALUE"

"$VALUE/bin/spark-shell"


