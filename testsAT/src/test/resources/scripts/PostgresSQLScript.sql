CREATE SCHEMA databasetest;
CREATE TABLE databasetest.crossdatanumbers (col_1 smallint, col_2 integer, col_3 bigint, col_4 decimal, col_5 numeric, col_6 real, col_7 double precision, col_8 smallserial, col_9 serial, col_10 bigserial);
INSERT INTO databasetest.crossdatanumbers(col_1 , col_2 , col_3 , col_4 , col_5 , col_6 , col_7 , col_8 , col_9 , col_10 ) VALUES (-32768,-2147483648,-9223372036854775808,-1000.0001,-1000.0001,20000.00,2.00000000000000,1,1,1);
INSERT INTO databasetest.crossdatanumbers(col_1 , col_2 , col_3 , col_4 , col_5 , col_6 , col_7 , col_8 , col_9 , col_10 ) VALUES (-100,-100,-100,-100.001,-100.001,20000.00,2000000.00000000,10,10,10);
INSERT INTO databasetest.crossdatanumbers(col_1 , col_2 , col_3 , col_4 , col_5 , col_6 , col_7 , col_8 , col_9 , col_10 ) VALUES (-10,-10,-10,-10.01,-10.01,20000.00,20000000.0000000,100,100,100);
INSERT INTO databasetest.crossdatanumbers(col_1 , col_2 , col_3 , col_4 , col_5 , col_6 , col_7 , col_8 , col_9 , col_10 ) VALUES (-1,-1,-1,-1.1,-1.1,20000.00,200000000.000000,1000,1000,1000);
INSERT INTO databasetest.crossdatanumbers(col_1 , col_2 , col_3 , col_4 , col_5 , col_6 , col_7 , col_8 , col_9 , col_10 ) VALUES (0,0,0,0.0,0.0,20.00000,2000000000.00000,10000,10000,10000);
INSERT INTO databasetest.crossdatanumbers(col_1 , col_2 , col_3 , col_4 , col_5 , col_6 , col_7 , col_8 , col_9 , col_10 ) VALUES (1,1,1,1.1,1.1,200.0000,20000000000.0000,20000,100000,100000);
INSERT INTO databasetest.crossdatanumbers(col_1 , col_2 , col_3 , col_4 , col_5 , col_6 , col_7 , col_8 , col_9 , col_10 ) VALUES (10,10,10,10.01,10.01,2000.000,200000000000.000,25000,1000000,1000000);
INSERT INTO databasetest.crossdatanumbers(col_1 , col_2 , col_3 , col_4 , col_5 , col_6 , col_7 , col_8 , col_9 , col_10 ) VALUES (100,100,100,100.001,100.001,20000.00,2000000000000.00,26000,10000000,10000000);
INSERT INTO databasetest.crossdatanumbers(col_1 , col_2 , col_3 , col_4 , col_5 , col_6 , col_7 , col_8 , col_9 , col_10 ) VALUES (1000,1000,1000,1000.0001,1000.0001,200000.0,20000000000000.0,30000,100000000,100000000);
INSERT INTO databasetest.crossdatanumbers(col_1 , col_2 , col_3 , col_4 , col_5 , col_6 , col_7 , col_8 , col_9 , col_10 ) VALUES (32767,2147483647, 9223372036854775807,10000.00001,10000.00001,2000000,200000000000000,32767,2147483647,9223372036854775807);
CREATE TABLE databasetest.crossdatacharacter(col_1 character varying(9), col_2 varchar(9),col_3 character(9),col_4 char(9), col_5 text);
INSERT INTO databasetest.crossdatacharacter(col_1,col_2,col_3,col_4,col_5) VALUES ('example_1','example_1','example_1','example_1','example_1');
INSERT INTO databasetest.crossdatacharacter(col_1,col_2,col_3,col_4,col_5) VALUES ('example_2','example_2','example_2','example_2','example_2');
INSERT INTO databasetest.crossdatacharacter(col_1,col_2,col_3,col_4,col_5) VALUES ('example_3','example_3','example_3','example_3','example_3');
INSERT INTO databasetest.crossdatacharacter(col_1,col_2,col_3,col_4,col_5) VALUES ('example_4','example_4','example_4','example_4','example_4');
INSERT INTO databasetest.crossdatacharacter(col_1,col_2,col_3,col_4,col_5) VALUES ('example_5','example_5','example_5','example_5','example_5');
INSERT INTO databasetest.crossdatacharacter(col_1,col_2,col_3,col_4,col_5) VALUES ('example_6','example_6','example_6','example_6','example_6');
INSERT INTO databasetest.crossdatacharacter(col_1,col_2,col_3,col_4,col_5) VALUES ('example_7','example_7','example_7','example_7','example_7');
INSERT INTO databasetest.crossdatacharacter(col_1,col_2,col_3,col_4,col_5) VALUES ('example_8','example_8','example_8','example_8','example_8');
INSERT INTO databasetest.crossdatacharacter(col_1,col_2,col_3,col_4,col_5) VALUES ('example_9','example_9','example_9','example_9','example_9');
CREATE TABLE databasetest.crossdataboolean(col_1 boolean);
INSERT INTO databasetest.crossdataboolean(col_1) VALUES (TRUE);
INSERT INTO databasetest.crossdataboolean(col_1) VALUES ('t');
INSERT INTO databasetest.crossdataboolean(col_1) VALUES ('true');
INSERT INTO databasetest.crossdataboolean(col_1) VALUES ('y');
INSERT INTO databasetest.crossdataboolean(col_1) VALUES ('yes');
INSERT INTO databasetest.crossdataboolean(col_1) VALUES ('on');
INSERT INTO databasetest.crossdataboolean(col_1) VALUES ('1');
INSERT INTO databasetest.crossdataboolean(col_1) VALUES (FALSE);
INSERT INTO databasetest.crossdataboolean(col_1) VALUES ('f');
INSERT INTO databasetest.crossdataboolean(col_1) VALUES ('false');
INSERT INTO databasetest.crossdataboolean(col_1) VALUES ('n');
INSERT INTO databasetest.crossdataboolean(col_1) VALUES ('no');
INSERT INTO databasetest.crossdataboolean(col_1) VALUES ('off');
INSERT INTO databasetest.crossdataboolean(col_1) VALUES ('0');
CREATE TABLE databasetest.crossdatauuid(col_1 uuid);
INSERT INTO databasetest.crossdatauuid(col_1) VALUES ('A0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A11');
INSERT INTO databasetest.crossdatauuid(col_1) VALUES ('{a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11}');
INSERT INTO databasetest.crossdatauuid(col_1) VALUES ('a0eebc999c0b4ef8bb6d6bb9bd380a11');
INSERT INTO databasetest.crossdatauuid(col_1) VALUES ('a0ee-bc99-9c0b-4ef8-bb6d-6bb9-bd38-0a11');
INSERT INTO databasetest.crossdatauuid(col_1) VALUES ('{a0eebc99-9c0b4ef8-bb6d6bb9-bd380a11}');
CREATE TABLE databasetest.crossdataarray(col_1 text,col_2 integer[]);
INSERT INTO databasetest.crossdataarray(col_1, col_2) VALUES ('Bill','{10000, 10000, 10000, 10000}');
INSERT INTO databasetest.crossdataarray(col_1, col_2)    VALUES ('Carol','{20000, 25000, 25000, 25000}');
SET timezone = 'America/Los_Angeles';
CREATE TABLE databasetest.crossdatatimestamp(col_1 timestamp, col_2 timestamp with time zone not null default NOW());
INSERT INTO databasetest.crossdatatimestamp(col_1, col_2) VALUES ('2016-12-15 15:12:32.459957', '2016-12-15 15:12:32.459957');
CREATE TABLE databasetest.crossdatadate(col_1 DATE);
INSERT INTO databasetest.crossdatadate(col_1) VALUES ('1999-01-08');
INSERT INTO databasetest.crossdatadate(col_1) VALUES ('January 8, 1999');
INSERT INTO databasetest.crossdatadate(col_1) VALUES ('1/8/1999');
INSERT INTO databasetest.crossdatadate(col_1) VALUES ('1/18/1999');
INSERT INTO databasetest.crossdatadate(col_1) VALUES ('01/02/03');
INSERT INTO databasetest.crossdatadate(col_1) VALUES ('1999-Jan-08');
INSERT INTO databasetest.crossdatadate(col_1) VALUES ('Jan-08-1999');
INSERT INTO databasetest.crossdatadate(col_1) VALUES ('08-Jan-1999');
INSERT INTO databasetest.crossdatadate(col_1) VALUES ('08-Jan-99');
INSERT INTO databasetest.crossdatadate(col_1) VALUES ('Jan-08-99');
INSERT INTO databasetest.crossdatadate(col_1) VALUES ('19990108');
INSERT INTO databasetest.crossdatadate(col_1) VALUES ('990108');
INSERT INTO databasetest.crossdatadate(col_1) VALUES ('1999.008');
INSERT INTO databasetest.crossdatadate(col_1) VALUES ('J2451187');
CREATE TABLE databasetest.crossdatatime(col_1 time, col_2 time with time zone);
INSERT INTO databasetest.crossdatatime(col_1, col_2) VALUES ('04:05:06.789','04:05:06 PST');
INSERT INTO databasetest.crossdatatime(col_1, col_2) VALUES ('04:05:06','2003-04-12 04:05:06 America/New_York');
INSERT INTO databasetest.crossdatatime(col_1, col_2) VALUES ('04:05','2003-04-12 04:05:06 PST8PDT');
INSERT INTO databasetest.crossdatatime(col_1, col_2) VALUES ('040506','040506 -8:00');
INSERT INTO databasetest.crossdatatime(col_1, col_2) VALUES ('04:05 AM','04:05 AM -800');
INSERT INTO databasetest.crossdatatime(col_1, col_2) VALUES ('04:05 PM','04:05 PM -8');

