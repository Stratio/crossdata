/*
 * Stratio Meta
 *
 * Copyright (c) 2014, Stratio, All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 */

package com.stratio.meta.sh.help;

/**
 * Type of help requested by the user through the META shell.
 */
public enum HelpType {
	CONSOLE_HELP,
	EXIT,
	DATATYPES,
	CREATE,
	CREATE_KEYSPACE,
	CREATE_TABLE,
	CREATE_INDEX,
	CREATE_LUCENE_INDEX,
	UPDATE,
	INSERT_INTO,
	TRUNCATE,
	DROP,
	DROP_INDEX,
	DROP_TABLE,
	DROP_KEYSPACE,
	DROP_TRIGGER,
	SELECT,
	ADD,
	LIST,
	LIST_PROCESS,
	LIST_UDF,
	LIST_TRIGGER,
	REMOVE_UDF,
	DELETE,
	SET_OPTIONS,
	EXPLAIN_PLAN,
	ALTER,
	ALTER_KEYSPACE,
	ALTER_TABLE,
	STOP,
	DESCRIBE,
        DESCRIBE_KEYSPACE,
	DESCRIBE_TABLE
}
