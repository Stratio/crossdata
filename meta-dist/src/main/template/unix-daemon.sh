#!/bin/sh
@LICENSE_HEADER@

# resolve links - $0 may be a softlink
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


if [ -z "$META_CONF" ]; then
    META_CONF="$BASEDIR/conf"
fi

if [ -f "$META_CONF/meta-env.sh" ]; then
    source "$META_CONF/meta-env.sh"
fi



# Reset the REPO variable. If you need to influence this use the environment setup file.
REPO=
@ENV_SETUP@

#JAVA_HOME validation
if [ -z "$JAVA_HOME" ] ; then
    echo "Error: JAVA_HOME is not defined correctly." 1>&2
    echo "  Current JAVA_HOME value: $JAVA_HOME" 1>&2
    exit 1
fi

# OS specific support.  $var _must_ be set to either true or false.
if [ -z "$JAVACMD" ] ; then
  if [ -n "$JAVA_HOME"  ] ; then
    if [ -x "$JAVA_HOME/jre/sh/java" ] ; then
      # IBM's JDK on AIX uses strange locations for the executables
      JAVACMD="$JAVA_HOME/jre/sh/java"
    else
      JAVACMD="$JAVA_HOME/bin/java"
    fi
  else
    JAVACMD=`which java`
  fi
fi

 if [ ! -x "$JAVACMD" ] ; then
  echo "Error: JAVA_HOME is not defined correctly." 1>&2
  echo "  We cannot execute $JAVACMD" 1>&2
  exit 1
fi

# JSVC support
if [ -z "$JSVCCMD" ] ; then
  if [ -x "/usr/bin/jsvc" ] ; then
    JSVCCMD="$JAVA_HOME/jre/sh/java"
  else
    JSVCCMD=`which jsvc`
  fi
fi

 if [ ! -x "$JSVCCMD" ] ; then
  echo "Error: Not found JSVC installed." 1>&2
  echo "  We cannot execute $JSVCCMD" 1>&2
  exit 1
fi

CLASSPATH="$CLASSPATH:$META_CONF/:$META_LIB/*:"

jsvc_exec()
{
    cd $FILE_PATH
    $JSVCCMD -home $JAVA_HOME -cp $CLASSPATH -user $META_SERVER_USER -outfile $META_LOG_OUT -errfile $META_LOG_ERR \
    -pidfile $META_PID $JAVA_OPTS @EXTRA_JVM_ARGUMENTS@ $1 @MAINCLASS@ @APP_ARGUMENTS@
}

case "$1" in
    start)
        echo "Starting the @APP_NAME@..."

        # Start the service
        jsvc_exec

        echo "The @APP_NAME@ has started."
    ;;
    stop)
        echo "Stopping the @APP_NAME@..."

        # Stop the service
        jsvc_exec "-stop"

        echo "The @APP_NAME@ has stopped."
    ;;
    restart)
        if [ -f "$PID" ]; then

            echo "Restarting the @APP_NAME@..."

            # Stop the service
            jsvc_exec "-stop"

            # Start the service
            jsvc_exec

            echo "The @APP_NAME@ has restarted."
        else
            echo "Daemon not running, no action taken"
            exit 1
        fi
            ;;
    *)
    echo "Usage: /etc/init.d/$PRG {start|stop|restart}" >&2
    exit 3
    ;;
esac




