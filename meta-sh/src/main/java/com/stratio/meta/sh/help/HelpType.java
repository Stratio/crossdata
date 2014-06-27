/*
 * Stratio Meta
 * 
 * Copyright (c) 2014, Stratio, All rights reserved.
 * 
 * This library is free software; you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation; either version
 * 3.0 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with this library.
 */

package com.stratio.meta.sh.help;

/**
 * Type of help requested by the user through the META shell.
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
