#!/bin/bash -xe

#######################################################
## Get Gosec-plugin LDAP user and pass and set XD vars
#######################################################

### Get LDAP user and pass
export XD_GOSEC_PLUGIN_LDAP_USER=$(curl -k -L -H "X-Vault-Token:$VAULT_TOKEN"  "https://$VAULT_HOST:$VAULT_PORT/v1/crossdata/$TENANT_NAME/gosec-plugin/ldap" -s | jq -r ".data .\"user\"")
export XD_GOSEC_PLUGIN_LDAP_PASS=$(curl -k -L -H "X-Vault-Token:$VAULT_TOKEN"  "https://$VAULT_HOST:$VAULT_PORT/v1/crossdata/$TENANT_NAME/gosec-plugin/ldap" -s | jq -r ".data .\"pass\"")


#############################################################################
## Get XD Gosec-plugin x509 client cert and set XD_GOSEC_PLUGIN_JKS_PASSWORD
#############################################################################

### Get certificate from KMS
curl -k -L -H "X-Vault-Token:$VAULT_TOKEN"  "https://$VAULT_HOST:$VAULT_PORT/v1/crossdata/$TENANT_NAME/gosec-plugin/x509-auth/x509-client-cert" -s | jq -r ".data .certificate_chain" > "$UUID.crt"
curl -k -L -H "X-Vault-Token:$VAULT_TOKEN"  "https://$VAULT_HOST:$VAULT_PORT/v1/crossdata/$TENANT_NAME/gosec-plugin/x509-auth/x509-client-cert" -s | jq -r ".data .private_key" > "$UUID.key"

### Get keystore password
export XD_GOSEC_PLUGIN_JKS_PASSWORD=$(curl -k -L -s -H "X-Vault-Token:$VAULT_TOKEN" https://$VAULT_HOST:$VAULT_PORT/v1/crossdata/$TENANT_NAME/tls/keystore | jq -r ".data .keystore_pass")

## Generating PKCS12
openssl pkcs12 -inkey "$UUID.key" -name "$SERVER_CERT_ALIAS" -in "$UUID.crt" \
               -export -out "$GOSEC_PLUGIN_JKS_NAME.pkcs12" -password "env:XD_GOSEC_PLUGIN_JKS_PASSWORD"

## Generating JKS
keytool -importkeystore -srckeystore "$GOSEC_PLUGIN_JKS_NAME.pkcs12" -srcalias "$SERVER_CERT_ALIAS" \
        -srcstorepass "$XD_GOSEC_PLUGIN_JKS_PASSWORD" -srcstoretype PKCS12 -destkeystore "$GOSEC_PLUGIN_JKS_NAME" -deststorepass "$XD_GOSEC_PLUGIN_JKS_PASSWORD"

## Cleaning
rm -f $GOSEC_PLUGIN_JKS_NAME.pkcs12 $UUID.crt $UUID.key

        #Set JAAS config
        cat > /etc/sds/crossdata/security/jaas.conf<<EOF
Client {
         com.sun.security.auth.module.Krb5LoginModule required
         useKeyTab=true
         storeKey=true
         useTicketCache=false
         keyTab="<__KEYTAB__>"
         principal="<__PRINCIPAL__>";
        };
EOF

export XD_JAAS_FILE=/etc/sds/crossdata/security/jaas.conf
export XD_PLUGIN_CLIENT_JAAS_PATH=$XD_JAAS_FILE

    sed -i "s#<__PRINCIPAL__>#$XD_PRINCIPAL#" $XD_PLUGIN_CLIENT_JAAS_PATH \
       && echo "[JAAS_CONF] ZK principal configured as $XD_PRINCIPAL" \
       || echo "[JAAS_CONF-ERROR] ZK principal was NOT configured"
    sed -i "s#<__KEYTAB__>#$XD_KEYTAB_NAME#" $XD_PLUGIN_CLIENT_JAAS_PATH\
       && echo "[JAAS_CONF] ZK keytab configured as $XD_KEYTAB_NAME" \
       || echo "[JAAS_CONF-ERROR] ZK keytab was NOT configured"


    #Set LDAP config
   export XD_PLUGIN_LDAP_PRINCIPAL=$XD_GOSEC_PLUGIN_LDAP_USER
   export XD_PLUGIN_LDAP_CREDENTIALS=$XD_GOSEC_PLUGIN_LDAP_PASS

    #Set Kafka config
    export XD_PLUGIN_KAFKA_TRUSTSTORE_PASSWORD=$XD_TRUSTSTORE_PASSWORD
    export XD_PLUGIN_KAFKA_TRUSTSTORE=$XD_TRUST_JKS_NAME
    export XD_PLUGIN_KAFKA_KEYSTORE=$GOSEC_PLUGIN_JKS_NAME
    export XD_PLUGIN_KAFKA_KEYSTORE_PASSWORD=$XD_GOSEC_PLUGIN_JKS_PASSWORD
    export XD_PLUGIN_KAFKA_KEY_PASSWOR=D$XD_GOSEC_PLUGIN_JKS_PASSWOR
