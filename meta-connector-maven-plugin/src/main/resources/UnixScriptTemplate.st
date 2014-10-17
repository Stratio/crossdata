#!/bin/bash

### BEGIN INIT INFO
# Provides: <name>
# Required-Start: $remote_fs
# Required-Stop: $remote_fs
# Should-Stop: $all
# Default-Start: 2 3 4 5
# Default-Stop: 0 1 6
# Short-Description: Crossdata connector
# Description: <desc>
### END INIT INFO
# Developed by pmadrid@stratio.com
# Broken by alvaro@stratio.com
# Version: 0.1 2014
# When I learn scripting a bit better, I'll try to improve this one...

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
INIT_HOME=`cd "${DIR}/.." >/dev/null; pwd`

echo "INIT_HOME=${INIT_HOME}"

NAME="<name>"
DESC="<desc>"
BASEDIR=${INIT_HOME}
CONFDIR=${INIT_HOME}/conf
LOGFILE=${INIT_HOME}/logs
pidDir=${INIT_HOME}
serviceUser="<user>"
serviceGroup="<user>"
javaCommandLineKeyword="<mainClassName>"
connectorClass="<mainClass>"
JMXPort="<jmxPort>"
# If JAVA_HOME has not been set, try to determine it.ner
JVM_SEARCH_DIRS="/usr/java/default /usr/java/latest /opt/java"

if [ -z "$JAVA_HOME" ]; then
	# If java is in PATH, use a JAVA_HOME that corresponds to that.
	java="`/usr/bin/which java 2>/dev/null`"

	if [ -n "$java" ]; then
		java=`readlink --canonicalize "$java"`
		JAVA_HOME=`dirname "\`dirname \$java\`"`
	else
		# No JAVA_HOME set and no java found in PATH; search for a JVM.
		for jdir in $JVM_SEARCH_DIRS; do
			if [ -x "$jdir/bin/java" ]; then
				JAVA_HOME="$jdir"
				break
			fi
		done
	fi
fi

if [ -z "$JAVA_HOME" ]; then
	echo "Error: JAVA_HOME is not defined correctly." 1>&2
	echo " We cannot execute $JAVA" 1>&2
	exit 1
fi

export JAVA_HOME
[ -f "/etc/default/$NAME" ] && . /etc/default/$NAME
CLASSPATH="$CONFDIR"

for file in $BASEDIR/*; do
	CLASSPATH="$CLASSPATH:$file"
done

LIB_CLASSPATH=""
for file in ${BASEDIR}/lib/*; do
        LIB_CLASSPATH="${LIB_CLASSPATH}:${file}"
done

CLASSPATH="${CLASSPATH}:${LIB_CLASSPATH}"

JMX_OPTIONS="-Dcom.sun.management.jmxremote.port=${JMXPort} -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false"

javaArgs="$javaArgs -classpath $CLASSPATH -Djava.net.preferIPv4Stack=true ${JMX_OPTIONS} ${connectorClass}"
javaCommandLine="${JAVA_HOME}/bin/java $javaArgs"

if [ ! -d "$pidDir" ]; then
	mkdir -p "$pidDir"
	if [ $? -ne 0 ]; then exit 1; fi
fi
pidFile="$pidDir/$NAME.pid"

# Returns 0 if the process with PID $1 is running.
function checkProcessIsRunning {
	local pid="$1"
	if [ -z "$pid" -o "$pid" == " " ]; then return 1; fi
	ps -Af | grep ${pid} | grep java
    if [ $? -ne 0 ]; then return 1; fi
	return 0;
}

# Returns 0 if the process with PID $1 is our Java service process.
function checkProcessIsOurService {
	local pid="$1"
	ps -Af | grep ${pid} | grep java
	if [ $? -ne 0 ]; then return 1; fi
	return 0;
}

# Returns 0 when the service is running and sets the variable $servicePid to the PID.
function getServicePid {
	if [ ! -f $pidFile ]; then return 1; fi
	local servicePid="$(<$pidFile)"
	checkProcessIsRunning $servicePid || return 1
	checkProcessIsOurService $servicePid || return 1
	return 0;
}

function startServiceProcess {
	cd $BASEDIR || return 1
	rm -f $pidFile
	local cmd="$javaCommandLine >>$LOGFILE 2>&1 & echo \$! >$pidFile"
	#sudo -u $serviceUser $SHELL -c "$cmd" || return 1
	echo "Launching service process: ${cmd}"
	su - $serviceUser -c "$cmd" || return 1
	sleep 0.1
	servicePid="$(<$pidFile)"
	echo "PID: ${pidFile} servicePid: ${servicePid}"
	if checkProcessIsRunning $servicePid; then :; else
		echo -e "\n$DESC start failed, see logfile."
		rm -f $pidfile
		return 1
	fi
	return 0;
}

function stopServiceProcess {
	servicePid="$(<$pidFile)"
	kill $servicePid || return 1
	local killWaitTime=10
	for ((i=0; i<$killWaitTime*10; i++)); do
		checkProcessIsRunning $servicePid
		if [ $? -ne 0 ]; then
			rm -f $pidFile
			return 0
		fi
		sleep 0.1
	done
	echo -e "\n$DESC did not terminate within 10 seconds, sending SIGKILL..."
	kill -s KILL $servicePid || return 1
	for ((i=0; i<$killWaitTime*10; i++)); do
		checkProcessIsRunning $servicePid
		if [ $? -ne 0 ]; then
			rm -f $pidFile
			return 0
		fi
		sleep 0.1
	done
	echo "Error: $DESC could not be stopped within 20 seconds!"
	return 1;
}

function startService {
	# Add any service start conditions here
	getServicePid
	if [ $? -eq 0 ]; then echo "${DESC} is already running"; return 0; fi
	echo -n "Starting ${DESC}"
	startServiceProcess
	if [ $? -ne 0 ]; then echo "Error starting ${DESC}" ; return 1; fi
	return 0;
}

function stopService {
	getServicePid
	if [ $? -ne 0 ]; then echo "${DESC} is not running"; return 0; fi
	echo -n "Stopping $DESC "
	stopServiceProcess
	if [ $? -ne 0 ]; then echo "Error stopping ${DESC}"; return 1; fi
	return 0;
}

function checkServiceStatus {
	echo -n "Checking for $DESC: "
	if [ ! -f "$CONFDIR/connector-application.conf" ]; then
		echo "Error: Configuration file not found!"
		return 1
	fi	
	if getServicePid; then
		local servicePid="$(<$pidFile)"
		echo "$DESC seems to be running (pid $servicePid)"
		return 0
	else
		echo "$DESC seems to be stopped"
		return 1
	fi
	return 0;
}

case "$1" in
	start)
		startService
		retval=$?
		exit $retval
	;;
	stop)
		stopService
		retval=$?
		exit $retval
	;;
	restart)
		stopService && startService
		retval=$?
		exit $retval
	;;
	status)
		checkServiceStatus
		retval=$?
		exit $retval
	;;
	*)
		echo "Usage: $0 {start|stop|restart|status}"
		exit 1
	;;
esac

exit 0
