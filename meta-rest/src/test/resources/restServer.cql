CREATE KEYSPACE selectdemo WITH REPLICATION = {'class' : 'SimpleStrategy', 'replication_factor': 1};

USE selectdemo;

CREATE TABLE selectdemo.users ( name varchar, gender varchar, email varchar, age int, bool boolean, phrase varchar, PRIMARY KEY ((name, gender), email, age)) WITH comment='Select demo table';

INSERT INTO selectdemo.users (name, gender, email, age, bool, phrase) VALUES ('name_0', 'male', 'name_0@domain.com', 10, true, '');
INSERT INTO selectdemo.users (name, gender, email, age, bool, phrase) VALUES ('name_1', 'male', 'name_1@domain.com', 11, true, '');
INSERT INTO selectdemo.users (name, gender, email, age, bool, phrase) VALUES ('name_2', 'male', 'name_2@domain.com', 12, true, '');
INSERT INTO selectdemo.users (name, gender, email, age, bool, phrase) VALUES ('name_3', 'male', 'name_3@domain.com', 13, true, '');
INSERT INTO selectdemo.users (name, gender, email, age, bool, phrase) VALUES ('name_4', 'male', 'name_4@domain.com', 14, true, '');
INSERT INTO selectdemo.users (name, gender, email, age, bool, phrase) VALUES ('name_5', 'male', 'name_5@domain.com', 15, false, '');
INSERT INTO selectdemo.users (name, gender, email, age, bool, phrase) VALUES ('name_6', 'male', 'name_6@domain.com', 16, false, '');
INSERT INTO selectdemo.users (name, gender, email, age, bool, phrase) VALUES ('name_7', 'male', 'name_7@domain.com', 17, false, '');
INSERT INTO selectdemo.users (name, gender, email, age, bool, phrase) VALUES ('name_8', 'male', 'name_8@domain.com', 18, false, '');
INSERT INTO selectdemo.users (name, gender, email, age, bool, phrase) VALUES ('name_9', 'male', 'name_9@domain.com', 19, false, '');