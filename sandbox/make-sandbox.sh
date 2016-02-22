#!/bin/bash
# Start time stamp
startTimestampUtc=`date -u  "+%Y%m%d %H:%M:%S"`
BASE_DIR=`dirname "${0}"`
CURRENT_DIR=`pwd`
# Initialize parameters and functions
. ${BASE_DIR}/functions.sh

function usageHelp ()
{
    echo 1>&2 "Usage: ${0} -v <version>"
    echo 1>&2 "Parameters:"
    echo 1>&2 "    -v <version>: Version of Crossdata to use, is mandatory"
    echo 1>&2 "Examples:"
    echo 1>&2 "    ${0} -v 1.0.1  -- Creates and packages the sandbox for Stratio Crossdata 1.0.1"
}

# Check if number of parameters is the expected
if [ $# -eq 2 ]; then
    while getopts v: o
    do    case "${o}" in
        v)  version="${OPTARG}";;
        [?]) echo 1>&2 "ERROR: ${0}:";usageHelp $*;exit 1;;
        esac
    done
else
    # Incorrect number of parameters
    echo 1>&2 "ERROR: ${0}: Number of parameters not correct: $#"
    usageHelp $*
    exit 2
fi

tarFile="stratio-crossdata-sandbox-${version}.tar.gz"
sanboxZip="https://github.com/Stratio/sandbox/archive/crossdata-1.0.zip"
name="crossdata-sandbox"

echo 1<&2 "INFO: ${0}: Running with following parameters: "
echo 1>&2 "    version: ${version}"

echo 1<&2 "INFO: ${0}: Cleaning data from previous executions"
deleteIfExists "crossdata-1.0.zip"
deleteIfExists "sandbox-develop"
VBoxManage unregistervm --delete stratio-sandbox-crossdata
VBoxManage unregistervm --delete stratio-sandbox-crossdata
deleteIfExists "package.box"
deleteIfExists "tmp"

echo 1<&2 "INFO: ${0}: Downloading unzipping base sanbox files from ${sanboxZip}"
wget ${sanboxZip}
unzip -q crossdata-1.0.zip
cd sandbox-crossdata-1.0
echo 1<&2 "INFO: ${0}: Preparing vagrant config file from template"
template="vagrant_settings.yml.template"
file="vagrant_settings.yml"
cp ${file} ${template}
sed -e "s:\${crossdata_version}:$version:" ${template} > $file
echo 1<&2 "INFO: ${0}: Running vagrant"
vagrant up
echo 1<&2 "INFO: ${0}: Stopping vagrant"
vagrant halt
echo 1<&2 "INFO: ${0}: Packaging Box"
vagrant package --vagrantfile VagrantfileBox
vagrant box add package.box --name $name --force

echo 1<&2 "INFO: ${0}: Preparing to start again and test it"
mkdir tmp/
cp ${file} tmp/
cd tmp
vagrant init $name
vagrant up
vagrant ssh
vagrant halt
cd ..
echo 1<&2 "INFO: ${0}: Creating tar.gz file"
tar -cvzf ${tarFile} package.box
if [ $? != 0 ]; then
    echo 1<&2 "ERROR: Something gone wrong, exiting :("
    exit 1
fi
# End time stamp
endTimestampUtc=`date -u  "+%Y%m%d %H:%M:%S"`

echo 1<&2 "INFO: ${0}: Finished process started at: ${startTimestampUtc}, finished at: ${endTimestampUtc}"
echo 1<&2 "INFO: ${0}: File ${tarFile} is ready. Enjoy :)"