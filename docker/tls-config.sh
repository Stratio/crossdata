#!/bin/bash -xe

### Get certificate from KMS
curl -k -L -H "X-Vault-Token:$VAULT_TOKEN"  "https://$VAULT_HOST:$VAULT_PORT/v1/crossdata/$TENANT_NAME/tls/x509-server-cert" -s | jq -r ".data .certificate_chain" > "$UUID.crt"
curl -k -L -H "X-Vault-Token:$VAULT_TOKEN"  "https://$VAULT_HOST:$VAULT_PORT/v1/crossdata/$TENANT_NAME/tls/x509-server-cert" -s | jq -r ".data .private_key" > "$UUID.key"

### Get keystore password
export XD_TLS_PASSWORD=$(curl -k -L -s -H "X-Vault-Token:$VAULT_TOKEN"  https://$VAULT_HOST:$VAULT_PORT/v1/crossdata/$TENANT_NAME/tls/keystore | jq -r ".data .keystore_pass")

## Generating PKCS12
openssl pkcs12 -inkey "$UUID.key" -name "$SERVER_CERT_ALIAS" -in "$UUID.crt" \
               -export -out "$XD_TLS_JKS_NAME.pkcs12" -password "env:XD_TLS_PASSWORD"

## Generating JKS
keytool -importkeystore -srckeystore "$XD_TLS_JKS_NAME.pkcs12" -srcalias "$SERVER_CERT_ALIAS" \
        -srcstorepass "$XD_TLS_PASSWORD" -srcstoretype PKCS12 -destkeystore "$XD_TLS_JKS_NAME" -deststorepass "$XD_TLS_PASSWORD"

## Cleaning
rm -f $XD_TLS_JKS_NAME.pkcs12 $UUID.crt $UUID.key