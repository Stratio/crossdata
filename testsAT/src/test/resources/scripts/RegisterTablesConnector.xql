CREATE CATALOG catalogTest;
REGISTER TABLE IF NOT EXISTS catalogTest.tableTest ON CLUSTER ClusterTest(id int, name text, age int, phone BIGINT, salary DOUBLE, reten FLOAT, new BOOLEAN, PRIMARY KEY(id, name));

REGISTER TABLE IF NOT EXISTS catalogTest.tab1 ON CLUSTER ClusterTest(id int, age int, PRIMARY KEY(id));


REGISTER TABLE IF NOT EXISTS catalogTest.tab2 ON CLUSTER ClusterTest(name text, surname text, PRIMARY KEY(name));


REGISTER TABLE IF NOT EXISTS catalogTest.tab3 ON CLUSTER ClusterTest(id BIGINT, age BIGINT, PRIMARY KEY(id));


REGISTER TABLE IF NOT EXISTS catalogTest.tab4 ON CLUSTER ClusterTest(money DOUBLE, reten DOUBLE, PRIMARY KEY(money));


REGISTER TABLE IF NOT EXISTS catalogTest.tab5 ON CLUSTER ClusterTest(money FLOAT, reten FLOAT, PRIMARY KEY(money));


REGISTER TABLE IF NOT EXISTS catalogTest.tab6 ON CLUSTER ClusterTest(married BOOLEAN , new BOOLEAN,  PRIMARY KEY(married));


REGISTER TABLE catalogTest.usersorderby ON CLUSTER ClusterTest ( company text, client text, factcompany bigint, factclient double, futurefact float, foundationyear int, stockmarket boolean, PRIMARY KEY (company, client));



