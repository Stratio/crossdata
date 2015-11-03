CREATE CATALOG catalogtest;
CREATE TABLE catalogtest.tableTest ON CLUSTER ClusterTest(id int, name text, age int, phone BIGINT, salary DOUBLE, reten FLOAT, new BOOLEAN, PRIMARY KEY(id));
INSERT INTO catalogtest.tableTest(id, name, age, phone, salary, reten, new) VALUES (1, 'name_1', 10, 10000000, 1111.11, 11.11, true);
INSERT INTO catalogtest.tableTest(id, name, age, phone, salary, reten, new) VALUES (2, 'name_2', 20, 20000000, 2222.22, 12.11, false);
INSERT INTO catalogtest.tableTest(id, name, age, phone, salary, reten, new) VALUES (3, 'name_3', 30, 30000000, 3333.33, 13.11, true);
INSERT INTO catalogtest.tableTest(id, name, age, phone, salary, reten, new) VALUES (4, 'name_4', 40, 40000000, 4444.44, 14.11, false);
INSERT INTO catalogtest.tableTest(id, name, age, phone, salary, reten, new) VALUES (5, 'name_5', 50, 50000000, 5555.55, 15.11, true);
INSERT INTO catalogtest.tableTest(id, name, age, phone, salary, reten, new) VALUES (6, 'name_6', 60, 60000000, 6666.66, 16.11, false);
INSERT INTO catalogtest.tableTest(id, name, age, phone, salary, reten, new) VALUES (7, 'name_7', 70, 70000000, 7777.77, 17.11, true);
INSERT INTO catalogtest.tableTest(id, name, age, phone, salary, reten, new) VALUES (8, 'name_8', 80, 80000000, 8888.88, 18.11, false);
INSERT INTO catalogtest.tableTest(id, name, age, phone, salary, reten, new) VALUES (9, 'name_9', 90, 90000000, 9999.99, 19.11, true);
INSERT INTO catalogtest.tableTest(id, name, age, phone, salary, reten, new) VALUES (10, 'name_10',1, 10000000, 1111.11, 20.11, false);

CREATE TABLE catalogtest.tab1 ON CLUSTER ClusterTest(id int, age int, PRIMARY KEY(id));
INSERT INTO catalogtest.tab1(id, age) VALUES (1, 10);
INSERT INTO catalogtest.tab1(id, age) VALUES (2, 20);
INSERT INTO catalogtest.tab1(id, age) VALUES (3, 30);
INSERT INTO catalogtest.tab1(id, age) VALUES (-4, -40);

CREATE TABLE catalogtest.tab2 ON CLUSTER ClusterTest(name text, surname text, PRIMARY KEY(name));
INSERT INTO catalogtest.tab2(name, surname) VALUES ('Hugo', 'Dominguez');
INSERT INTO catalogtest.tab2(name, surname) VALUES ('Carlos', 'Hernandez');
INSERT INTO catalogtest.tab2(name, surname) VALUES ('Miguel', 'Fernandez');
INSERT INTO catalogtest.tab2(name, surname) VALUES ('Antonio', 'Alcocer');

CREATE TABLE catalogtest.tab3 ON CLUSTER ClusterTest(id BIGINT, age BIGINT, PRIMARY KEY(id));
INSERT INTO catalogtest.tab3(id, age) VALUES (1, 10);
INSERT INTO catalogtest.tab3(id, age) VALUES (2, 20);
INSERT INTO catalogtest.tab3(id, age) VALUES (3, 30);
INSERT INTO catalogtest.tab3(id, age) VALUES (-4, -40);

CREATE TABLE catalogtest.tab4 ON CLUSTER ClusterTest(money DOUBLE, reten DOUBLE, PRIMARY KEY(money));
INSERT INTO catalogtest.tab4(money, reten) VALUES (1.1, 10.10);
INSERT INTO catalogtest.tab4(money, reten) VALUES (2.2, 20.20);
INSERT INTO catalogtest.tab4(money, reten) VALUES (3.3, 30.30);
INSERT INTO catalogtest.tab4(money, reten) VALUES (-4.4, -40.40);

CREATE TABLE catalogtest.tab5 ON CLUSTER ClusterTest(money FLOAT, reten FLOAT, PRIMARY KEY(money));
INSERT INTO catalogtest.tab5(money, reten) VALUES (1.1, 10.10);
INSERT INTO catalogtest.tab5(money, reten) VALUES (2.2, 20.20);
INSERT INTO catalogtest.tab5(money, reten) VALUES (3.3, 30.30);
INSERT INTO catalogtest.tab5(money, reten) VALUES (-4.4, -40.40);

CREATE TABLE catalogtest.tab6 ON CLUSTER ClusterTest(married BOOLEAN , new BOOLEAN,  PRIMARY KEY(married));
INSERT INTO catalogtest.tab6(married, new) VALUES (true, false);
INSERT INTO catalogtest.tab6(married, new) VALUES (false, true);

