#!/bin/bash
@LICENSE_HEADER@

JMXPort=7194


JMX_OPTIONS="-Dcom.sun.management.jmxremote.port=${JMXPort} -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false"

# Read configuration variable file if it is present
if [ -r /etc/default/meta ]; then
    . /etc/default/meta
fi

# resolve links - $0 may be a softlink
PRG="$0"

while [ -h "${PRG}" ]; do
  ls=`ls -ld "${PRG}"`
  link=`expr "${ls}" : '.*-> \(.*\)$'`
  if expr "${link}" : '/.*' > /dev/null; then
    PRG="${link}"
  else
    PRG=`dirname "${PRG}"`/"${link}"
  fi
done

PRGDIR=`dirname "${PRG}"`
BASEDIR=`cd "${PRGDIR}/.." >/dev/null; pwd`


if [ -z "${META_CONF}" ]; then
    META_CONF="${BASEDIR}/conf"
fi

if [ -f "${META_CONF}/meta-env.sh" ]; then
    source "${META_CONF}/meta-env.sh"
fi



# Reset the REPO variable. If you need to influence this use the environment setup file.
REPO=
@ENV_SETUP@

# OS specific support.  $var _must_ be set to either true or false.
if [ -z "${JAVACMD}" ] ; then
  if [ -n "${JAVA_HOME}"  ] ; then
    if [ -x "${JAVA_HOME}/jre/sh/java" ] ; then
      # IBM's JDK on AIX uses strange locations for the executables
      JAVACMD="${JAVA_HOME}/jre/sh/java"
    else
      JAVACMD="${JAVA_HOME}/bin/java"
    fi
  else
    JAVACMD=`which java`
  fi
fi

if [ ! -x "${JAVACMD}" ] ; then
  echo "Error: JAVA_HOME is not defined correctly." 1>&2
  echo "  We cannot execute ${JAVACMD}" 1>&2
  exit 1
fi

if [ -z "${REPO}" ]
then
  REPO="${BASEDIR}"/@REPO@
fi

#CLASSPATH="${CLASSPATH}:${META_CONF}/:${META_LIB}/*:"

LIB_CLASSPATH=""
while read line
do
        LIB_CLASSPATH="${LIB_CLASSPATH}:${META_LIB}/$line"
done < "${META_CONF}/libs-order"

CLASSPATH="${CLASSPATH}:${META_CONF}/:${LIB_CLASSPATH}:${META_LIB}/*:"


exec "${JAVACMD}" ${JAVA_OPTS} @EXTRA_JVM_ARGUMENTS@ \
  -classpath "${CLASSPATH}" \
  -Dapp.name="@APP_NAME@" \
  -Dapp.pid="$$" \
  ${JMX_OPTIONS} \
  @MAINCLASS@ \
  @APP_ARGUMENTS@"$@"@UNIX_BACKGROUND@