-- Not supported types:
-- - text[][] any [][] is not supported
-- - money
-- - Enum type
-- - point
-- - line
-- - lseg
-- - box
-- - path
-- - polygon
-- - circle
-- - cidr
-- - col_2
-- - inet
-- - macaddr
-- - BIT()
-- - BIT VARYING()

--CREATE TABLE databasetest.crossdataarray(col_1 text,col_2 integer[],col_3 text[][]);
--INSERT INTO databasetest.crossdataarray(col_1, col_2,col_3) VALUES ('Bill','{10000, 10000, 10000, 10000}','{{"meeting", "lunch"}, {"training", "presentation"}}');
--INSERT INTO databasetest.crossdataarray(col_1, col_2,col_3)    VALUES ('Carol','{20000, 25000, 25000, 25000}','{{"breakfast", "consulting"}, {"meeting", "lunch"}}');
--CREATE TABLE databasetest.crossdatamonetary(col_1 money);
--INSERT INTO databasetest.crossdatamonetary(col_1) VALUES (-10.08);
--INSERT INTO databasetest.crossdatamonetary(col_1) VALUES (0.00);
--INSERT INTO databasetest.crossdatamonetary(col_1) VALUES (10.08);
--CREATE TYPE databasetest.crossdatatype AS ENUM('type1', 'type2', 'type3');
--CREATE TABLE databasetest.crossdataenum(col_1 databasetest.crossdatatype);
--INSERT INTO databasetest.crossdataenum(col_1) VALUES ('type1');
--INSERT INTO databasetest.crossdataenum(col_1) VALUES ('type2');
--INSERT INTO databasetest.crossdataenum(col_1) VALUES ('type3');
--CREATE TABLE databasetest.crossdatageometric(col_1 point, col_2 line, col_3 lseg, col_4 box, col_5 path, col_6 polygon, col_7 circle);
--CREATE TABLE databasetest.crossdatanetwork(col_1 cidr, col_2 inet);
--INSERT INTO databasetest.crossdatanetwork(col_1, col_2) VALUES ('192.168.100.128/25','192.168.100.128/25');
--INSERT INTO databasetest.crossdatanetwork(col_1, col_2) VALUES ('192.168/24','192.168.0.0/24');
--INSERT INTO databasetest.crossdatanetwork(col_1, col_2) VALUES ('192.168/25','192.168.0.0/25');
--INSERT INTO databasetest.crossdatanetwork(col_1, col_2) VALUES ('192.168.1','192.168.1.0');
--INSERT INTO databasetest.crossdatanetwork(col_1, col_2) VALUES ('192.168','192.168.0.0');
--INSERT INTO databasetest.crossdatanetwork(col_1, col_2) VALUES ('128.1','128.1.0.0');
--INSERT INTO databasetest.crossdatanetwork(col_1, col_2) VALUES ('128','128.0.0.0');
--INSERT INTO databasetest.crossdatanetwork(col_1, col_2) VALUES ('128.1.2','128.1.2.0');
--INSERT INTO databasetest.crossdatanetwork(col_1, col_2) VALUES ('10.1.2','10.1.2.0');
--INSERT INTO databasetest.crossdatanetwork(col_1, col_2) VALUES ('10.1','10.1.0.0');
--INSERT INTO databasetest.crossdatanetwork(col_1, col_2) VALUES ('10','10.0.0.0');
--INSERT INTO databasetest.crossdatanetwork(col_1, col_2) VALUES ('10.1.2.3','10.1.2.3');
--INSERT INTO databasetest.crossdatanetwork(col_1, col_2) VALUES ('2001:4f8:3:ba::/64','2001:4f8:3:ba::/64');
--INSERT INTO databasetest.crossdatanetwork(col_1, col_2) VALUES ('2001:4f8:3:ba:2e0:81ff:fe22:d1f1/128','2001:4f8:3:ba:2e0:81ff:fe22:d1f1/128');
--INSERT INTO databasetest.crossdatanetwork(col_1, col_2) VALUES ('::ffff:1.2.3.0/120','::ffff:1.2.3.0/120');
--INSERT INTO databasetest.crossdatanetwork(col_1, col_2) VALUES ('::ffff:1.2.3.0/128','::ffff:1.2.3.0/128');
--CREATE TABLE databasetest.crossdatamacaddr(col_1 macaddr);
--INSERT INTO databasetest.crossdatamacaddr(col_1) VALUES ('08:00:2b:01:02:03');
--INSERT INTO databasetest.crossdatamacaddr(col_1) VALUES ('08-00-2b-01-02-03');
--INSERT INTO databasetest.crossdatamacaddr(col_1) VALUES ('08002b:010203');
--INSERT INTO databasetest.crossdatamacaddr(col_1) VALUES ('08002b-010203');
--INSERT INTO databasetest.crossdatamacaddr(col_1) VALUES ('0800.2b01.0203');
--INSERT INTO databasetest.crossdatamacaddr(col_1) VALUES ('08002b010203');
--CREATE TABLE databasetest.crossdatabit (col_1 BIT(3), col_2 BIT VARYING(5));
--INSERT INTO databasetest.crossdatabit(col_1, col_2) VALUES (B'101', B'00');
--INSERT INTO databasetest.crossdatabit(col_1, col_2) VALUES (B'100', B'101');