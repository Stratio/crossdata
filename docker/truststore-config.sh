#!/bin/bash -xe

### Get keystore password
export XD_TRUSTSTORE_PASSWORD=$(curl -k -L -s -H "X-Vault-Token:$VAULT_TOKEN" https://$VAULT_HOST:$VAULT_PORT/v1/crossdata/$TENANT_NAME/truststore/keystore | jq -r ".data .keystore_pass")

export XD_JVMCA_PASS="changeit" #TODO: This password should be provided by VAULT service

## Prepare Truststore
counter=1
code=$(curl -k -L -s -o /dev/null -w "%{http_code}" -H "X-Vault-Token:$VAULT_TOKEN" https://$VAULT_HOST:$VAULT_PORT/v1/crossdata/$TENANT_NAME/truststore/certs/$counter)
while [  $code -eq 200 ]; do
    curl -k -L -s -H "X-Vault-Token:$VAULT_TOKEN" https://$VAULT_HOST:$VAULT_PORT/v1/crossdata/$TENANT_NAME/truststore/certs/$counter | jq -r ".data .certificate" > "$UUID.crt"

    # Create der file for root CA
    openssl x509 -outform der -in "$UUID.crt" -out $UUID.der

    # Create keystore
    keytool -import -noprompt -alias $counter -keystore $XD_TRUST_JKS_NAME  -storepass $XD_TRUSTSTORE_PASSWORD -file $UUID.der

    # Add CA to JVM Keystore
    keytool -importcert -noprompt -alias $counter -keystore $JAVA_HOME/jre/lib/security/cacerts -storepass $XD_JVMCA_PASS -file $UUID.der

    # Update counter
    let counter=counter+1
    code=$(curl -k -L -s -o /dev/null -w "%{http_code}" -H "X-Vault-Token:$VAULT_TOKEN" https://$VAULT_HOST:$VAULT_PORT/v1/crossdata/$TENANT_NAME/truststore/certs/$counter)

    # Clean
    rm -f $UUID.der "$UUID.crt"
done