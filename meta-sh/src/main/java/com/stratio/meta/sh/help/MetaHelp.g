/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
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

grammar MetaHelp;

options {
	language = Java;
}

@header {
	package com.stratio.meta.sh.help.generated;
	import com.stratio.meta.sh.help.HelpType;
	import com.stratio.meta.sh.help.HelpStatement;
}

@members {
	public void displayRecognitionError(String[] tokenNames, RecognitionException e){
		System.err.print("Error recognized: ");
		String hdr = getErrorHeader(e);
		String msg = getErrorMessage(e, tokenNames);
		System.err.print(hdr+": ");
		System.err.println(msg);
	}
}

@lexer::header {
	package com.stratio.meta.sh.help.generated;
}

@lexer::members {

}

// Case-insensitive alpha characters
fragment A: ('a'|'A');
fragment B: ('b'|'B');
fragment C: ('c'|'C');
fragment D: ('d'|'D');
fragment E: ('e'|'E');
fragment F: ('f'|'F');
fragment G: ('g'|'G');
fragment H: ('h'|'H');
fragment I: ('i'|'I');
fragment J: ('j'|'J');
fragment K: ('k'|'K');
fragment L: ('l'|'L');
fragment M: ('m'|'M');
fragment N: ('n'|'N');
fragment O: ('o'|'O');
fragment P: ('p'|'P');
fragment Q: ('q'|'Q');
fragment R: ('r'|'R');
fragment S: ('s'|'S');
fragment T: ('t'|'T');
fragment U: ('u'|'U');
fragment V: ('v'|'V');
fragment W: ('w'|'W');
fragment X: ('x'|'X');
fragment Y: ('y'|'Y');
fragment Z: ('z'|'Z');
fragment EXPONENT : ('e'|'E') ('+'|'-')? ('0'..'9')+ ;
fragment POINT: '.';

// Case-insensitive keywords
T_TRUNCATE: T R U N C A T E;
T_CREATE: C R E A T E;
T_ALTER: A L T E R;
T_KEYSPACE: K E Y S P A C E;
T_DROP: D R O P;
T_TABLE: T A B L E;
T_USE: U S E;
T_SET: S E T;
T_OPTIONS: O P T I O N S;
T_ANALYTICS: A N A L Y T I C S;
T_CONSISTENCY: C O N S I S T E N C Y;
T_EXPLAIN: E X P L A I N;
T_PLAN: P L A N;
T_FOR: F O R;
T_INDEX: I N D E X;
T_HASH: H A S H;
T_LIST: L I S T;
T_REMOVE: R E M O V E;
T_UDF: U D F;
T_PROCESS: P R O C E S S;
T_TRIGGER: T R I G G E R;
T_STOP: S T O P;
T_USING: U S I N G;
T_TYPE: T Y P E;
T_ADD: A D D;
T_PRIMARY: P R I M A R Y;
T_KEY: K E Y;
T_INSERT: I N S E R T;
T_INTO: I N T O;
T_COMPACT: C O M P A C T;
T_STORAGE: S T O R A G E;
T_CLUSTERING: C L U S T E R I N G;
T_ORDER: O R D E R;
T_SELECT: S E L E C T;
T_VALUES: V A L U E S;
T_UPDATE: U P D A T E;
T_FROM: F R O M;
T_DELETE: D E L E T E;
T_DISABLE: D I S A B L E;
T_DISTINCT: D I S T I N C T;
T_COUNT: C O U N T;
T_AS: A S;
T_BETWEEN: B E T W E E N;
T_ASC: A S C;
T_DESC: D E S C;
T_LIKE: L I K E;
T_HELP: H E L P;
T_EXIT: E X I T;
T_QUIT: Q U I T;
T_DATATYPES: D A T A T Y P E S;
T_LUCENE: L U C E N E;
T_DEFAULT: D E F A U L T;

T_SEMICOLON: ';';



alterHelpStatement returns [HelpType type]:
	T_ALTER {$type = HelpType.ALTER;}
	(
		T_KEYSPACE {$type = HelpType.ALTER_KEYSPACE;}
		| T_TABLE {$type = HelpType.ALTER_TABLE;}
	)?
	;

listHelpStatement returns [HelpType type]:
	T_LIST {$type = HelpType.LIST;}
	(
		T_PROCESS {$type = HelpType.LIST_PROCESS;}
		| T_UDF {$type = HelpType.LIST_UDF;}
		| T_TRIGGER {$type = HelpType.LIST_TRIGGER;}
	)?
	;

dropHelpStatement returns [HelpType type]:
	T_DROP {$type = HelpType.DROP;}
	(
		T_KEYSPACE {$type = HelpType.DROP_KEYSPACE;}
		| T_TABLE {$type = HelpType.DROP_TABLE;}
		| T_INDEX {$type = HelpType.DROP_INDEX;}
		| T_TRIGGER {$type = HelpType.DROP_TRIGGER;}
	)?
	;

insertHelpStatement returns [HelpType type]:
	T_INSERT {$type = HelpType.INSERT_INTO;}
	(
		T_INTO {$type = HelpType.INSERT_INTO;}
	)?
	;

createHelpStatement returns [HelpType type]:
	T_CREATE {$type = HelpType.CREATE;}
	(
		T_KEYSPACE {$type = HelpType.CREATE_KEYSPACE;}
		| T_TABLE {$type = HelpType.CREATE_TABLE;}
		| T_INDEX {$type = HelpType.CREATE_INDEX;}
		| (T_DEFAULT T_INDEX) {$type = HelpType.CREATE_INDEX;}
		| (T_LUCENE T_INDEX) {$type = HelpType.CREATE_LUCENE_INDEX;}
	)?
	;

helpStatement returns [HelpType type]
	@init{
		HelpType t = HelpType.CONSOLE_HELP;
	}:
	(T_QUIT | T_EXIT) {t = HelpType.EXIT;}
	| (T_DATATYPES) {t = HelpType.DATATYPES;}
	| (createType=createHelpStatement) {t = createType;}
	| (T_UPDATE) {t = HelpType.UPDATE;}
	| (insertType=insertHelpStatement) {t = insertType;}
	| (T_TRUNCATE) {t = HelpType.TRUNCATE;}
	| (dropType=dropHelpStatement) {t = dropType;}
	| (T_SELECT) {t = HelpType.SELECT;}
	| (T_ADD) {t = HelpType.ADD;}
	| (listType=listHelpStatement) {t = listType;}
	| (T_REMOVE T_UDF) {t = HelpType.REMOVE_UDF;}
	| (T_DELETE) {t = HelpType.DELETE;}
	| (T_SET T_OPTIONS) {t = HelpType.SET_OPTIONS;}
	| (T_EXPLAIN T_PLAN) {t = HelpType.EXPLAIN_PLAN;}
	| (alterType=alterHelpStatement) {t = alterType;}
	| (T_STOP) {t = HelpType.STOP;}
	;
	finally{
		$type = t;
	}

query returns [HelpStatement st]
	@init{
		HelpType t = HelpType.CONSOLE_HELP;
	}:
	T_HELP (type=helpStatement {t=type;})? (T_SEMICOLON)* EOF
	;
	finally{
		$st = new HelpStatement(t);
	}
	


WS: (' ' | '\t' | '\n' | '\r')+ { 
        $channel = HIDDEN; 
};

