First Steps with Crossdata Sandbox
=============================================
Once you have started the sandbox you can follow this steps::

    > sudo su -

Start crossdata::

    > cd /etc/init.d
    > service crossdata start

Start Connectors::

    > service connector_cassandra start
    > service connector_mongodb start
    > service connector_sparksql start

At this point, we have all that we need so now we start crossdata shell::

    > cd /opt/sds/crossdata/bin
    > ./crossdata-sh

Now you can play with the shell with this example of crossdata use:

The first steps are to attach the connectors...::

   ATTACH CLUSTER cassandra_prod ON DATASTORE Cassandra WITH OPTIONS {'Hosts': '[127.0.0.1]', 'Port': 9042, 'rpcPort':9160, 'cluster':'cassandra_prod'};
   ATTACH CLUSTER mongoCluster ON DATASTORE Mongo WITH OPTIONS {'Hosts': '[127.0.0.1]', 'Port': '[27017]'};
   ATTACH CONNECTOR CassandraConnector TO cassandra_prod WITH OPTIONS {'DefaultLimit': '1000'} AND PRIORITY=1;
   ATTACH CONNECTOR MongoConnector TO mongoCluster WITH OPTIONS {};
   ATTACH CONNECTOR SparkSQLConnector TO cassandra_prod WITH OPTIONS {'DefaultLimit': '1000'};
   ATTACH CONNECTOR SparkSQLConnector TO mongoCluster WITH OPTIONS {'DefaultLimit': '1000'};

Now we can operate as usual...::

    CREATE CATALOG catalogTest;
    USE catalogTest;
    CREATE TABLE tableTest ON CLUSTER cassandra_prod (id int PRIMARY KEY, serial int, name text, rating double, email text);
    CREATE TABLE tableTest2 ON CLUSTER cassandra_prod (id int PRIMARY KEY, lastname text, age int, company text);
    CREATE TABLE students ON CLUSTER mongoCluster (id int PRIMARY KEY, enrolled boolean);

You can insert a few rows by executing::

    INSERT INTO catalogTest.tableTest(id, serial, name, rating, email) VALUES (999, 54000, 'Peter', 8.9,'myemail@yahoo.com');
    INSERT INTO catalogTest.tableTest(id, serial, name, rating, email) VALUES (1000, 71098, 'Charles', 2.7,'contact@stratio.com');
    INSERT INTO catalogTest.tableTest(id, serial, name, rating, email) VALUES (1001, 34539, 'John', 9.3,'crossdata@stratio.com');

    INSERT INTO catalogTest.tableTest2(id, lastname, age, company) VALUES (999, 'Miller', 23, 'Best Company');
    INSERT INTO catalogTest.tableTest2(id, lastname, age, company) VALUES (1000, 'Fernandez', 35, 'Stratio');
    INSERT INTO catalogTest.tableTest2(id, lastname, age, company) VALUES (1001, 'Yorke', 42, 'Big Data Company');

    INSERT INTO students(id, enrolled) VALUES (999, true);
    INSERT INTO students(id, enrolled) VALUES (2,  true);
    INSERT INTO students(id, enrolled) VALUES (1000, true);
    INSERT INTO students(id, enrolled) VALUES (1001, true);
    INSERT INTO students(id, enrolled) VALUES (5, false);

    SELECT * FROM catalogTest.tableTest;
    SELECT id, age FROM catalogTest.tableTest2;

    SELECT name, age FROM catalogTest.tableTest INNER JOIN catalogTest.tableTest2 ON tableTest.id=tableTest2.id;
    SELECT name, enrolled FROM tableTest INNER JOIN students ON tableTest.id=students.id;
Let's create a full text index::

    CREATE FULL_TEXT INDEX myIndex ON tableTest(email);
    SELECT * FROM tableTest WHERE email MATCH '*yahoo*';