CREATE TABLE catalogtest.usersorderby ON CLUSTER ClusterTest ( company text, client text, factcompany bigint, factclient double, futurefact float, foundationyear int, stockmarket boolean, PRIMARY KEY (company));

INSERT INTO catalogtest.usersorderby (company, client, factcompany, factclient, futurefact, foundationyear, stockmarket ) VALUES ( 'Company01', 'Client01', 1000000, 20000.50, 200000.12, 2004, true);
INSERT INTO catalogtest.usersorderby (company, client, factcompany, factclient, futurefact, foundationyear, stockmarket ) VALUES ( 'Company02', 'Client02', 2000000, 20000.90, 300000.12, 2002, true);
INSERT INTO catalogtest.usersorderby (company, client, factcompany, factclient, futurefact, foundationyear, stockmarket ) VALUES ( 'Company03', 'Client03', 100000, 10000.50, 250000.12, 2004, false);
INSERT INTO catalogtest.usersorderby (company, client, factcompany, factclient, futurefact, foundationyear, stockmarket ) VALUES ( 'Company04', 'Client04', 1050000, 30000.50, 420000.12, 2006, true);
INSERT INTO catalogtest.usersorderby (company, client, factcompany, factclient, futurefact, foundationyear, stockmarket ) VALUES ( 'Company05', 'Client05', 3000000, 20500.50, 570000.12, 1999, false);
INSERT INTO catalogtest.usersorderby (company, client, factcompany, factclient, futurefact, foundationyear, stockmarket ) VALUES ( 'Company01', 'Client06', 1000000, 50000.00, 200000.12, 2004, true);
INSERT INTO catalogtest.usersorderby (company, client, factcompany, factclient, futurefact, foundationyear, stockmarket ) VALUES ( 'Company02', 'Client07', 2000000, 20001.50, 300000.12, 2002, true);
INSERT INTO catalogtest.usersorderby (company, client, factcompany, factclient, futurefact, foundationyear, stockmarket ) VALUES ( 'Company03', 'Client08', 100000, 30052.50, 250000.12, 2004, false);
INSERT INTO catalogtest.usersorderby (company, client, factcompany, factclient, futurefact, foundationyear, stockmarket ) VALUES ( 'Company04', 'Client09', 1050000, 50000.50, 420000.12, 2006, true);
INSERT INTO catalogtest.usersorderby (company, client, factcompany, factclient, futurefact, foundationyear, stockmarket ) VALUES ( 'Company05', 'Client10', 3000000, 20500.50, 570000.12, 1999, false);
INSERT INTO catalogtest.usersorderby (company, client, factcompany, factclient, futurefact, foundationyear, stockmarket ) VALUES ( 'Company01', 'Client02', 1000000, 20000.90, 200000.12, 2004, true);
INSERT INTO catalogtest.usersorderby (company, client, factcompany, factclient, futurefact, foundationyear, stockmarket ) VALUES ( 'Company02', 'Client01', 2000000, 20000.50, 300000.12, 2002, true);
INSERT INTO catalogtest.usersorderby (company, client, factcompany, factclient, futurefact, foundationyear, stockmarket ) VALUES ( 'Company03', 'Client04', 100000, 30000.50, 250000.12, 2004, false);
INSERT INTO catalogtest.usersorderby (company, client, factcompany, factclient, futurefact, foundationyear, stockmarket ) VALUES ( 'Company04', 'Client03', 1050000, 10000.50, 420000.12, 2006, true);
INSERT INTO catalogtest.usersorderby (company, client, factcompany, factclient, futurefact, foundationyear, stockmarket ) VALUES ( 'Company05', 'Client06', 3000000, 50000.00, 570000.12, 1999, false);
INSERT INTO catalogtest.usersorderby (company, client, factcompany, factclient, futurefact, foundationyear, stockmarket ) VALUES ( 'Company01', 'Client05', 1000000, 20500.50, 200000.12, 2004, true);
INSERT INTO catalogtest.usersorderby (company, client, factcompany, factclient, futurefact, foundationyear, stockmarket ) VALUES ( 'Company02', 'Client08', 2000000, 30052.50, 300000.12, 2002, true);
INSERT INTO catalogtest.usersorderby (company, client, factcompany, factclient, futurefact, foundationyear, stockmarket ) VALUES ( 'Company03', 'Client07', 100000, 20001.50, 250000.12, 2004, false);
INSERT INTO catalogtest.usersorderby (company, client, factcompany, factclient, futurefact, foundationyear, stockmarket ) VALUES ( 'Company04', 'Client10', 1050000, 20500.50, 420000.12, 2006, true);
INSERT INTO catalogtest.usersorderby (company, client, factcompany, factclient, futurefact, foundationyear, stockmarket ) VALUES ( 'Company05', 'Client09', 3000000, 50000.50, 570000.12, 1999, false);

