#!/bin/bash
DIR=$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )

# Get options
while getopts "p:l:" option; do
  case $option in
     p)      PIDFILE=$OPTARG ;;
     l)      LOGFILE=$OPTARG ;;
     *)      echo "Unknown option" ; exit 1 ;;
  esac
done

# Set defatult values
LOGFILE=${LOGFILE:-"/var/log/sds/crossdata/crossdata-shell.log"}
PIDFILE=${PIDFILE:-"/var/run/sds/crossdata-shell.pid"}

$DIR/shell.sh & echo $! >$PIDFILE