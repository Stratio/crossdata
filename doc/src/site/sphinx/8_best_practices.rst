==================================
Best practices and recommendations
==================================

When using the whole Crossdata stack, that is, including the Driver and the Server, there are some recommendations for distributed environments,
specially in Amazon. In a scenario where the Crossdata driver is going to be executed in a different location where the Crossdata servers are running,
some configuration issues have to be taking into account in order to access remotely::

1. Use the public DNS names or hosts of the Crossdata servers in the driver configuration instead of the IP addresses

2. In all the machines where a Crossdata server is running, add the following associations to the /etc/hosts file, enumerating all the Crossdata
servers that form the Crossdata system::
    + Private-IP_Server1 Public-DNS_Server1
    + Private-IP_Server2 Public-DNS_Server2
    + Private-IP_Server3 Public-DNS_Server3
    + ...

3. In the machine where the Crossdata driver is going to be running, add the following associations to the /etc/hosts file, enumerating all
the Crossdata servers that form the Crossdata system::
    + Public-IP_Server1 Public-DNS_Server1
    + Public-IP_Server2 Public-DNS_Server2
    + Public-IP_Server3 Public-DNS_Server3
    + ...

Recommended Configuration:
--------------------------
See `Spark Hardware Provisioning <http://spark.apache.org/docs/latest/hardware-provisioning.html>`_

TODO