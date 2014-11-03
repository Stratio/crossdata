/*
 * Licensed to STRATIO (C) under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  The STRATIO (C) licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.stratio.crossdata.sh.help;

/**
 * Type of help requested by the user through the CROSSDATA shell.
 */
public enum HelpType {
    /**
     * Show console help.
     */
    CONSOLE_HELP,
    /**
     * Show exit help.
     */
    EXIT,
    /**
     * Describe supported datatypes.
     */
    DATATYPES,
    /**
     * Show create help.
     */
    CREATE,
    /**
     * Show create keyspace help.
     */
    CREATE_KEYSPACE,
    /**
     * Show create table help.
     */
    CREATE_TABLE,
    /**
     * Show create index help.
     */
    CREATE_INDEX,
    /**
     * Show create lucene index help.
     */
    CREATE_LUCENE_INDEX,
    /**
     * Show update help.
     */
    UPDATE,
    /**
     * Show insert into help.
     */
    INSERT_INTO,
    /**
     * Show truncate help.
     */
    TRUNCATE,
    /**
     * Show drop help.
     */
    DROP,
    /**
     * Show drop index help.
     */
    DROP_INDEX,
    /**
     * Show drop table help.
     */
    DROP_TABLE,
    /**
     * Show drop keyspace help.
     */
    DROP_KEYSPACE,
    /**
     * Show drop trigger help.
     */
    DROP_TRIGGER,
    /**
     * Show select help.
     */
    SELECT,
    /**
     * Show add help.
     */
    ADD,
    /**
     * Show list help.
     */
    LIST,
    /**
     * Show list process help.
     */
    LIST_PROCESS,
    /**
     * Show list udf help.
     */
    LIST_UDF,
    /**
     * Show list trigger help.
     */
    LIST_TRIGGER,
    /**
     * Show remove udf help.
     */
    REMOVE_UDF,
    /**
     * Show delete help.
     */
    DELETE,
    /**
     * Show set options help.
     */
    SET_OPTIONS,
    /**
     * Show explain plan help.
     */
    EXPLAIN_PLAN,
    /**
     * Show alter help.
     */
    ALTER,
    /**
     * Show alter keyspace help.
     */
    ALTER_KEYSPACE,
    /**
     * Show alter table help.
     */
    ALTER_TABLE,
    /**
     * Show stop help.
     */
    STOP,
    /**
     * Show describe help.
     */
    DESCRIBE,
    /**
     * Show describe keyspace help.
     */
    DESCRIBE_KEYSPACE,
    /**
     * Show describe keyspace help.
     */
    DESCRIBE_KEYSPACES,
    /**
     * Show describe table help.
     */
    DESCRIBE_TABLE,
    /**
     * Show describe table help.
     */
    DESCRIBE_TABLES
}
