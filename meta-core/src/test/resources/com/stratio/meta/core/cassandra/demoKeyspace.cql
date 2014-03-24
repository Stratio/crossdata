CREATE KEYSPACE demo WITH REPLICATION = {'class' : 'SimpleStrategy', 'replication_factor': 1};

USE demo;

CREATE TABLE demo.users ( name varchar, gender varchar, email varchar, age int, bool boolean, phrase text, lucene_index_1 text, PRIMARY KEY ((name, gender), email, age));

#Create lucene index
CREATE CUSTOM INDEX demo_lucene ON demo.users (lucene_index_1) USING 'org.apache.cassandra.db.index.stratio.RowIndex' WITH OPTIONS = {'refresh_seconds':'1', 'schema':'{default_analyzer:"org.apache.lucene.analysis.standard.StandardAnalyzer",fields:{name:{type:"string"}, gender:{type:"string"}, email:{type:"string"}, age:{type:"integer"}, bool:{type:"boolean"}, phrase:{type:"text", analyzer:"org.apache.lucene.analysis.en.EnglishAnalyzer"}}}'};

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

