Crossdata Sandbox
*****************

Crossdata provides a sandbox that allows to test its functionality. This sandbox includes Crossdata 0.3.4, Stratio
Cassandra, and Cassandra and Stratio Deep Connectors.

Requisites
============
`Oracle Virtual Box <https://www.virtualbox.org/wiki/Downloads>`_

`Vagrant <https://www.vagrantup.com/downloads.html>`_

If you are using Windows system you will need:
`Git Console <http://git-scm.com/download/win>`_
or
`CGYWin <https://cygwin.com/install.html>`_

How to get the Crossdata sandbox (Vagrant)
============================================
Start with Crossdata is very easy. Just follow this steps::

    > mkdir CrossdataSandbox
    > cd CrossdataSandbox
    > vagrant init stratio/crossdata
    
At this point we have the vagrant file with the Crossdata configuration ready to start.::

    > vagrant up
    
Now, the sandbox is ready to run.::

    > vagrant ssh
    
Done! You can test Crossdata as you want.

When you finish your tests, you can stop it.::

    > vagrant halt
    
and you can remove the vagrant instance.::

    > vagrant destroy
    

How to get the Crossdata sandbox (OVA)
============================================
You can get the OVA file with the Sandbox from this [link](www.stratio.com/crossdata).
Once it has been downloaded, just import in your Virtual Box.


First Steps with Crossdata Sandbox
=============================================
Once you have started the sandbox you can follow this steps::

    > sudo su -

Start crossdata::

    > cd /etc/init.d
    > service crossdata start

Start Connectors::

    > service connector_cassandra start
    > service connector_deep start

At this point, we have all that we need so now we start crossdata shell::

    > cd /opt/sds/crossdata/bin
    > ./crossdata-sh

Now you can play with the shell with this example of crossdata use:

The first steps are to attach the connectors...::

    xdsh> ADD DATASTORE /etc/sds/connectors/cassandra/CassandraDataStore.xml;
    xdsh> ATTACH CLUSTER cassandra_prod ON DATASTORE Cassandra WITH OPTIONS {'Hosts': '[127.0.0.1]', 'Port': 9042};
    xdsh> ADD CONNECTOR /etc/sds/connectors/cassandra/CassandraConnector.xml;
    xdsh> ADD CONNECTOR /etc/sds/connectors/deep/DeepConnector.xml;
    xdsh> ATTACH CONNECTOR CassandraConnector TO cassandra_prod WITH OPTIONS {'DefaultLimit': '1000'};
    xdsh> ATTACH CONNECTOR DeepConnector TO cassandra_prod WITH OPTIONS {};

Now we can operate as usual...::

    xdsh> CREATE CATALOG catalogTest;
    xdsh> USE catalogTest;         
    xdsh> CREATE TABLE tableTest ON CLUSTER cassandra_prod (id int PRIMARY KEY, serial int, name text, rating double, email text);
    xdsh> CREATE TABLE tableTest2 ON CLUSTER cassandra_prod (id int PRIMARY KEY, lastname text, age int, company text);

You can insert a few rows by executing::

    xdsh> INSERT INTO catalogTest.tableTest(id, serial, name, rating, email) VALUES (999, 54000, 'Peter', 8.9,'myemail@yahoo.com');
    xdsh> INSERT INTO catalogTest.tableTest(id, serial, name, rating, email) VALUES (1000, 71098, 'Charles', 2.7,'contact@stratio.com');
    xdsh> INSERT INTO catalogTest.tableTest(id, serial, name, rating, email) VALUES (1001, 34539, 'John', 9.3,'crossdata@stratio.com');

    xdsh> INSERT INTO catalogTest.tableTest2(id, lastname, age, company) VALUES (999, 'Miller', 23, 'Best Company');
    xdsh> INSERT INTO catalogTest.tableTest2(id, lastname, age, company) VALUES (1000, 'Fernandez', 35, 'Stratio');
    xdsh> INSERT INTO catalogTest.tableTest2(id, lastname, age, company) VALUES (1001, 'Yorke', 42, 'Big Data Company');

You can also insert 900 rows in every table by typing the next command in a system shell::

    xdsh> exit

    > cd /etc/sds/crossdata/
    > java -jar CrossdataClientExample.jar

Now, we can come back to the crossdata shell and see some results::

    > cd /opt/sds/crossdata/bin
    > ./crossdata-sh

    xdsh> USE catalogTest;
    xdsh> SELECT * FROM catalogTest.tableTest;
    xdsh> SELECT id, age FROM catalogTest.tableTest2;

    xdsh> SELECT name, age FROM catalogtest.tabletest INNER JOIN catalogtest.tabletest2 ON tabletest.id=tabletest2.id;

Let's create a full text index::

    xdsh> CREATE FULL_TEXT INDEX myIndex ON tableTest(email);
    xdsh> SELECT * FROM tabletest WHERE email MATCH '*yahoo*';


F.A.Q about the sandbox
=======================

##### **I am in the same directory that I copy the Vagrant file but I have this error:**::


    A Vagrant environment or target machine is required to run this
    command. Run vagrant init to create a new Vagrant environment. Or,
    get an ID of a target machine from vagrant global-status to run
    this command on. A final option is to change to a directory with a
    Vagrantfile and to try again.


Make sure your file name is Vagrantfile instead of Vagrantfile.txt or VagrantFile.

______________________________________________________________________________________

##### **When I execute vagrant ssh I have this error:** ::


    ssh executable not found in any directories in the %PATH% variable. Is an
    SSH client installed? Try installing Cygwin, MinGW or Git, all of which
    contain an SSH client. Or use your favorite SSH client with the following
    authentication information shown below:


We need to install `Cygwin <https://cygwin.com/install.html>`_ or `Git for Windows <http://git-scm.com/download/win>`_.



For more information please visit `<http://stratio.github.io/crossdata>`_

