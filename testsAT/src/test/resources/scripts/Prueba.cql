CREATE KEYSPACE selectdemo WITH replication = {'class' : 'SimpleStrategy', 'replication_factor': 1};

USE selectdemo;

# Order by
CREATE TABLE selectdemo.usersorderby ( company varchar, client varchar, factcompany bigint, factclient double, futurefact float, foundationyear int, stockmarket boolean, PRIMARY KEY (company, client));

INSERT INTO selectdemo.usersorderby (company, client, factcompany, factclient, futurefact, foundationyear, stockmarket ) VALUES ( 'Company01', 'Client01', 1000000, 20000.50, 200000.12, 2004, true);
INSERT INTO selectdemo.usersorderby (company, client, factcompany, factclient, futurefact, foundationyear, stockmarket ) VALUES ( 'Company02', 'Client02', 2000000, 20000.90, 300000.12, 2002, true);
INSERT INTO selectdemo.usersorderby (company, client, factcompany, factclient, futurefact, foundationyear, stockmarket ) VALUES ( 'Company03', 'Client03', 100000, 10000.50, 250000.12, 2004, false);
INSERT INTO selectdemo.usersorderby (company, client, factcompany, factclient, futurefact, foundationyear, stockmarket ) VALUES ( 'Company04', 'Client04', 1050000, 30000.50, 420000.12, 2006, true);
INSERT INTO selectdemo.usersorderby (company, client, factcompany, factclient, futurefact, foundationyear, stockmarket ) VALUES ( 'Company05', 'Client05', 3000000, 20500.50, 570000.12, 1999, false);
INSERT INTO selectdemo.usersorderby (company, client, factcompany, factclient, futurefact, foundationyear, stockmarket ) VALUES ( 'Company01', 'Client06', 1000000, 50000.00, 200000.12, 2004, true);
INSERT INTO selectdemo.usersorderby (company, client, factcompany, factclient, futurefact, foundationyear, stockmarket ) VALUES ( 'Company02', 'Client07', 2000000, 20001.50, 300000.12, 2002, true);
INSERT INTO selectdemo.usersorderby (company, client, factcompany, factclient, futurefact, foundationyear, stockmarket ) VALUES ( 'Company03', 'Client08', 100000, 30052.50, 250000.12, 2004, false);
INSERT INTO selectdemo.usersorderby (company, client, factcompany, factclient, futurefact, foundationyear, stockmarket ) VALUES ( 'Company04', 'Client09', 1050000, 50000.50, 420000.12, 2006, true);
INSERT INTO selectdemo.usersorderby (company, client, factcompany, factclient, futurefact, foundationyear, stockmarket ) VALUES ( 'Company05', 'Client10', 3000000, 20500.50, 570000.12, 1999, false);
INSERT INTO selectdemo.usersorderby (company, client, factcompany, factclient, futurefact, foundationyear, stockmarket ) VALUES ( 'Company01', 'Client02', 1000000, 20000.90, 200000.12, 2004, true);
INSERT INTO selectdemo.usersorderby (company, client, factcompany, factclient, futurefact, foundationyear, stockmarket ) VALUES ( 'Company02', 'Client01', 2000000, 20000.50, 300000.12, 2002, true);
INSERT INTO selectdemo.usersorderby (company, client, factcompany, factclient, futurefact, foundationyear, stockmarket ) VALUES ( 'Company03', 'Client04', 100000, 30000.50, 250000.12, 2004, false);
INSERT INTO selectdemo.usersorderby (company, client, factcompany, factclient, futurefact, foundationyear, stockmarket ) VALUES ( 'Company04', 'Client03', 1050000, 10000.50, 420000.12, 2006, true);
INSERT INTO selectdemo.usersorderby (company, client, factcompany, factclient, futurefact, foundationyear, stockmarket ) VALUES ( 'Company05', 'Client06', 3000000, 50000.00, 570000.12, 1999, false);
INSERT INTO selectdemo.usersorderby (company, client, factcompany, factclient, futurefact, foundationyear, stockmarket ) VALUES ( 'Company01', 'Client05', 1000000, 20500.50, 200000.12, 2004, true);
INSERT INTO selectdemo.usersorderby (company, client, factcompany, factclient, futurefact, foundationyear, stockmarket ) VALUES ( 'Company02', 'Client08', 2000000, 30052.50, 300000.12, 2002, true);
INSERT INTO selectdemo.usersorderby (company, client, factcompany, factclient, futurefact, foundationyear, stockmarket ) VALUES ( 'Company03', 'Client07', 100000, 20001.50, 250000.12, 2004, false);
INSERT INTO selectdemo.usersorderby (company, client, factcompany, factclient, futurefact, foundationyear, stockmarket ) VALUES ( 'Company04', 'Client10', 1050000, 20500.50, 420000.12, 2006, true);
INSERT INTO selectdemo.usersorderby (company, client, factcompany, factclient, futurefact, foundationyear, stockmarket ) VALUES ( 'Company05', 'Client09', 3000000, 50000.50, 570000.12, 1999, false);

