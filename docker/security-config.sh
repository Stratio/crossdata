#!/bin/bash -xe

#Ensure security folder is created
mkdir -p /etc/sds/crossdata/security

#Get from vault the different data
#TODO: REMOVE -k FROM CURL WHEN TRUSTSTORE FOR VAULT IS INSIDE THE DOCKER!!!!!!!!!!!
# Get params
#VAULT_TOKEN='4d6cafd2-f5a4-abad-88f0-48eae1c24904'  # This is the only env. value from entry point


# Main execution

## Init
#TENANT_NAME="crossdata1"   # MARATHON_APP_ID without slash
#VAULT_HOST='127.0.0.1'    # It should be a predefined hostname
#VAULT_PORT='8200'       # It should be a predefined port

## Configure constant
export SERVER_CERT_ALIAS="crossdata-server"
export XD_TLS_JKS_NAME="/etc/sds/crossdata/security/server.jks"
export XD_TRUST_JKS_NAME="/etc/sds/crossdata/security/truststore.jks"
export XD_KEYTAB_NAME="/etc/sds/crossdata/security/crossdata.keytab"
export GOSEC_PLUGIN_JKS_NAME="/etc/sds/crossdata/security/gosec-plugin.jks"

## Generating uuid
export UUID=$(uuidgen)

####################################################
## Get XD TLS Server Info and set XD_TLS_PASSWORD
####################################################
source tls-config.sh

#######################################################
## Create XD Truststore and set XD_TRUSTSTORE_PASSWORD
#######################################################
source truststore-config.sh

####################################################
## Kerberos config
####################################################
source kerberos-server-config.sh

#######################################################
## Gosec-plugin config
#######################################################
source gosec-config.sh $1

#######################################################
## HDFS security
#######################################################
source hdfs-security.sh

#######################################################
## Crossdata security
#######################################################
source crossdata-security.sh


