#!/bin/bash
@LICENSE_HEADER@


scriptdir=`pwd`"/"`basename $0`"/"`dirname $0`
dist=0
if [ -n "`echo $scriptdir | grep 'bin' | grep 'crossdata-dist' `" ]; then dist=1;fi

# Read configuration variable file if it is present
if [ -r /etc/default/crossdata ]; then
 if [ $dist -lt 1 ]; then
    . /etc/default/crossdata/crossdata-env.sh
 fi
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


if [ -z "${CROSSDATA_CONF}" ]; then
    CROSSDATA_CONF="${BASEDIR}/conf"
fi

if [ -f "${CROSSDATA_CONF}/crossdata-env.sh" ]; then
    source "${CROSSDATA_CONF}/crossdata-env.sh"
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

LIB=${CROSSDATA_LIB}
CLASSPATH=${CLASSPATH}:${CROSSDATA_CONF}/:$(JARS=("$LIB"/*.jar); IFS=:; echo "${JARS[*]}")

exec "${JAVACMD}" ${JAVA_OPTS} @EXTRA_JVM_ARGUMENTS@ \
  -classpath "${CLASSPATH}" \
  -Dapp.name="@APP_NAME@" \
  -Dapp.pid="$$" \
  ${ENGINE_OPTS} \
  @MAINCLASS@ \
  @APP_ARGUMENTS@"$@"@UNIX_BACKGROUND@
