README
======================================================================

First steps to initialize services
------------------------------------
> sudo su -

#Start crossdata
> cd /etc/init.d
> service crossdata start

#Start Connectors
> service connector_cassandra start
> service connector_deep start

#At this point, we have all that we need so now we start crossdata shell
> cd /opt/sds/crossdata/bin
> ./crossdata-sh

Playing with the shell
-----------------------------------
This is an example of crossdata use:

The first steps are to attach the connectors...

1. ADD DATASTORE /etc/sds/connectors/cassandra/CassandraDataStore.xml;
2. ATTACH CLUSTER cassandra_prod ON DATASTORE Cassandra WITH OPTIONS {'Hosts': '[127.0.0.1]', 'Port': 9042};
3. ADD CONNECTOR /etc/sds/connectors/cassandra/CassandraConnector.xml;
4. ADD CONNECTOR /etc/sds/connectors/deep/DeepConnector.xml;
5. ATTACH CONNECTOR CassandraConnector TO cassandra_prod WITH OPTIONS {'DefaultLimit': '1000'};
6. ATTACH CONNECTOR DeepConnector TO cassandra_prod WITH OPTIONS {'DefaultLimit': '1000'};

Now we can operate as usual...

7. CREATE CATALOG catalogTest;
8. USE catalogTest;         
9. CREATE TABLE tableTest ON CLUSTER cassandra_prod (id int PRIMARY KEY, serial int, name text, rating double, email text);
10. CREATE TABLE tableTest2 ON CLUSTER cassandra_prod (id int PRIMARY KEY, lastname text, age int, company text);

You can insert a few rows by executing:

11. INSERT INTO catalogTest.tableTest(id, serial, name, rating, email) VALUES (999, 54000, 'Peter', 8.9,
'myemail@yahoo.com');
12. INSERT INTO catalogTest.tableTest(id, serial, name, rating, email) VALUES (1000, 71098, 'Charles', 2.7,
'contact@stratio.com');
13. INSERT INTO catalogTest.tableTest(id, serial, name, rating, email) VALUES (1001, 34539, 'John', 9.3,
'crossdata@stratio.com');

14. INSERT INTO catalogTest.tableTest2(id, lastname, age, company) VALUES (999, 'Miller', 23, 'Best Company');
15. INSERT INTO catalogTest.tableTest2(id, lastname, age, company) VALUES (1000, 'Fernandez', 35, 'Stratio');
16. INSERT INTO catalogTest.tableTest2(id, lastname, age, company) VALUES (1001, 'Yorke', 42, 'Big Data Company');

You can also insert 900 rows in every table by typing the next command in a system shell:

17. exit

> cd /etc/sds/crossdata/
> java -jar CrossdataClientExample.jar

Now, we can come back to the crossdata shell and see some results:

> cd /opt/sds/crossdata/bin
> ./crossdata-sh

18. USE catalogTest;
19. SELECT * FROM catalogTest.tableTest;
20. SELECT count(*) FROM catalogTest.tableTest;
21. SELECT now() FROM catalogTest.tableTest;
22. SELECT id, age FROM catalogTest.tableTest2;

23. SELECT name, age FROM catalogtest.tabletest INNER JOIN catalogtest.tabletest2 ON tabletest.id=tabletest2.id;

Let's create a full text index:

24. CREATE FULL_TEXT INDEX myIndex ON tableTest(email);

25. SELECT * FROM tabletest WHERE email MATCH '*yahoo*';

