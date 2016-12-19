#!/bin/bash -xe

### Get keytab
export BASE64_KEYTAB=$(curl -k -L -H "X-Vault-Token:$VAULT_TOKEN"  "https://$VAULT_HOST:$VAULT_PORT/v1/crossdata/$TENANT_NAME/krb" -s | jq -r ".data .keytab_base64")
export XD_PRINCIPAL=$(curl -k -L -H "X-Vault-Token:$VAULT_TOKEN"  "https://$VAULT_HOST:$VAULT_PORT/v1/crossdata/$TENANT_NAME/krb" -s | jq -r ".data .principal")

## Generating keytab
echo $BASE64_KEYTAB | base64 -d > $XD_KEYTAB_NAME


#Set krb5.conf
cat > $JAVA_HOME/jre/lib/security/krb5.conf <<EOF
    [libdefaults]
     default_realm = __<REALM>__
     dns_lookup_realm = false
     udp_preference_limit = 1
    [realms]
     __<REALM>__ = {
       kdc = __<KDC_HOST>__
       admin_server = __<KADMIN_HOST>__
       default_domain = __<LW_REALM>__
     }
    [domain_realm]
     .__<LW_REALM>__ = __<REALM>__
     __<LW_REALM>__ = __<REALM>__
EOF


lw_realm=$(echo $REALM | tr '[:upper:]' '[:lower:]')
sed -i "s#__<REALM>__#$REALM#" $JAVA_HOME/jre/lib/security/krb5.conf \
&& echo "[KRB-CONF] Realm configured in krb5.conf" \
|| echo "[KRB-CONF-ERROR] Something went wrong when REALM was configured in krb5.conf"

sed -i "s#__<LW_REALM>__#$lw_realm#" $JAVA_HOME/jre/lib/security/krb5.conf \
&& echo "[KRB-CONF] Domain configured in krb5.conf" \
|| echo "[KRB-CONF-ERROR] Something went wrong when DOMAIN was configured in krb5.conf"

sed -i "s#__<KDC_HOST>__#$KDC_HOST#" $JAVA_HOME/jre/lib/security/krb5.conf \
&& echo "[KRB-CONF] kdc host configured in krb5.conf" \
|| echo "[KRB-CONF-ERROR] Something went wrong when kdc host was configured in krb5.conf"

sed -i "s#__<KADMIN_HOST>__#$KADMIN_HOST#" $JAVA_HOME/jre/lib/security/krb5.conf \
&& echo "[KRB-CONF] kadmin host configured in krb5.conf" \
|| echo "[KRB-CONF-ERROR] Something went wrong when kadmin host was configured in krb5.conf"

