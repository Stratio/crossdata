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

grammar Meta;

options {
    language = Java;
}
 
@header {
    package com.stratio.meta.core.grammar.generated;
    import com.stratio.meta.common.statements.structures.*;
    import com.stratio.meta.common.statements.structures.assignations.*;
    import com.stratio.meta.common.statements.structures.relationships.*;
    import com.stratio.meta.common.statements.structures.window.*;
    import com.stratio.meta2.common.statements.structures.*;
    import com.stratio.meta2.common.statements.structures.terms.*;
    import com.stratio.meta.common.statements.structures.selectors.*;
    import com.stratio.meta.core.statements.*;
    import com.stratio.meta2.core.statements.*;
    import com.stratio.meta.core.structures.*;
    import com.stratio.meta2.core.structures.*;
    import com.stratio.meta.core.utils.*;
    import java.util.LinkedHashMap;
    import java.util.HashMap;
    import java.util.Map;
    import java.util.Set;
    import java.util.HashSet;
    import org.apache.commons.lang3.tuple.MutablePair;
}

@members {
    private ErrorsHelper foundErrors = new ErrorsHelper();

    public ErrorsHelper getFoundErrors(){
        return foundErrors;
    }

    @Override
    public void displayRecognitionError(String[] tokenNames, RecognitionException e){        
        String hdr = getErrorHeader(e);
        String msg = getErrorMessage(e, tokenNames);
        /*System.err.println("Antlr exception: ");
        System.err.print("\tError recognized: ");
        System.err.print(hdr+": ");
        System.err.println(msg);*/
        AntlrError antlrError = new AntlrError(hdr, msg);
        foundErrors.addError(antlrError);
    }
}

