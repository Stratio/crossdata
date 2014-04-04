CREATE KEYSPACE demo WITH REPLICATION = {'class' : 'SimpleStrategy', 'replication_factor': 1};

USE demo;

CREATE TABLE demo.users ( name varchar, gender varchar, email varchar, age int, bool boolean, phrase varchar, stratio_lucene_index_1 varchar, PRIMARY KEY ((name, gender), email, age));

#Create lucene index
CREATE CUSTOM INDEX demo_lucene ON demo.users (stratio_lucene_index_1) USING 'org.apache.cassandra.db.index.stratio.RowIndex' WITH OPTIONS = {'refresh_seconds':'1', 'schema':'{default_analyzer:"org.apache.lucene.analysis.standard.StandardAnalyzer",fields:{name:{type:"string"}, gender:{type:"string"}, email:{type:"string"}, age:{type:"integer"}, bool:{type:"boolean"}, phrase:{type:"text", analyzer:"org.apache.lucene.analysis.en.EnglishAnalyzer"}}}'};

#Create Cassandra indexes
CREATE INDEX users_gender_idx ON users (gender);
CREATE INDEX users_age_idx ON users (age);
CREATE INDEX users_bool_idx ON users (bool);

#Insert test data without phrase
INSERT INTO demo.users (name, gender, email, age, bool, phrase) VALUES ('name_0', 'male', 'name_0@domain.com', 10, true, '');
INSERT INTO demo.users (name, gender, email, age, bool, phrase) VALUES ('name_1', 'male', 'name_1@domain.com', 11, true, '');
INSERT INTO demo.users (name, gender, email, age, bool, phrase) VALUES ('name_2', 'male', 'name_2@domain.com', 12, true, '');
INSERT INTO demo.users (name, gender, email, age, bool, phrase) VALUES ('name_3', 'male', 'name_3@domain.com', 13, true, '');
INSERT INTO demo.users (name, gender, email, age, bool, phrase) VALUES ('name_4', 'male', 'name_4@domain.com', 14, true, '');
INSERT INTO demo.users (name, gender, email, age, bool, phrase) VALUES ('name_5', 'male', 'name_5@domain.com', 15, false, '');
INSERT INTO demo.users (name, gender, email, age, bool, phrase) VALUES ('name_6', 'male', 'name_6@domain.com', 16, false, '');
INSERT INTO demo.users (name, gender, email, age, bool, phrase) VALUES ('name_7', 'male', 'name_7@domain.com', 17, false, '');
INSERT INTO demo.users (name, gender, email, age, bool, phrase) VALUES ('name_8', 'male', 'name_8@domain.com', 18, false, '');
INSERT INTO demo.users (name, gender, email, age, bool, phrase) VALUES ('name_9', 'male', 'name_9@domain.com', 19, false, '');

#Insert test data for lucene complex queries
INSERT INTO demo.users (name, gender, email, age, bool, phrase) VALUES ('name_10', 'male', 'name_10@domain.com', 100, true, 'Apache Lucene is a high-performance, full-featured text search engine library written enterely in Java.');
INSERT INTO demo.users (name, gender, email, age, bool, phrase) VALUES ('name_11', 'male', 'name_11@domain.com', 101, true, 'It is a technology suitable for nearly any application that requires full-text search, especially cross-platform');
INSERT INTO demo.users (name, gender, email, age, bool, phrase) VALUES ('name_12', 'male', 'name_12@domain.com', 102, false, 'Apache Lucene is an open source project available for free download.');
INSERT INTO demo.users (name, gender, email, age, bool, phrase) VALUES ('name_13', 'male', 'name_13@domain.com', 103, false, 'Please use the links on the right to access Lucene');
INSERT INTO demo.users (name, gender, email, age, bool, phrase) VALUES ('name_14', 'male', 'name_14@domain.com', 104, false, 'Lucene is a Java full-text search engine.');
INSERT INTO demo.users (name, gender, email, age, bool, phrase) VALUES ('name_15', 'male', 'name_15@domain.com', 105, false, 'Lucene is not a complete application, but rather a code library and API that can easily be used to add search capabilities to applications.');

# Table with a counter column. This columns can only be updated and do not support inserts.
CREATE TABLE demo.counter (name varchar, counter_value counter, PRIMARY KEY (name));
UPDATE demo.counter SET counter_value = counter_value + 1 WHERE name = 'name_0';
UPDATE demo.counter SET counter_value = counter_value + 1 WHERE name = 'name_1';
UPDATE demo.counter SET counter_value = counter_value + 1 WHERE name = 'name_2';
UPDATE demo.counter SET counter_value = counter_value + 1 WHERE name = 'name_3';
UPDATE demo.counter SET counter_value = counter_value + 1 WHERE name = 'name_4';
UPDATE demo.counter SET counter_value = counter_value + 1 WHERE name = 'name_5';
UPDATE demo.counter SET counter_value = counter_value + 1 WHERE name = 'name_6';
UPDATE demo.counter SET counter_value = counter_value + 1 WHERE name = 'name_7';
UPDATE demo.counter SET counter_value = counter_value + 1 WHERE name = 'name_8';
UPDATE demo.counter SET counter_value = counter_value + 1 WHERE name = 'name_9';

# Table with columns representing all the supported datatypes.
CREATE TABLE demo.types ( varchar_column varchar, bigint_column bigint, boolean_column boolean, double_column double, float_column float, int_column int, PRIMARY KEY (varchar_column));

INSERT INTO demo.types (varchar_column, bigint_column , boolean_column , double_column , float_column , int_column ) VALUES ( 'name_0', 10000, true, 1.0, 1.0, 100);
INSERT INTO demo.types (varchar_column, bigint_column , boolean_column , double_column , float_column , int_column ) VALUES ( 'name_1', 10001, true, 1.01, 1.01, 101);
INSERT INTO demo.types (varchar_column, bigint_column , boolean_column , double_column , float_column , int_column ) VALUES ( 'name_2', 10002, true, 1.02, 1.02, 102);
INSERT INTO demo.types (varchar_column, bigint_column , boolean_column , double_column , float_column , int_column ) VALUES ( 'name_3', 10003, true, 1.03, 1.03, 103);
INSERT INTO demo.types (varchar_column, bigint_column , boolean_column , double_column , float_column , int_column ) VALUES ( 'name_4', 10004, true, 1.04, 1.04, 104);
INSERT INTO demo.types (varchar_column, bigint_column , boolean_column , double_column , float_column , int_column ) VALUES ( 'name_5', 10005, true, 1.05, 1.05, 105);
INSERT INTO demo.types (varchar_column, bigint_column , boolean_column , double_column , float_column , int_column ) VALUES ( 'name_6', 10006, true, 1.06, 1.06, 106);
INSERT INTO demo.types (varchar_column, bigint_column , boolean_column , double_column , float_column , int_column ) VALUES ( 'name_7', 10007, true, 1.07, 1.07, 107);
INSERT INTO demo.types (varchar_column, bigint_column , boolean_column , double_column , float_column , int_column ) VALUES ( 'name_8', 10008, true, 1.08, 1.08, 108);
INSERT INTO demo.types (varchar_column, bigint_column , boolean_column , double_column , float_column , int_column ) VALUES ( 'name_9', 10009, true, 1.09, 1.09, 109);

