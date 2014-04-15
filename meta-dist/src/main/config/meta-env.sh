#!/bin/bash
PRG="$0"
while [ -h "$PRG" ]; do
  ls=`ls -ld "$PRG"`
  link=`expr "$ls" : '.*-> \(.*\)$'`
  if expr "$link" : '/.*' > /dev/null; then
    PRG="$link"
  else
    PRG=`dirname "$PRG"`/"$link"
  fi
done

PRGDIR=`dirname "$PRG"`
BASEDIR=`cd "$PRGDIR/.." >/dev/null; pwd`



# META_CONF is global enviroment variable
# JAVA_OPTS
# JSVCCMD
# JAVA_HOME
# META_HOME
META_HOME="$BASEDIR"
# META_LIB
META_LIB="$META_HOME/lib"
# META_BIN
META_BIN="$META_HOME/bin"
# META_PID
# META_LOG_OUT
META_LOG_OUT="$META_HOME/meta-server.out"
# META_LOG_ERROR
META_LOG_ERR="$META_HOME/meta-server.err"
# META_SERVER_USER
META_SERVER_USER="root"

echo "META_HOME = $META_HOME"
echo "META_CONF = $META_CONF"
echo "META_LIB  = $META_LIB"
echo "META_BIN  = $META_BIN"
echo "META_PID  = $META_PID"
echo "META_LOG_OUT  = $META_LOG_OUT"
echo "META_LOG_ERR  = $META_LOG_ERR"
echo "META_SERVER_USER  = META_SERVER_USER"

export META_HOME
export META_LIB
export META_BIN
export META_PID
export META_LOG_OUT
export META_LOG_ERR
export META_SERVER_USER