@lexer::header {
    package com.stratio.meta.core.grammar.generated;
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
T_DESCRIBE: D E S C R I B E;
T_TRUNCATE: T R U N C A T E;
T_CREATE: C R E A T E;
T_ALTER: A L T E R;
T_KEYSPACE: K E Y S P A C E;
T_KEYSPACES: K E Y S P A C E S;
T_NOT: N O T;
T_WITH: W I T H;
T_DROP: D R O P;
T_TABLE: T A B L E;
T_TABLES: T A B L E S;
T_IF: I F;
T_EXISTS: E X I S T S;
T_AND: A N D;
T_USE: U S E;
T_SET: S E T;
T_OPTIONS: O P T I O N S;
T_ANALYTICS: A N A L Y T I C S;
T_TRUE: T R U E;
T_FALSE: F A L S E;
T_CONSISTENCY: C O N S I S T E N C Y;
T_ALL: A L L;
T_ANY: A N Y;
T_QUORUM: Q U O R U M;
T_ONE: O N E;
T_TWO: T W O;
T_THREE: T H R E E;
T_EACH_QUORUM: E A C H '_' Q U O R U M;
T_LOCAL_ONE: L O C A L '_' O N E;
T_LOCAL_QUORUM: L O C A L '_' Q U O R U M;
T_EXPLAIN: E X P L A I N;
T_PLAN: P L A N;
T_FOR: F O R;
T_INDEX: I N D E X;
T_LIST: L I S T;
T_REMOVE: R E M O V E;
T_UDF: U D F;
T_PROCESS: P R O C E S S;
T_TRIGGER: T R I G G E R;
T_STOP: S T O P;
T_ON: O N;
T_USING: U S I N G;
T_TYPE: T Y P E;
T_ADD: A D D;
T_PRIMARY: P R I M A R Y;
T_KEY: K E Y;
T_INSERT: I N S E R T;
T_INTO: I N T O;
T_COMPACT: C O M P A C T;
T_STORAGE: S T O R A G E;
T_CLUSTER: C L U S T E R;
T_CLUSTERING: C L U S T E R I N G;
T_ORDER: O R D E R;
T_SELECT: S E L E C T;
T_VALUES: V A L U E S;
T_UPDATE: U P D A T E;
T_WHERE: W H E R E;
T_IN: I N;
T_FROM: F R O M;
T_DELETE: D E L E T E;
T_WINDOW: W I N D O W;
T_LAST: L A S T;
T_ROWS: R O W S;
T_INNER: I N N E R;
T_JOIN: J O I N;
T_BY: B Y;
T_LIMIT: L I M I T;
T_DISABLE: D I S A B L E;
T_DISTINCT: D I S T I N C T;
T_COUNT: C O U N T;
T_AS: A S;
T_BETWEEN: B E T W E E N;
T_ASC: A S C;
T_DESC: D E S C;
T_LIKE: L I K E;
T_EPHEMERAL: E P H E M E R A L;
T_AT: '@';
T_CATALOG: C A T A L O G;
T_CATALOGS: C A T A L O G S;
T_DATASTORE: D A T A S T O R E;
T_CONNECTOR: C O N N E C T O R;

T_SEMICOLON: ';';
T_EQUAL: '=';
T_START_SBRACKET: '{';
T_END_SBRACKET: '}';
T_COLON: ':';
T_COMMA: ',';
T_START_PARENTHESIS: '(';
T_END_PARENTHESIS: ')';
T_QUOTE: '"';
T_SINGLE_QUOTE: '\'';
T_DEFAULT: D E F A U L T;
T_LUCENE: L U C E N E;
T_CUSTOM: C U S T O M;
T_START_BRACKET: '[';
T_END_BRACKET: ']';
T_PLUS: '+';
T_SUBTRACT: '-';
T_INTERROGATION: '?';
T_ASTERISK: '*';
T_GROUP: G R O U P;
T_AGGREGATION: A G G R E G A T I O N;
T_SUM: S U M;
T_MAX: M A X;
T_MIN: M I N;
T_AVG: A V G;
T_GT: '>';
T_LT: '<';
T_GTE: '>' '='; 
T_LTE: '<' '=';
T_NOT_EQUAL: '<' '>'; 
T_TOKEN: T O K E N;
T_MATCH: M A T C H;
T_SEC: S E C;
T_SECS: S E C S;
T_SECOND: S E C O N D;
T_SECONDS: S E C O N D S;
T_MINS: M I N S;
T_MINUTE: M I N U T E;
T_MINUTES: M I N U T E S;
T_HOUR: H O U R;
T_HOURS: H O U R S;
T_DAY: D A Y;
T_DAYS: D A Y S;
T_NULL: N U L L;

fragment LETTER: ('A'..'Z' | 'a'..'z');
fragment DIGIT: '0'..'9';

//QUOTED_LITERAL
//    @init{ StringBuilder sb = new StringBuilder(); }
//    @after{ setText(sb.toString()); }:
//        '\'' (c=~('\'') { sb.appendCodePoint(c);} | '\'' '\'' { sb.appendCodePoint('\''); })* '\''
//    ;

QUOTED_LITERAL
    @init{
        StringBuilder sb = new StringBuilder();
        String initialQuotation = "'";
        String finalQuotation = "'";
    }
    @after{
        setText(initialQuotation+sb.toString()+finalQuotation);
    }:
        ('"' { initialQuotation = "\""; } |'\'') (c=~('"'|'\'') { sb.appendCodePoint(c);} | '\'' '\'' { sb.appendCodePoint('\''); })* ('"' { finalQuotation = "\""; } |'\'')
    ;

JSON
    @init{
        StringBuilder sb = new StringBuilder();
    }
    @after{
        setText("{" + sb.toString() + "}");
    }:
    T_START_SBRACKET (c=~(';') {sb.appendCodePoint(c);})* T_END_SBRACKET
    ;

T_CONSTANT: (DIGIT)+;

T_IDENT: LETTER (LETTER | DIGIT | '_')*;

//T_KS_AND_TN: LETTER (LETTER | DIGIT | '_')* (POINT LETTER (LETTER | DIGIT | '_')*)?;
T_KS_AND_TN: T_IDENT (POINT T_IDENT)?;

//T_CTLG_TBL_COL: LETTER (LETTER | DIGIT | '_')* (POINT LETTER (LETTER | DIGIT | '_')*)? (POINT LETTER (LETTER | DIGIT | '_')*)?;
T_CTLG_TBL_COL: T_IDENT (POINT T_IDENT (POINT T_IDENT)?)?;

T_TERM: (LETTER | DIGIT | '_' | POINT)+;

T_FLOAT:   ('0'..'9')+ POINT ('0'..'9')* EXPONENT?
     |   POINT ('0'..'9')+ EXPONENT?
     |   ('0'..'9')+ EXPONENT
     ;

T_PATH: (LETTER | DIGIT | '_' | POINT | '-' | '/')+;

//READ_ANY
//    @init{
//        StringBuilder sb = new StringBuilder();
//    }
//    @after{
//        setText(sb.toString());
//    }:
//        (c=(.) {sb.appendCodePoint(c);})+
//    ;

// ========================================================
// CLUSTER
// ========================================================

createClusterStatement returns [CreateClusterStatement ccs]
    @init{
        boolean ifNotExists = false;
    }
    @after{
        ccs = new CreateClusterStatement(
            $clusterName.text,
            ifNotExists,
            $datastoreName.text,
            $j.text);
    }
    :
    T_CREATE T_CLUSTER
    (T_IF T_NOT T_EXISTS {ifNotExists = true;})?
    clusterName=T_IDENT
    T_ON T_DATASTORE datastoreName=QUOTED_LITERAL
    T_WITH T_OPTIONS
    j=JSON
    ;

dropClusterStatement returns [DropClusterStatement dcs]
    @after{
        dcs = new DropClusterStatement($clusterName.text);
    }
    :
    T_DROP T_CLUSTER
    clusterName=T_IDENT
    ;

alterClusterStatement returns [AlterClusterStatement acs]
    @after{
        acs = new AlterClusterStatement($clusterName.text, $j.text);
    }
    :
    T_ALTER T_CLUSTER
    clusterName=T_IDENT
    T_WITH T_OPTIONS
    j=JSON
    ;

// ========================================================
// CATALOG
// ========================================================

createCatalogStatement returns [CreateCatalogStatement crksst]
    @init{
        boolean ifNotExists = false;
    }
    :
    T_CREATE T_CATALOG
    (T_IF T_NOT T_EXISTS {ifNotExists = true;})?
    catalogName=T_IDENT
    (T_WITH j=JSON)?
    { $crksst = new CreateCatalogStatement($catalogName.text, ifNotExists, $j.text); }
    ;

dropCatalogStatement returns [DropCatalogStatement drksst]
    @init{
        boolean ifExists = false;
    }
    :
    T_DROP T_CATALOG
    (T_IF T_EXISTS {ifExists = true;})?
    catalogName=T_IDENT
    { $drksst = new DropCatalogStatement($catalogName.text, ifExists);}
    ;

alterCatalogStatement returns [AlterCatalogStatement alksst]
    :
    T_ALTER T_CATALOG
    catalogName=T_IDENT
    T_WITH j=JSON
    { $alksst = new AlterCatalogStatement($catalogName.text, $j.text); };

// ========================================================
// TABLE
// ========================================================


//STATEMENTS

describeStatement returns [DescribeStatement descs]:
    T_DESCRIBE (
        T_CATALOG {$descs = new DescribeStatement(DescribeType.CATALOG);} (catalog=T_IDENT { $descs.setCatalog($catalog.text);})?
    	| T_CATALOGS {$descs = new DescribeStatement(DescribeType.CATALOGS);}
        | T_TABLE tableName=getTable { $descs = new DescribeStatement(DescribeType.TABLE); $descs.setTableName(tableName);}
        | T_TABLES {$descs = new DescribeStatement(DescribeType.TABLES);}
    )
;

//DELETE (col1, col2) FROM table1 WHERE field1=value1 AND field2=value2;
deleteStatement returns [DeleteStatement ds]
	@init{
		$ds = new DeleteStatement();
	}:
	T_DELETE
	(
        T_START_PARENTHESIS
        firstField=getField {$ds.addColumn(firstField);}
		(T_COMMA field=getField {$ds.addColumn(field);})*
        T_END_PARENTHESIS
        )?
	T_FROM
	tableName=getTableID {$ds.setTableName(tableName);}
	T_WHERE
	rel1=getRelation {$ds.addRelation(rel1);} (T_AND relN=getRelation {$ds.addRelation(relN);})*
	;

//ADD \"index_path\";
addStatement returns [AddStatement as]:
	//T_ADD (T_QUOTE | T_SINGLE_QUOTE) name=T_PATH (T_QUOTE | T_SINGLE_QUOTE) {$as = new AddStatement($name.text);}
	//T_ADD T_QUOTE name=T_PATH T_QUOTE {$as = new AddStatement($name.text);}
	T_ADD name=QUOTED_LITERAL {$as = new AddStatement($name.text);}
	;

//LIST ( PROCESS | UDF | TRIGGER) ;
listStatement returns [ListStatement ls]:
	T_LIST (type=getListTypes)
	{
		if($type.text != null){
			$ls = new ListStatement($type.text);
		}else{
			throw new RecognitionException();
		}
	}
	;

//REMOVE UDF \"jar.name\";"
removeUDFStatement returns [RemoveUDFStatement rus]:
	//T_REMOVE 'UDF' (T_QUOTE | T_SINGLE_QUOTE) jar=getTerm {$rus = new RemoveUDFStatement(jar);} (T_QUOTE | T_SINGLE_QUOTE)
	T_REMOVE T_UDF jar=QUOTED_LITERAL {$rus = new RemoveUDFStatement($jar.text);}
	;

//DROP INDEX IF EXISTS index_name;
dropIndexStatement returns [DropIndexStatement dis]
	@init{
		$dis = new DropIndexStatement();
	}:
	T_DROP T_INDEX
	(T_IF T_EXISTS {$dis.setDropIfExists();})?
	name=(T_KS_AND_TN | T_IDENT | T_LUCENE) {$dis.setName($name.text);}
	;


//CREATE HASH INDEX ON table1 (field1, field2);
//CREATE HASH INDEX index1 ON table1 (field1, field2) USING com.company.Index.class;
//CREATE HASH INDEX index1 ON table1 (field1, field2) WITH OPTIONS opt1=val1 AND opt2=val2;
createIndexStatement returns [CreateIndexStatement cis]
	@init{
		$cis = new CreateIndexStatement();
	}:
	T_CREATE {$cis.setIndexType("default");} (indexType=getIndexType {$cis.setIndexType(indexType);})? T_INDEX
	(T_IF T_NOT T_EXISTS {$cis.setCreateIfNotExists();})?
	(name=T_IDENT {$cis.setName($name.text);})?
	T_ON tableName=getTableID {$cis.setTableName(tableName);}
	T_START_PARENTHESIS
        firstField=getField {$cis.addColumn(firstField);}
	(T_COMMA
		field=getField {$cis.addColumn(field);}
	)*
	T_END_PARENTHESIS
	(T_USING usingClass=getTerm {$cis.setUsingClass(usingClass.toString());})?
	(T_WITH T_OPTIONS T_EQUAL T_START_SBRACKET key=getTerm T_COLON value=getTerm {$cis.addOption(key, value);}
		(T_COMMA keyN=getTerm T_COLON valueN=getTerm {$cis.addOption(keyN, valueN);} )* T_END_SBRACKET
	)?
	;
    //identProp1=T_IDENT T_EQUAL valueProp1=getTerm {properties.put($identProp1.text, valueProp1.toString());}
/*
(T_WITH T_OPTIONS T_EQUAL T_START_SBRACKET key=T_IDENT T_COLON value=getTerm {$cis.addOption($key.text, value.toString());}
		(T_AND key=T_IDENT T_COLON value=getTerm {$cis.addOption($key.text, value.toString());} )* T_END_SBRACKET
*/

getField returns [String newField]:
    (unitField=getUnits {$newField = unitField;}
    |fieldToken=(T_IDENT | T_LUCENE | T_KEY) {$newField = $fieldToken.text;})
;

getUnits returns [String newUnit]:
    unitToken=(T_SEC
    | T_SECS
    | T_SECONDS
    | T_MIN
    | T_MINS
    | T_MINUTES
    | T_HOUR
    | T_HOURS
    | T_DAY
    | T_DAYS)
    {$newUnit = $unitToken.text;}
;

updateTableStatement returns [UpdateTableStatement pdtbst]
    @init{
        boolean optsInc = false;
        boolean condsInc = false;
        ArrayList<Option> options = new ArrayList<>();
        ArrayList<Assignation> assignations = new ArrayList<>();
        ArrayList<Relation> whereclauses = new ArrayList<>();
        Map<String, Term<?>> conditions = new HashMap<>();
    }:
    T_UPDATE tablename=getTableID
    (T_USING opt1=getOption {optsInc = true; options.add(opt1);} (T_AND optN=getOption {options.add(optN);})*)?
    T_SET assig1=getAssignment {assignations.add(assig1);} (T_COMMA assigN=getAssignment {assignations.add(assigN);})*
    T_WHERE rel1=getRelation {whereclauses.add(rel1);} (T_AND relN=getRelation {whereclauses.add(relN);})*
    (T_IF id1=T_IDENT T_EQUAL term1=getTerm {condsInc = true; conditions.put($id1.text, term1);}
                    (T_AND idN=T_IDENT T_EQUAL termN=getTerm {conditions.put($idN.text, termN);})*)?
    {
        if(optsInc)
            if(condsInc)
                $pdtbst = new UpdateTableStatement(tablename, options, assignations, whereclauses, conditions);
            else
                $pdtbst = new UpdateTableStatement(tablename, options, assignations, whereclauses);
        else
            if(condsInc)
                $pdtbst = new UpdateTableStatement(tablename, assignations, whereclauses, conditions);
            else
                $pdtbst = new UpdateTableStatement(tablename, assignations, whereclauses);
    }
    ;

stopProcessStatement returns [StopProcessStatement stprst]:
    T_STOP T_PROCESS ident=getProcess { $stprst = new StopProcessStatement(ident); }
    ;

getProcess returns [String procname]:
    processname=(T_PATH | T_IDENT) {$procname = $processname.text;}
;

dropTriggerStatement returns [DropTriggerStatement drtrst]:
    T_DROP
    T_TRIGGER ident=T_IDENT
    T_ON
    ident2=T_IDENT
    {$drtrst = new DropTriggerStatement($ident.text,$ident2.text);}
    ;

createTriggerStatement returns [CreateTriggerStatement crtrst]:
    T_CREATE
    T_TRIGGER trigger_name=T_IDENT
    T_ON
    table_name=T_IDENT
    T_USING class_name=T_IDENT
    {$crtrst = new CreateTriggerStatement($trigger_name.text,$table_name.text,$class_name.text);}
    ;

createTableStatement returns [CreateTableStatement crtast]
@init{
    LinkedHashMap<String, String> columns = new LinkedHashMap<>();
    ArrayList<String>   primaryKey = new ArrayList<String>();
    ArrayList<String> clusterKey = new ArrayList<String>();
    int primaryKeyType = 0;
    int columnNumberPK= 0;
    int columnNumberPK_inter= 0;
    boolean ifNotExists = false;
    boolean withProperties = false;
    }:
    T_CREATE
    T_TABLE
    (T_IF T_NOT T_EXISTS {ifNotExists = true;})?
    tablename=getTableID
    T_START_PARENTHESIS (
                ident_column1=getField type1=getDataType (T_PRIMARY T_KEY)? {columns.put(ident_column1, type1); primaryKeyType=1;}
                (
                    ( T_COMMA ident_columN=getField typeN=getDataType (T_PRIMARY T_KEY {primaryKeyType=1;columnNumberPK=columnNumberPK_inter +1;})? {columns.put(ident_columN, typeN);columnNumberPK_inter+=1;})
                    |(
                        T_COMMA T_PRIMARY T_KEY T_START_PARENTHESIS
                        (
                            (   primaryK=getField {primaryKey.add(primaryK);primaryKeyType=2;}

                                (T_COMMA partitionKN=getField {primaryKey.add(partitionKN);})*
                            )
                            |(
                                T_START_PARENTHESIS partitionK=getField {primaryKey.add(partitionK); primaryKeyType=3;}
                                    (T_COMMA partitionKN=getField {primaryKey.add(partitionKN);})*
                                T_END_PARENTHESIS
                                (T_COMMA clusterKN=getField {clusterKey.add(clusterKN);})*

                            )
                        )
                       T_END_PARENTHESIS
                   )
                )*
         )
    T_END_PARENTHESIS (T_WITH {withProperties=true;} properties=getMetaProperties)?
    {
        $crtast = new CreateTableStatement(tablename, columns, primaryKey, clusterKey, primaryKeyType, columnNumberPK);
        $crtast.setProperties(properties);
        $crtast.setIfNotExists(ifNotExists);
        $crtast.setWithProperties(withProperties);
    }
;

alterTableStatement returns [AlterTableStatement altast]
@init{
        int option= 0;
    }:
    T_ALTER
    T_TABLE
    //tablename=getTableID
    tableName = getTable
    (T_ALTER column=getField T_TYPE type=T_IDENT {option=1;}
        |T_ADD column=getField type=T_IDENT {option=2;}
        |T_DROP column=getField {option=3;}
        |(T_WITH {option=4;} props=getMetaProperties)?
    )
    {$altast = new AlterTableStatement(tableName, column, $type.text, props, option);  }
;

selectStatement returns [SelectStatement slctst]
    @init{
        boolean windowInc = false;
        boolean joinInc = false;
        boolean whereInc = false;
        boolean orderInc = false;
        boolean groupInc = false;
        boolean limitInc = false;
        boolean disable = false;
        Map fieldsAliasesMap = new HashMap<String, String>();
        Map tablesAliasesMap = new HashMap<String, String>();
        MutablePair<String, String> pair = new MutablePair<>();
    }:
    T_SELECT selClause=getSelectClause[fieldsAliasesMap] T_FROM tablename=getAliasedTableID[tablesAliasesMap]
    (T_WITH T_WINDOW {windowInc = true;} window=getWindow)?
    (T_INNER T_JOIN { joinInc = true;} identJoin=getAliasedTableID[tablesAliasesMap] T_ON getFields[pair])?
    (T_WHERE {whereInc = true;} whereClauses=getWhereClauses)?
    (T_ORDER T_BY {orderInc = true;} ordering=getOrdering)?
    (T_GROUP T_BY {groupInc = true;} groupby=getGroupBy)?
    (T_LIMIT {limitInc = true;} constant=getConstant)?
    (T_DISABLE T_ANALYTICS {disable = true;})?
    {
        $slctst = new SelectStatement(selClause, tablename);
        if(windowInc)
            $slctst.setWindow(window);
        if(joinInc)
            $slctst.setJoin(new InnerJoin(identJoin, pair.getLeft(), pair.getRight()));
        if(whereInc)
             $slctst.setWhere(whereClauses);
        if(orderInc)
             $slctst.setOrder(ordering);
        if(groupInc)
             $slctst.setGroup(groupby);
        if(limitInc)
             $slctst.setLimit(Integer.parseInt(constant));
        if(disable)
            $slctst.setDisableAnalytics(true);

        $slctst.replaceAliasesWithName(fieldsAliasesMap, tablesAliasesMap);
        $slctst.updateTableNames();
    };

insertIntoStatement returns [InsertIntoStatement nsntst]
    @init{
        ArrayList<String> ids = new ArrayList<>();
        boolean ifNotExists = false;
        int typeValues = InsertIntoStatement.TYPE_VALUES_CLAUSE;
        ArrayList<GenericTerm> cellValues = new ArrayList<>();
        boolean optsInc = false;
        ArrayList<Option> options = new ArrayList<>();
    }:
    T_INSERT
    T_INTO
    tableName=getTableID
    T_START_PARENTHESIS
    ident1=getField {ids.add(ident1);}
    (T_COMMA identN=getField {ids.add(identN);})*
    T_END_PARENTHESIS
    (
        selectStmnt=selectStatement {typeValues = InsertIntoStatement.TYPE_SELECT_CLAUSE;}
        |
        T_VALUES
        T_START_PARENTHESIS
            term1=getTermOrLiteral {cellValues.add(term1);}
            (T_COMMA termN=getTermOrLiteral {cellValues.add(termN);})*
        T_END_PARENTHESIS
    )
    (T_IF T_NOT T_EXISTS {ifNotExists=true;} )?
    (
        T_USING {optsInc=true;}
        opt1=getOption {
            options.add(opt1);
        }
        (T_AND optN=getOption {options.add(optN);})*
    )?
    {
        if(typeValues==InsertIntoStatement.TYPE_SELECT_CLAUSE)
            if(optsInc)
                $nsntst = new InsertIntoStatement(tableName, ids, selectStmnt, ifNotExists, options);
            else
                $nsntst = new InsertIntoStatement(tableName, ids, selectStmnt, ifNotExists);
        else
            if(optsInc)
                $nsntst = new InsertIntoStatement(tableName, ids, cellValues, ifNotExists, options);
            else
                $nsntst = new InsertIntoStatement(tableName, ids, cellValues, ifNotExists);

    }
    ;

explainPlanStatement returns [ExplainPlanStatement xpplst]:
    T_EXPLAIN T_PLAN T_FOR parsedStmnt=metaStatement
    {$xpplst = new ExplainPlanStatement(parsedStmnt);}
    ;


dropTableStatement returns [DropTableStatement drtbst]
    @init{
        boolean ifExists = false;
    }:
    T_DROP
    T_TABLE
    (T_IF T_EXISTS { ifExists = true; })?
    identID=getTableID {
        $drtbst = new DropTableStatement(identID, ifExists);
    };

truncateStatement returns [TruncateStatement trst]:
	T_TRUNCATE
        ident=getTableID {
            $trst = new TruncateStatement(ident);
	};

metaStatement returns [MetaStatement st]:
    st_nsnt   = insertIntoStatement { $st = st_nsnt;}
    | st_slct = selectStatement { $st = st_slct;}
    | st_crta = createTableStatement { $st = st_crta;}
    | st_alta = alterTableStatement { $st = st_alta;}
    | st_pdtb = updateTableStatement { $st = st_pdtb; }
    | st_tbdr = dropTableStatement { $st = st_tbdr; }
    | st_trst = truncateStatement { $st = st_trst; }
    | ls = listStatement { $st = ls; }
    | st_stpr = stopProcessStatement { $st = st_stpr; }
    | st_xppl = explainPlanStatement { $st = st_xppl;}
    | add = addStatement { $st = add; }
    | rs = removeUDFStatement { $st = rs; }
    | ds = deleteStatement { $st = ds; }
    | descs = describeStatement { $st = descs;}
    | st_crks = createCatalogStatement { $st = st_crks; }
    | st_alks = alterCatalogStatement { $st = st_alks; }
    | st_drks = dropCatalogStatement { $st = st_drks ;}
    | ccs = createClusterStatement { $st = ccs;}
    | dcs = dropClusterStatement {$st = dcs;}
    | acs = alterClusterStatement {$st = acs;}
    | cis = createIndexStatement { $st = cis; }
    | dis = dropIndexStatement { $st = dis; }
    | st_crtr = createTriggerStatement { $st = st_crtr; }
    | st_drtr = dropTriggerStatement { $st = st_drtr; }
    ;

query returns [MetaStatement st]:
	mtst=metaStatement (T_SEMICOLON)+ EOF {
		$st = mtst;
	};


//FUNCTIONS
getIndexType returns [String indexType]:
    ( idxType=T_DEFAULT
    | idxType=T_LUCENE
    | idxType=T_CUSTOM)
    {$indexType=$idxType.text;}
;

getMetaProperties returns [ArrayList<Property> props]
    @init{
        $props = new ArrayList<>();
    }:
    firstProp=getMetaProperty {$props.add(firstProp);}
    (T_AND newProp=getMetaProperty {$props.add(newProp);})*
;

getMetaProperty returns [Property mp]
    @init{
        BooleanTerm boolProp = new BooleanTerm("true");
    }:
    (identProp=T_IDENT T_EQUAL valueProp=getTerm {$mp = new PropertyNameValue($identProp.text, new StringTerm(valueProp.toString()));}
    | T_COMPACT T_STORAGE {$mp = new PropertyCompactStorage();}
    | T_CLUSTERING T_ORDER T_BY T_START_PARENTHESIS ordering=getOrdering {$mp = new PropertyClusteringOrder(ordering);} T_END_PARENTHESIS)
    | T_EPHEMERAL ( | T_EQUAL (T_FALSE { boolProp = new BooleanTerm("false");} | T_TRUE )) {$mp = new PropertyNameValue("ephemeral", boolProp);}
;

getDataType returns [String dataType]:
    (
        ident1=T_IDENT (T_LT ident2=T_IDENT (T_COMMA ident3=T_IDENT)? T_GT)?
    )
    {$dataType = $ident1.text.concat(ident2==null?"":"<"+$ident2.text).concat(ident3==null?"":","+$ident3.text).concat(ident2==null?"":">");}
;

getOrdering returns [ArrayList<Ordering> order]
    @init{
        order = new ArrayList<>();
        Ordering ordering;
    }:
    ident1=(T_KS_AND_TN | T_IDENT) {ordering = new Ordering($ident1.text);} (T_ASC {ordering.setOrderDir(OrderDirection.ASC);} | T_DESC {ordering.setOrderDir(OrderDirection.DESC);})? {order.add(ordering);}
    (T_COMMA identN=(T_KS_AND_TN | T_IDENT) {ordering = new Ordering($identN.text);} (T_ASC {ordering.setOrderDir(OrderDirection.ASC);} | T_DESC {ordering.setOrderDir(OrderDirection.DESC);})? {order.add(ordering);})*
;

getGroupBy returns [ArrayList<GroupBy> groups]
    @init{
        groups = new ArrayList<>();
        GroupBy groupBy;
    }:
    ident1=(T_KS_AND_TN | T_IDENT) {groupBy = new GroupBy($ident1.text); groups.add(groupBy);}
    (T_COMMA identN=(T_KS_AND_TN | T_IDENT) {groupBy = new GroupBy($identN.text); groups.add(groupBy);})*
;

getWhereClauses returns [ArrayList<Relation> clauses]
    @init{
        clauses = new ArrayList<>();
    }:
    rel1=getRelation {clauses.add(rel1);} (T_AND relN=getRelation {clauses.add(relN);})*
;

getFields[MutablePair pair]:
    ident1L=getTableID { pair.setLeft(ident1L); } T_EQUAL ident1R=getTableID { pair.setRight(ident1R); }
    | T_START_PARENTHESIS ident1L=getTableID { pair.setLeft(ident1L); } T_EQUAL ident1R=getTableID { pair.setRight(ident1R); } T_END_PARENTHESIS
;

getWindow returns [Window ws]:
    (T_LAST {$ws = new Window(WindowType.LAST);}
    | cnstnt=getConstant (T_ROWS {$ws = new Window(WindowType.NUM_ROWS); $ws.setNumRows(Integer.parseInt(cnstnt));}
                       | unit=getTimeUnit {$ws = new Window(WindowType.TEMPORAL); $ws.setTimeWindow(Integer.parseInt(cnstnt), unit);}
                       )
    );

getTimeUnit returns [TimeUnit unit]:
    ( T_SEC {$unit=TimeUnit.SECONDS;}
    | T_SECS {$unit=TimeUnit.SECONDS;}
    | T_SECONDS {$unit=TimeUnit.SECONDS;}
    | T_MIN {$unit=TimeUnit.MINUTES;}
    | T_MINS {$unit=TimeUnit.MINUTES;}
    | T_MINUTES {$unit=TimeUnit.MINUTES;}
    | T_HOUR {$unit=TimeUnit.HOURS;}
    | T_HOURS {$unit=TimeUnit.HOURS;}
    | T_DAY {$unit=TimeUnit.DAYS;}
    | T_DAYS {$unit=TimeUnit.DAYS;}
    )
;

getSelectClause[Map fieldsAliasesMap] returns [SelectionClause sc]:
    scc=getSelectionCount {$sc = scc;}
    | scl=getSelectionList[fieldsAliasesMap] {$sc = scl;}
;

getSelectionCount returns [SelectionCount scc]
    @init{
        boolean identInc = false;
        char symbol = '*';
    }:
    T_COUNT T_START_PARENTHESIS symbolStr=getCountSymbol { symbol=symbolStr.charAt(0); } T_END_PARENTHESIS
    (T_AS {identInc = true;} ident=T_IDENT )?
    {
        if(identInc)
            $scc = new SelectionCount(symbol, identInc, $ident.text);
        else
            $scc = new SelectionCount(symbol);
    }
;

getCountSymbol returns [String str]:
    '*' {$str = new String("*");}
    | '1' {$str = new String("1");}
    ;

getSelectionList[Map fieldsAliasesMap] returns [SelectionList scl]
    @init{
        boolean distinct = false;
    }:
    (T_DISTINCT {distinct = true;})? selections=getSelection[fieldsAliasesMap]
    { $scl = new SelectionList(distinct, selections);}
;

getSelection[Map fieldsAliasesMap] returns [Selection slct]
    @init{
        SelectionSelector slsl;
        ArrayList<SelectionSelector> selections = new ArrayList<>();
    }:
    (
        T_ASTERISK { $slct = new SelectionAsterisk();}
        | selector1=getSelector { slsl = new SelectionSelector(selector1);} (T_AS alias1=getAlias {slsl.setAlias($alias1.text); fieldsAliasesMap.put($alias1.text, selector1.toString());})? {selections.add(slsl);}
            (T_COMMA selectorN=getSelector {slsl = new SelectionSelector(selectorN);} (T_AS aliasN=getAlias {slsl.setAlias($aliasN.text); fieldsAliasesMap.put($aliasN.text, selectorN.toString());})? {selections.add(slsl);})*
            { $slct = new SelectionSelectors(selections);}
    )
;

getAlias returns [String alias]:
	ident=T_IDENT {$alias=$ident.text;}
;


getSelector returns [SelectorMeta slmt]
    @init{
        ArrayList<SelectorMeta> params = new ArrayList<>();
        GroupByFunction gbFunc = null;
    }:
    ( (T_SUM {gbFunc = GroupByFunction.SUM;}
       | T_MAX {gbFunc = GroupByFunction.MAX;}
       | T_MIN {gbFunc = GroupByFunction.MIN;}
       | T_AVG {gbFunc = GroupByFunction.AVG;}
       | T_COUNT {gbFunc = GroupByFunction.COUNT;}
      )
            T_START_PARENTHESIS
                (select1=getSelector {params.add(select1);}
                | T_ASTERISK {params.add(new SelectorIdentifier("*"));}
                )?
            T_END_PARENTHESIS {$slmt = new SelectorGroupBy(gbFunc, params.get(0));}
        | (identID=getTableID | luceneID=T_LUCENE) (
            {if (identID != null) $slmt = new SelectorIdentifier(identID); else $slmt = new SelectorIdentifier($luceneID.text);}
            | T_START_PARENTHESIS (select1=getSelector {params.add(select1);} (T_COMMA selectN=getSelector {params.add(selectN);})*)?
                T_END_PARENTHESIS {$slmt = new SelectorFunction(identID, params);}
        )
    )
;

getListTypes returns [String listType]:
	//ident=('PROCESS' | 'UDF' | 'TRIGGER' | 'process' | 'udf' | 'trigger') {$listType = new String($ident.text);}
	//ident=('PROCESS' | 'UDF' | 'TRIGGER') {$listType = new String($ident.text);}
	//ident=(T_PROCESS | 'UDF' | 'TRIGGER') {$listType = new String($ident.text);}
	ident=(T_PROCESS | T_UDF | T_TRIGGER) {$listType = new String($ident.text);}
	;

getAssignment returns [Assignation assign]:
    ident=T_KS_AND_TN (
        T_EQUAL value=getValueAssign {$assign = new Assignment(new ColumnName($ident.text), Operator.ASSIGN, value);}
        | T_START_BRACKET indexTerm=getTerm T_END_BRACKET T_EQUAL termValue=getTerm {
            $assign = new Assignation (new ColumnName($ident.text, indexTerm), Operator.ASSIGN, termValue);
        }
    )
;

getValueAssign returns [GenericTerm valueAssign]:
    term1=getTerm { $valueAssign = new ValueAssignment(term1);}
    | ident=T_IDENT (T_PLUS (T_START_SBRACKET mapLiteral=getMapLiteral T_END_SBRACKET { $valueAssign = new ValueAssignment(new IdentMap($ident.text, mapLiteral));}
                             | value1=getIntSetOrList {
                                                        if(value1 instanceof IntTerm)
                                                            $valueAssign = new ValueAssignment(new IntTerm($ident.text, '+', ((IntTerm) value1).getTerm()));
                                                        else if(value1 instanceof ListLiteral)
                                                            $valueAssign = new ValueAssignment(new ListLiteral($ident.text, '+', ((ListLiteral) value1).getLiterals()));
                                                        else
                                                            $valueAssign = new ValueAssignment(new SetLiteral($ident.text, '+', ((SetLiteral) value1).getLiterals()));
                                                       }
                           )
                    | T_SUBTRACT value2=getIntSetOrList {
                                                if(value2 instanceof IntTerm)
                                                    $valueAssign = new ValueAssignment(new IntTerm($ident.text, '-', ((IntTerm) value2).getTerm()));
                                                else if(value2 instanceof ListLiteral)
                                                    $valueAssign = new ValueAssignment(new ListLiteral($ident.text, '-', ((ListLiteral) value2).getLiterals()));
                                                else
                                                    $valueAssign = new ValueAssignment(new SetLiteral($ident.text, '-', ((SetLiteral) value2).getLiterals()));
                                                }
                )
;

getIntSetOrList returns [IdentIntOrLiteral iiol]
    @init{
    }:
    constant=getConstant { $iiol = new IntTerm(Integer.parseInt(constant));}
    | T_START_BRACKET list=getList T_END_BRACKET { $iiol = new ListLiteral(list);}
    | T_START_SBRACKET set=getSet T_END_SBRACKET { $iiol = new SetLiteral(set);}
;

getList returns [ArrayList list]
    @init{
        list = new ArrayList<String>();
    }:
    term1=getTerm {list.add(term1.toString());}
    (T_COMMA termN=getTerm {list.add(termN.toString());})*
    ;

getMapLiteral returns [Map<String, Term> mapTerms]
    @init{
        $mapTerms = new HashMap<>();
    }:
    (leftTerm1=getTerm T_COLON rightTerm1=getTerm {$mapTerms.put(leftTerm1.toString(), rightTerm1);}
    (T_COMMA leftTermN=getTerm T_COLON rightTermN=getTerm {$mapTerms.put(leftTermN.toString(), rightTermN);})*)?
    ;

getSet returns [Set set]
    @init{
        set = new HashSet<String>();
    }:
    term1=getTerm {set.add(term1.toString());}
    (T_COMMA termN=getTerm {set.add(termN.toString());})*
    ;

getRelation returns [Relation mrel]:
    T_TOKEN T_START_PARENTHESIS listIds=getIds T_END_PARENTHESIS operator=getComparator (term=getTerm {$mrel = new RelationToken(listIds, operator, term);}
                            | T_TOKEN T_START_PARENTHESIS terms=getTerms T_END_PARENTHESIS {$mrel = new RelationToken(listIds, operator, terms);})
    | (ident=T_IDENT | ident=T_KS_AND_TN) ( compSymbol=getComparator termR=getTerm {$mrel = new RelationCompare($ident.text, compSymbol, termR);}
                    | T_IN T_START_PARENTHESIS terms=getTerms T_END_PARENTHESIS {$mrel = new RelationIn($ident.text, terms);}
                    | T_BETWEEN term1=getTerm T_AND term2=getTerm {$mrel = new RelationBetween($ident.text, term1, term2);}
                    )
;

getComparator returns [String comparator]:
    T_EQUAL {$comparator="=";}
    | T_GT {$comparator=">";}
    | T_LT {$comparator="<";}
    | T_GTE {$comparator=">=";}
    | T_LTE {$comparator="<=";}
    | T_NOT_EQUAL {$comparator="<>";}
    | T_LIKE {$comparator="LIKE";}
    | T_MATCH {$comparator="MATCH";}
;

getIds returns [ArrayList<String> listStrs]
    @init{
        listStrs = new ArrayList<>();
    }:
    ident1=T_IDENT {listStrs.add($ident1.text);} (T_COMMA identN=T_IDENT {listStrs.add($identN.text);})*
;

getOptions returns [ArrayList<Option> opts]@init{
        opts = new ArrayList<>();
    }:
    opt1=getOption {opts.add(opt1);} (optN=getOption {opts.add(optN);})*
;

getOption returns [Option opt]:
    T_COMPACT T_STORAGE {$opt=new Option(Option.OPTION_COMPACT);}
    | T_CLUSTERING T_ORDER {$opt=new Option(Option.OPTION_CLUSTERING);}
    | identProp=T_IDENT T_EQUAL valueProp=getTerm {$opt=new Option($identProp.text, valueProp);}
;

getTerms returns [ArrayList list]
    @init{
        list = new ArrayList<Term>();
    }:
    term1=getTerm {list.add(term1);}
    (T_COMMA termN=getTerm {list.add(termN);})*
    ;

getTermOrLiteral returns [GenericTerm vc]
    @init{
        ListTerms cl = new ListTerms();
    }:
    term=getTerm {$vc=term;}
    |
    T_START_SBRACKET
    (
        term1=getTerm {cl.addTerm(term1);}
        (T_COMMA termN=getTerm {cl.addTerm(termN);})*
    )?
    T_END_SBRACKET {$vc=cl;}
;

getAliasedTableID[Map tablesAliasesMap] returns [String tableID]:
	(ident1=T_IDENT {$tableID = new String($ident1.text);}
    | ident2=T_KS_AND_TN {$tableID = new String($ident2.text);}) (alias=T_IDENT {tablesAliasesMap.put($alias.text, $tableID);})?
    ;

getTableID returns [String tableID]:
    (ident1=T_IDENT {$tableID = new String($ident1.text);}
    | ident2=T_KS_AND_TN {$tableID = new String($ident2.text);})
    ;

getGenericTerm [GenericTerm genericTerm]:
    genericTerm=getCollectionTerms
    | genericTerm=getTerm
;

getCollectionTerms [CollectionTerms collectionTerms]:
    collectionTerms=getListTerms
    | collectionTerms=getSetTerms
    | collectionTerms=getMapTerms
;

getListTerms [ListTerms listTerms]
    @init{
        listTerms = new LisTerms();
    }:
    T_START_SBRACKET (value=getTerm {lisTerms.addTerm(value);})* T_END_SBRACKET
;

getSetTerms [SetTerms setTerms]
    @init{
        setTerms = new SetTerms();
    }:
    T_START_BRACKET (value=getTerm {setTerms.addTerm(value);})* T_END_BRACKET
;

getMapTerms [MapTerms mapTerms]
    @init{
        mapTerms = new MapTerms();
    }:
    T_START_SBRACKET (key=getTerm T_COLON value=getTerm {mapTerms.addTerm(key, value);})* T_END_SBRACKET
;

getTerm returns [Term term]:
    term1=getPartialTerm ( {$term = term1;} |
    T_AT term2=getPartialTerm {$term = new StringTerm(term1.getTermValue()+"@"+term2.getTermValue());} )
;

getPartialTerm returns [Term term]:
    ident=T_IDENT {$term = new StringTerm($ident.text);}
    | constant=getConstant {$term = new LongTerm(constant);}
    | T_FALSE {$term = new BooleanTerm("false");}
    | T_TRUE {$term = new BooleanTerm("true");}
    | floatingNumber=T_FLOAT {$term = new DoubleTerm($floatingNumber.text);}
    | ksAndTn=T_KS_AND_TN {$term = new StringTerm($ksAndTn.text);}
    | noIdent=T_TERM {$term = new StringTerm($noIdent.text);}
    | path=T_PATH {$term = new StringTerm($path.text);}
    | qLiteral=QUOTED_LITERAL {$term = new StringTerm($qLiteral.text);}
;

getConstant returns [String constStr]:
    constToken=T_CONSTANT {$constStr = new String($constToken.text);}
    | '1' {$constStr = new String("1");}
    ;

getFloat returns [String floating]:
    termToken=T_TERM {$floating=$termToken.text;}
    | floatToken = T_FLOAT {$floating=$floatToken.text;}
    ;

getColumn returns [ColumnName column]
    @init{
       String t1 = null;
       String t2 = null;
       String t3 = null;
    }
    @after{
        String columnName = t1;
        if(t2 != null){
            columnName = t2;
        }else if(t3 != null){
            columnName = t3;
        }

        String [] columnTokens = columnName.split("\\.");
        if(columnTokens.length == 1){
            column = new ColumnName(columnTokens[0]);
        }else if(columnTokens.length == 2){
            column = new ColumnName(columnTokens[1]);
            column.setTable(columnTokens[0]);
        }else{
            column = new ColumnName(columnTokens[2]);
            column.setTable(columnTokens[1]);
            column.setCatalog(columnTokens[0]);
        }
    }:
    (ident1=T_IDENT {t1 = $ident1.text;}
    | ident2=T_KS_AND_TN {t2 = $ident2.text;}
    | ident3=T_CTLG_TBL_COL {t3 = $ident3.text;})
    ;

getTable returns [TableName table]
    @init{
       String t1 = null;
       String t2 = null;
    }
    @after{
        String tableName = t1;
        if(t2 != null){
            tableName = t2;
        }

        String [] tableTokens = tableName.split("\\.");
        if(tableTokens.length == 2){
         table = new TableName(tableTokens[1]);
         table.setCatalog(tableTokens[0]);
        }else{
         table = new TableName(tableName);
        }

    }:
    (ident1=T_IDENT {t1 = $ident1.text;}
    | ident2=T_KS_AND_TN {t2 = $ident2.text;})
    ;

// ===================================================

WS: (' ' | '\t' | '\n' | '\r')+ { 
        $channel = HIDDEN; 
    };
