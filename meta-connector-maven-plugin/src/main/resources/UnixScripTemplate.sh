#!/bin/bash
@LICENSE_HEADER@

### BEGIN INIT INFO
# Provides: @NAME@
# Required-Start: $remote_fs
# Required-Stop: $remote_fs
# Should-Stop: $all
# Default-Start: 2 3 4 5
# Default-Stop: 0 1 6
# Short-Description: Monitoring agent for Stratio SDS
# Description: @DESC@
# BigData platform, allowing SDS Manager to check local
# resources.
### END INIT INFO
# Developed by pmadrid@stratio.com
# Broken by alvaro@stratio.com
# Version: 0.1 2014
# When I learn scripting a bit better, I'll try to improve this one...

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
INIT_HOME=`cd "${DIR}/.." >/dev/null; pwd`


NAME=@NAME@
DESC=@DESC@
BASEDIR=${INIT_HOME} #donde esta el binario y el /lib donde esten los lib
CONFDIR=${INIT_HOME}/conf # donde esta el fichero de configuracion.
LOGFILE=${INIT_HOME}/logs
pidDir=${INIT_HOME}
lockDir=${INIT_HOME}
lockFile=${lockDir}/@NAME@
serviceUser=@USER@
javaCommandLineKeyword=@MAIN_CLASS@ #todo: poner la clase de meta main del conector de cassandra
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
javaArgs="$javaArgs -classpath $CLASSPATH -Djava.net.preferIPv4Stack=true  $javaCommandLineKeyword"  #este es el comando de java que ejecuta el codigo
javaCommandLine="${JAVA_HOME}/bin/java $javaArgs"
if [ -f $lockFile ]; then
echo "Error: Script already running" && exit 1
fi
touch $lockFile
if [ ! -d "$pidDir" ]; then
mkdir -p "$pidDir"
if [ $? -ne 0 ]; then rm -f $lockFile; exit 1; fi
fi
pidFile="$pidDir/$NAME.pid"
# Returns 0 if the process with PID $1 is running.
function checkProcessIsRunning {
local pid="$1"
if [ -z "$pid" -o "$pid" == " " ]; then return 1; fi
if [ ! -e /proc/$pid ]; then return 1; fi
return 0; }
# Returns 0 if the process with PID $1 is our Java service process.
function checkProcessIsOurService {
local pid="$1"
local cmd="$(ps -p $pid --no-headers -o comm)"
if [ "$cmd" != "java" ]; then return 1; fi
grep -q --binary -F "$javaCommandLineKeyword" /proc/$pid/cmdline
if [ $? -ne 0 ]; then return 1; fi
return 0; }
# Returns 0 when the service is running and sets the variable $servicePid to the PID.
function getServicePid {
if [ ! -f $pidFile ]; then return 1; fi
local servicePid="$(<$pidFile)"
checkProcessIsRunning $servicePid || return 1
checkProcessIsOurService $servicePid || return 1
return 0; }
function startServiceProcess {
cd $BASEDIR || return 1
rm -f $pidFile
local cmd="setsid $javaCommandLine >>$LOGFILE 2>&1 & echo \$! >$pidFile"
#sudo -u $serviceUser $SHELL -c "$cmd" || return 1
su - $serviceUser -c "$cmd" || return 1
sleep 0.1
servicePid="$(<$pidFile)"
if checkProcessIsRunning $servicePid; then :; else
echo -e "\n$DESC start failed, see logfile."
rm -f $pidfile
return 1
fi
return 0; }
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
return 1; }
function startService {
if [ ! -f "$CONFDIR/agent.ini" ]; then
echo "Error: Configuration file not found!"
return 1
fi
getServicePid
if [ $? -eq 0 ]; then echo "$DESC is already running"; return 0; fi
echo -n "Starting $DESC "
startServiceProcess
if [ $? -ne 0 ]; then echo "Error starting $DESC" ; return 1; fi
return 0; }
function stopService {
getServicePid
if [ $? -ne 0 ]; then echo "$DESC is not running"; return 0; fi
echo -n "Stopping $DESC "
stopServiceProcess
if [ $? -ne 0 ]; then echo "Error stopping $DESC"; return 1; fi
return 0; }
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
return 0; }
case "$1" in
start)
startService
retval=$?
rm -f $lockFile
exit $retval
;;
stop)
stopService
retval=$?
rm -f $lockFile
exit $retval
;;
restart)
stopService && startService
retval=$?
rm -f $lockFile
exit $retval
;;
status)
checkServiceStatus
retval=$?
rm -f $lockFile
exit $retval
;;
*)
echo "Usage: $0 {start|stop|restart|status}"
rm -f $lockFile
exit 1
;;
esac
exit 0
