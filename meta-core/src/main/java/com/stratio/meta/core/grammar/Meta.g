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
    import com.stratio.meta2.common.data.*;
    import com.stratio.meta2.common.statements.structures.selectors.*;
    import com.stratio.meta.common.statements.structures.relationships.*;
    import com.stratio.meta.common.statements.structures.window.*;
    import com.stratio.meta2.common.statements.structures.selectors.*;
    import com.stratio.meta.common.statements.structures.selectors.*;
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

    private String sessionCatalog = "";

    public String getEffectiveCatalog(CatalogName cn) {
        return ((cn != null) && (!cn.getName().isEmpty()))? cn.getName(): sessionCatalog;
    }

    public String getEffectiveCatalog(TableName tn) {
        return ((tn != null) && (tn.getCatalogName()!=null) && (!tn.getCatalogName().getName().isEmpty()))? tn.getCatalogName().getName(): sessionCatalog;
    }

    public TableName getEffectiveTable(TableName tn) {
        if(tn != null){
            return new TableName(getEffectiveCatalog(tn), tn.getName());
        }
        return null;
    }

    public TableName normalizeTableName(String str){
        if(str.contains(".")){
            String[] idParts = str.split("\\.");
            return new TableName(idParts[0], idParts[1]);
        } else {
            return new TableName(sessionCatalog, str);
        }
    }

    public ColumnName normalizeColumnName(TableName tn, String str){
        String [] columnTokens = str.split("\\.");
        if(columnTokens.length == 1){
            return new ColumnName(getEffectiveTable(tn), columnTokens[0]);
        }else if(columnTokens.length == 2){
            return new ColumnName(getEffectiveCatalog(tn), columnTokens[0], columnTokens[1]);
        }else{
            return new ColumnName(columnTokens[0], columnTokens[1], columnTokens[2]);
        }
    }

    private ErrorsHelper foundErrors = new ErrorsHelper();

    public ErrorsHelper getFoundErrors(){
        return foundErrors;
    }

    @Override
    public void displayRecognitionError(String[] tokenNames, RecognitionException e){        
        String hdr = getErrorHeader(e);
        String msg = getErrorMessage(e, tokenNames);
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
T_ATTACH: A T T A C H;
T_DETACH: D E T A C H;
T_TO: T O;

fragment LETTER: ('A'..'Z' | 'a'..'z');
fragment DIGIT: '0'..'9';

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

T_CONSTANT: (DIGIT)+;

T_IDENT: LETTER (LETTER | DIGIT | '_')*;

//T_KS_AND_TN: LETTER (LETTER | DIGIT | '_')* (POINT LETTER (LETTER | DIGIT | '_')*)?;
T_KS_AND_TN: T_IDENT (POINT T_IDENT)?;

//T_CTLG_TBL_COL: LETTER (LETTER | DIGIT | '_')* (POINT LETTER (LETTER | DIGIT | '_')*)? (POINT LETTER (LETTER | DIGIT | '_')*)?;
T_CTLG_TBL_COL: T_IDENT (POINT T_IDENT (POINT T_IDENT)?)?;

T_FLOAT:   ('0'..'9')+ POINT ('0'..'9')* EXPONENT?
     |   POINT ('0'..'9')+ EXPONENT?
     |   ('0'..'9')+ EXPONENT
     ;

T_TERM: (LETTER | DIGIT | '_' | POINT)+;

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

attachClusterStatement returns [AttachClusterStatement acs]
    @init{
        boolean ifNotExists = false;
    }
    @after{
        acs = new AttachClusterStatement(
            $clusterName.text,
            ifNotExists,
            $dataStoreName.text,
            j);
    }:
    T_ATTACH T_CLUSTER
    (T_IF T_NOT T_EXISTS {ifNotExists = true;})?
    clusterName=T_IDENT
    T_ON T_DATASTORE dataStoreName=QUOTED_LITERAL
    T_WITH T_OPTIONS
    j=getJson
;

detachClusterStatement returns [DetachClusterStatement dcs]
    @after{
        dcs = new DetachClusterStatement($clusterName.text);
    }:
    T_DETACH T_CLUSTER clusterName=T_IDENT
;

alterClusterStatement returns [AlterClusterStatement acs]
    @after{
        acs = new AlterClusterStatement($clusterName.text, j);
    }:
    T_ALTER T_CLUSTER clusterName=T_IDENT T_WITH T_OPTIONS j=getJson
;

// ========================================================
// CONNECTOR
// ========================================================

attachConnectorStatement returns [AttachConnectorStatement acs]
    @after{
        $acs = new AttachConnectorStatement($connectorName.text, $clusterName.text, optionsJson);
    }:
    T_ATTACH T_CONNECTOR connectorName=QUOTED_LITERAL T_TO clusterName=QUOTED_LITERAL T_WITH T_OPTIONS optionsJson=getJson
;

detachConnectorStatement returns [DetachConnectorStatement dcs]
    @after{
        $dcs = new DetachConnectorStatement(new ConnectorName($connectorName.text), new ClusterName($clusterName.text));
    }:
    T_DETACH T_CONNECTOR connectorName=T_IDENT T_FROM clusterName=T_IDENT
;

// ========================================================
// CATALOG
// ========================================================

createCatalogStatement returns [CreateCatalogStatement crctst]
    @init{
        boolean ifNotExists = false;
    }:
    T_CREATE T_CATALOG
    (T_IF T_NOT T_EXISTS {ifNotExists = true;})?
    catalogName=T_IDENT
    (T_WITH j=getJson)?
    { $crctst = new CreateCatalogStatement($catalogName.text, ifNotExists, j); }
;

dropCatalogStatement returns [DropCatalogStatement drcrst]
    @init{
        boolean ifExists = false;
    }:
    T_DROP T_CATALOG
    (T_IF T_EXISTS {ifExists = true;})?
    catalogName=T_IDENT
    { $drcrst = new DropCatalogStatement($catalogName.text, ifExists);}
;

alterCatalogStatement returns [AlterCatalogStatement alctst]:
    T_ALTER T_CATALOG
    catalogName=T_IDENT
    T_WITH j=getJson
    { $alctst = new AlterCatalogStatement($catalogName.text, j); }
;

// ========================================================
// TABLE
// ========================================================


//STATEMENTS

describeStatement returns [DescribeStatement descs]:
    T_DESCRIBE (
        T_CATALOG {$descs = new DescribeStatement(DescribeType.CATALOG);} (catalog=T_IDENT { $descs.setCatalog($catalog.text);})?
    	| T_CATALOGS {$descs = new DescribeStatement(DescribeType.CATALOGS);}
        | T_TABLE tablename=getTable { $descs = new DescribeStatement(DescribeType.TABLE); $descs.setTableName(tablename);}
        | T_TABLES {$descs = new DescribeStatement(DescribeType.TABLES);}
    )
;

//DELETE FROM table1 WHERE field1=value1 AND field2=value2;
deleteStatement returns [DeleteStatement ds]
	@after{
		$ds = new DeleteStatement(tablename, whereClauses);
	}:
	T_DELETE T_FROM tablename=getTableName
	T_WHERE whereClauses=getWhereClauses[tablename]
	//T_WHERE rel1=getRelation[tablename] {$ds.addRelation(rel1);} (T_AND relN=getRelation[tablename] {$ds.addRelation(relN);})*
;

//ADD \"index_path\";
addStatement returns [AddStatement as]:
	//T_ADD (T_QUOTE | T_SINGLE_QUOTE) name=T_PATH (T_QUOTE | T_SINGLE_QUOTE) {$as = new AddStatement($name.text);}
	//T_ADD T_QUOTE name=T_PATH T_QUOTE {$as = new AddStatement($name.text);}
	T_ADD name=QUOTED_LITERAL {$as = new AddStatement($name.text);}
;

//DROP (DATASTORE | CONNECTOR) \"name\";
dropManifestStatement returns [MetaStatement dms]
    @init{
        boolean dataStore = true;
    }:
    T_DROP (T_DATASTORE | T_CONNECTOR { dataStore = false; } ) name=T_IDENT
    { if(dataStore)
        $dms = new DropDataStoreStatement($name.text);
      else
        $dms = new DropConnectorStatement($name.text);
    }
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
	//T_REMOVE 'UDF' (T_QUOTE | T_SINGLE_QUOTE) jar=getSelector[null] {$rus = new RemoveUDFStatement(jar);} (T_QUOTE | T_SINGLE_QUOTE)
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
	T_ON tablename=getTableName {$cis.setTableName(tablename);}
	T_START_PARENTHESIS
        firstField=getColumnName[tablename] {$cis.addColumn(firstField);}
	(T_COMMA
		field=getColumnName[tablename] {$cis.addColumn(field);}
	)*
	T_END_PARENTHESIS
	(T_USING usingClass=getSelector[tablename] {$cis.setUsingClass(usingClass.toString());})?
	(T_WITH T_OPTIONS T_EQUAL optionsJson=getJson {$cis.setOptionsJson(optionsJson);})?
;

    //identProp1=T_IDENT T_EQUAL valueProp1=getSelector[tablename] {properties.put($identProp1.text, valueProp1.toString());}
    /*
    (T_WITH T_OPTIONS T_EQUAL T_START_SBRACKET key=T_IDENT T_COLON value=getSelector[tablename] {$cis.addOption($key.text, value.toString());}
            (T_AND key=T_IDENT T_COLON value=getSelector[tablename] {$cis.addOption($key.text, value.toString());} )* T_END_SBRACKET
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
        ArrayList<Relation> assignations = new ArrayList<>();
        Map<Selector, Selector> conditions = new HashMap<>();
    }:
    T_UPDATE tablename=getTableName
    (T_USING opt1=getOption[tablename] {optsInc = true; options.add(opt1);} (T_AND optN=getOption[tablename] {options.add(optN);})*)?
    T_SET assig1=getAssignment[tablename] {assignations.add(assig1);} (T_COMMA assigN=getAssignment[tablename] {assignations.add(assigN);})*
    (T_WHERE whereClauses=getWhereClauses[tablename])?
    (T_IF id1=getSelector[tablename] T_EQUAL term1=getSelector[tablename] {condsInc = true; conditions.put(id1, term1);}
                    (T_AND idN=getSelector[tablename] T_EQUAL termN=getSelector[tablename] {conditions.put(idN, termN);})*)?
    {
        if(optsInc)
            if(condsInc)
                $pdtbst = new UpdateTableStatement(tablename, options, assignations, whereClauses, conditions);
            else
                $pdtbst = new UpdateTableStatement(tablename, options, assignations, whereClauses);
        else
            if(condsInc)
                $pdtbst = new UpdateTableStatement(tablename, assignations, whereClauses, conditions);
            else
                $pdtbst = new UpdateTableStatement(tablename, assignations, whereClauses);
    }
;

stopProcessStatement returns [StopProcessStatement stprst]:
    T_STOP T_PROCESS tablename=getProcess { $stprst = new StopProcessStatement(tablename); }
;

getProcess returns [String procname]:
    processname=(T_PATH | T_IDENT) {$procname = $processname.text;}
;

dropTriggerStatement returns [DropTriggerStatement drtrst]:
    T_DROP
    T_TRIGGER tablename=T_IDENT
    T_ON
    ident2=T_IDENT
    {$drtrst = new DropTriggerStatement($tablename.text,$ident2.text);}
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
        LinkedHashMap<ColumnName, String> columns = new LinkedHashMap<>();
        ArrayList<ColumnName> primaryKey = new ArrayList<>();
        ArrayList<ColumnName> clusterKey = new ArrayList<>();
        int primaryKeyType = 0;
        int columnNumberPK= 0;
        int columnNumberPK_inter= 0;
        boolean ifNotExists = false;
        boolean withProperties = false;
    }:
    T_CREATE T_TABLE (T_IF T_NOT T_EXISTS {ifNotExists = true;})? tablename=getTableName T_ON T_CLUSTER clusterID=T_IDENT
    T_START_PARENTHESIS (
        ident_column1=getField type1=getDataType (T_PRIMARY T_KEY)? {columns.put(new ColumnName(tablename, ident_column1), type1); primaryKeyType=1;}
            (
                (T_COMMA ident_columN=getField typeN=getDataType (T_PRIMARY T_KEY {primaryKeyType=1;columnNumberPK=columnNumberPK_inter +1;})? {columns.put(new ColumnName(tablename, ident_columN), typeN); columnNumberPK_inter+=1;})
                |(T_COMMA T_PRIMARY T_KEY T_START_PARENTHESIS
                    (
                        (primaryK=getField {primaryKey.add(new ColumnName(tablename, primaryK)); primaryKeyType=2;}
                            (T_COMMA partitionKN=getField {primaryKey.add(new ColumnName(tablename, partitionKN));})*
                        )
                        |(T_START_PARENTHESIS partitionK=getField {primaryKey.add(new ColumnName(tablename, partitionK)); primaryKeyType=3;}
                            (T_COMMA partitionKN=getField {primaryKey.add(new ColumnName(tablename, partitionKN));})*
                            T_END_PARENTHESIS
                            (T_COMMA clusterKN=getField {clusterKey.add(new ColumnName(tablename, clusterKN));})*
                        )
                    )
                    T_END_PARENTHESIS
                )
            )*
        )
    T_END_PARENTHESIS (T_WITH {withProperties=true;} properties=getMetaProperties[tablename])?
    {
        $crtast = new CreateTableStatement(tablename, new ClusterName($clusterID.text), columns, primaryKey, clusterKey, primaryKeyType, columnNumberPK);
        $crtast.setProperties(properties);
        $crtast.setIfNotExists(ifNotExists);
        $crtast.setWithProperties(withProperties);
    }
;

alterTableStatement returns [AlterTableStatement altast]
    @init{
        int option= 0;
    }:
    T_ALTER T_TABLE tablename=getTableName
    (T_ALTER column=getColumnName[tablename] T_TYPE type=T_IDENT {option=1;}
        |T_ADD column=getColumnName[tablename] type=T_IDENT {option=2;}
        |T_DROP column=getColumnName[tablename] {option=3;}
        |(T_WITH {option=4;} props=getMetaProperties[tablename])?
    )
    {$altast = new AlterTableStatement(tablename, column, $type.text, props, option);  }
;

selectStatement returns [SelectStatement slctst]
    @init{
        boolean windowInc = false;
        boolean joinInc = false;
        boolean whereInc = false;
        boolean orderInc = false;
        boolean groupInc = false;
        boolean limitInc = false;
        Map fieldsAliasesMap = new HashMap<String, String>();
        Map tablesAliasesMap = new HashMap<String, String>();
        MutablePair<String, String> pair = new MutablePair<>();
    }:
    T_SELECT selClause=getSelectExpression[fieldsAliasesMap] T_FROM tablename=getAliasedTableID[tablesAliasesMap]
    (T_WITH T_WINDOW {windowInc = true;} window=getWindow)?
    (T_INNER T_JOIN { joinInc = true;} identJoin=getAliasedTableID[tablesAliasesMap] T_ON joinRelations=getWhereClauses[tablename])?
    (T_WHERE {whereInc = true;} whereClauses=getWhereClauses[null])?
    //(T_ORDER T_BY {orderInc = true;} ordering=getOrdering)?
    //(T_GROUP T_BY {groupInc = true;} groupby=getGroupBy)?
    (T_LIMIT {limitInc = true;} constant=getConstant)?
    {
        $slctst = new SelectStatement(selClause, tablename);
        if(windowInc)
            $slctst.setWindow(window);
        if(joinInc)
            $slctst.setJoin(new InnerJoin(identJoin, joinRelations));
        if(whereInc)
             $slctst.setWhere(whereClauses);
        //if(orderInc)
        //     $slctst.setOrder(ordering);
        //if(groupInc)
        //     $slctst.setGroup(groupby);
        if(limitInc)
             $slctst.setLimit(Integer.parseInt(constant));

        //$slctst.replaceAliasesWithName(fieldsAliasesMap, tablesAliasesMap);
        //$slctst.updateTableNames();
    }
;

insertIntoStatement returns [InsertIntoStatement nsntst]
    @init{
        ArrayList<ColumnName> ids = new ArrayList<>();
        boolean ifNotExists = false;
        int typeValues = InsertIntoStatement.TYPE_VALUES_CLAUSE;
        ArrayList<Selector> cellValues = new ArrayList<>();
        boolean optsInc = false;
        ArrayList<Option> options = new ArrayList<>();
    }:
    T_INSERT T_INTO tablename=getTableName
    T_START_PARENTHESIS
        ident1=getColumnName[tablename] {ids.add(ident1);} (T_COMMA identN=getColumnName[tablename] {ids.add(identN);})*
    T_END_PARENTHESIS
    (
        selectStmnt=selectStatement {typeValues = InsertIntoStatement.TYPE_SELECT_CLAUSE;}
        |
        T_VALUES
        T_START_PARENTHESIS
            term1=getSelector[tablename] {cellValues.add(term1);}
            (T_COMMA termN=getSelector[tablename] {cellValues.add(termN);})*
        T_END_PARENTHESIS
    )
    (T_IF T_NOT T_EXISTS {ifNotExists=true;} )?
    (
        T_USING {optsInc=true;}
        opt1=getOption[tablename] {
            options.add(opt1);
        }
        (T_AND optN=getOption[tablename] {options.add(optN);})*
    )?
    {
        if(typeValues==InsertIntoStatement.TYPE_SELECT_CLAUSE)
            if(optsInc)
                $nsntst = new InsertIntoStatement(tablename, ids, selectStmnt, ifNotExists, options);
            else
                $nsntst = new InsertIntoStatement(tablename, ids, selectStmnt, ifNotExists);
        else
            if(optsInc)
                $nsntst = new InsertIntoStatement(tablename, ids, cellValues, ifNotExists, options);
            else
                $nsntst = new InsertIntoStatement(tablename, ids, cellValues, ifNotExists);

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
    T_DROP T_TABLE (T_IF T_EXISTS { ifExists = true; })?
    identID=getTableName {
        $drtbst = new DropTableStatement(identID, ifExists);
    }
;

truncateStatement returns [TruncateStatement trst]:
	T_TRUNCATE
        tablename=getTableName {
            $trst = new TruncateStatement(tablename);
	}
;

metaStatement returns [MetaStatement st]
    @init{
        boolean hasCatalog = false;
    }:
    (T_START_BRACKET (inputCatalog=T_IDENT {hasCatalog=true;} )? T_END_BRACKET T_COMMA {if(hasCatalog) sessionCatalog = $inputCatalog.text;})?
    (st_nsnt   = insertIntoStatement { $st = st_nsnt;}
    | st_slct = selectStatement { $st = st_slct;}
    | st_crta = createTableStatement { $st = st_crta;}
    | st_altt = alterTableStatement { $st = st_altt;}
    | st_pdtb = updateTableStatement { $st = st_pdtb; }
    | st_tbdr = dropTableStatement { $st = st_tbdr; }
    | st_trst = truncateStatement { $st = st_trst; }
    | st_lsst = listStatement { $st = st_lsst; }
    | st_stpr = stopProcessStatement { $st = st_stpr; }
    | st_xppl = explainPlanStatement { $st = st_xppl;}
    | st_adds = addStatement { $st = st_adds; }
    | st_drmn = dropManifestStatement { $st = st_drmn;}
    | st_rust = removeUDFStatement { $st = st_rust; }
    | st_dlst = deleteStatement { $st = st_dlst; }
    | st_desc = describeStatement { $st = st_desc;}
    | st_crks = createCatalogStatement { $st = st_crks; }
    | st_alks = alterCatalogStatement { $st = st_alks; }
    | st_drks = dropCatalogStatement { $st = st_drks ;}
    | st_atcs = attachClusterStatement { $st = st_atcs;}
    | st_dtcs = detachClusterStatement {$st = st_dtcs;}
    | st_alcs = alterClusterStatement {$st = st_alcs;}
    | st_atcn = attachConnectorStatement { $st = st_atcn;}
    | st_decn = detachConnectorStatement { $st = st_decn;}
    | st_cixs = createIndexStatement { $st = st_cixs; }
    | st_dixs = dropIndexStatement { $st = st_dixs; }
    | st_crtr = createTriggerStatement { $st = st_crtr; }
    | st_drtr = dropTriggerStatement { $st = st_drtr; })
;

query returns [MetaStatement st]:
	mtst=metaStatement (T_SEMICOLON)+ EOF {
		$st = mtst;
	}
;


//FUNCTIONS
getIndexType returns [String indexType]:
    ( idxType=T_DEFAULT
    | idxType=T_LUCENE
    | idxType=T_CUSTOM)
    {$indexType=$idxType.text;}
;

getMetaProperties[TableName tablename] returns [ArrayList<Property> props]
    @init{
        $props = new ArrayList<>();
    }:
    firstProp=getMetaProperty[tablename] {$props.add(firstProp);}
    (T_AND newProp=getMetaProperty[tablename] {$props.add(newProp);})*
;

getMetaProperty[TableName tablename] returns [Property mp]
    @init{
        BooleanSelector boolProp = new BooleanSelector("true");
    }:
    (identProp=getSelector[tablename] T_EQUAL valueProp=getSelector[tablename] {$mp = new PropertyNameValue(identProp, new StringSelector(valueProp.toString()));}
    | T_COMPACT T_STORAGE {$mp = new PropertyCompactStorage();}
    | T_CLUSTERING T_ORDER T_BY T_START_PARENTHESIS ordering=getOrdering {$mp = new PropertyClusteringOrder(ordering);} T_END_PARENTHESIS)
    | T_EPHEMERAL ( | T_EQUAL (T_FALSE { boolProp = new BooleanSelector("false");} | T_TRUE )) {$mp = new PropertyNameValue(new StringSelector("ephemeral"), boolProp);}
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
    //TODO: Change to support group by
    ident1=(T_KS_AND_TN | T_IDENT) {groupBy = new GroupBy(); groups.add(groupBy);}
    (T_COMMA identN=(T_KS_AND_TN | T_IDENT) {groupBy = new GroupBy(); groups.add(groupBy);})*
;

getWhereClauses[TableName tablename] returns [ArrayList<Relation> clauses]
    @init{
        clauses = new ArrayList<>();
    }:
    rel1=getRelation[tablename] {clauses.add(rel1);} (T_AND relN=getRelation[tablename] {clauses.add(relN);})*
;

getFields[MutablePair pair]:
    ident1L=getTableName { pair.setLeft(ident1L); } T_EQUAL ident1R=getTableName { pair.setRight(ident1R); }
    | T_START_PARENTHESIS ident1L=getTableName { pair.setLeft(ident1L); } T_EQUAL ident1R=getTableName { pair.setRight(ident1R); } T_END_PARENTHESIS
;

getWindow returns [Window ws]:
    (T_LAST {$ws = new Window(WindowType.LAST);}
    | cnstnt=getConstant (T_ROWS {$ws = new Window(WindowType.NUM_ROWS); $ws.setNumRows(Integer.parseInt(cnstnt));}
                       | unit=getTimeUnit {$ws = new Window(WindowType.TEMPORAL); $ws.setTimeWindow(Integer.parseInt(cnstnt), unit);}
                       )
    )
;

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
    | T_DAYS {$unit=TimeUnit.DAYS;})
;

getSelectExpression[Map fieldsAliasesMap] returns [SelectExpression se]
    @init{
        boolean distinct = false;
        List<Selector> selectors = new ArrayList<>();
    }
    @after{
        se = new SelectExpression(selectors);
        se.setDistinct(distinct);
    }:
    (T_DISTINCT {distinct = true;})?
    (
        T_ASTERISK { s = new AsteriskSelector(); selectors.add(s);}
        | s=getSelector[null]
                (T_AS alias1=getAlias {
                    s.setAlias($alias1.text);
                    fieldsAliasesMap.put($alias1.text, s.toString());}
                )? {selectors.add(s);}
            (T_COMMA s=getSelector[null]
                    (T_AS aliasN=getAlias {
                        s.setAlias($aliasN.text);
                        fieldsAliasesMap.put($aliasN.text, s.toString());}
                    )? {selectors.add(s);})*
    )
;

getCountSymbol returns [String str]:
    '1' {$str = new String("1");}
;

getAlias returns [String alias]:
	tablename=T_IDENT {$alias=$tablename.text;}
;

getSelector[TableName tablename] returns [Selector s]
    @init{
        List<Selector> params = new ArrayList<>();
        String name = null;
        //String functionName = null;
    }:
    (
        (functionName=T_SUM
            | functionName=T_MAX
            | functionName=T_MIN
            | functionName=T_AVG
            | functionName=T_COUNT
            | functionName=T_IDENT
        )
        T_START_PARENTHESIS
            (select1=getSelector[tablename] {params.add(select1);}
            | T_ASTERISK {params.add(new AsteriskSelector());}
            )?
        T_END_PARENTHESIS {s = new FunctionSelector($functionName.text, params);}
        |
        (
            columnName=getColumnName[tablename] {s = new ColumnSelector(columnName);}
            | floatingNumber=T_FLOAT {s = new FloatingPointSelector($floatingNumber.text);}
            | constant=getConstant {s = new IntegerSelector(constant);}
            | T_FALSE {s = new BooleanSelector(false);}
            | T_TRUE {s = new BooleanSelector(true);}
            | path=T_PATH {s = new StringSelector($path.text);}
            | qLiteral=QUOTED_LITERAL {s = new StringSelector($qLiteral.text);}
        )
    )
;

// Migrate
//
//     | constant=getConstant {$term = new LongTerm(constant);}
//     | T_FALSE {$term = new BooleanSelector("false");}
//     | T_TRUE {$term = new BooleanSelector("true");}
//     | floatingNumber=T_FLOAT {$term = new DoubleTerm($floatingNumber.text);}
//    NO | ksAndTn=T_KS_AND_TN {$term = new StringSelector($ksAndTn.text);}
//    NO | noIdent=T_TERM {$term = new StringSelector($noIdent.text);}
//     | path=T_PATH {$term = new StringSelector($path.text);}
//     | qLiteral=QUOTED_LITERAL {$term = new StringSelector($qLiteral.text);}
//
//


getListTypes returns [String listType]:
	//tablename=('PROCESS' | 'UDF' | 'TRIGGER' | 'process' | 'udf' | 'trigger') {$listType = new String($tablename.text);}
	//tablename=('PROCESS' | 'UDF' | 'TRIGGER') {$listType = new String($tablename.text);}
	//tablename=(T_PROCESS | 'UDF' | 'TRIGGER') {$listType = new String($tablename.text);}
	tablename=(T_PROCESS | T_UDF | T_TRIGGER) {$listType = new String($tablename.text);}
;

getAssignment[TableName tablename] returns [Relation assign]:
    firstTerm=getSelector[tablename]
        (T_EQUAL value=getValueAssign[tablename] {$assign = new Relation(firstTerm, Operator.ASSIGN, value);}
        | T_START_BRACKET indexTerm=getSelector[tablename] T_END_BRACKET T_EQUAL termValue=getValueAssign[tablename] {
            //TODO: Support index for collections (Example: cities[2] = 'Madrid')
            $assign = new Relation (firstTerm, Operator.ASSIGN, termValue);
        })
;

getValueAssign[TableName tablename] returns [Selector valueAssign]
    @init{
        Operator op = Operator.ADD;
    }:
    //firstTerm=getSelector[tablename] T_PLUS secondTerm=getSelector
    vAssign=getSelector[tablename] {valueAssign = vAssign;}
    (
        operator=(T_PLUS | T_SUBTRACT {op = Operator.SUBTRACT;} )
        termN=getSelector[tablename] {
            //TODO: Relation of Relation (Example: amount = amount + 5)
        }
    )*
;

getRelation[TableName tablename] returns [Relation mrel]
    @after{
        $mrel = new Relation(s, operator, rs);
    }:
    s=getSelector[tablename]
    operator=getComparator
    rs=getSelector[null]

;

getComparator returns [Operator op]:
    T_EQUAL {$op = Operator.COMPARE;}
    | T_GT {$op = Operator.GT;}
    | T_LT {$op = Operator.LT;}
    | T_GTE {$op = Operator.GET;}
    | T_LTE {$op = Operator.LET;}
    | T_NOT_EQUAL {$op = Operator.DISTINCT;}
    | T_LIKE {$op = Operator.LIKE;}
    | T_MATCH {$op = Operator.MATCH;}
;

getIds returns [ArrayList<String> listStrs]
    @init{
        listStrs = new ArrayList<>();
    }:
    ident1=T_IDENT {listStrs.add($ident1.text);} (T_COMMA identN=T_IDENT {listStrs.add($identN.text);})*
;

getOptions[TableName tablename] returns [ArrayList<Option> opts]@init{
        opts = new ArrayList<>();
    }:
    opt1=getOption[tablename] {opts.add(opt1);} (optN=getOption[tablename] {opts.add(optN);})*
;

getOption[TableName tablename] returns [Option opt]:
    T_COMPACT T_STORAGE {$opt=new Option(Option.OPTION_COMPACT);}
    | T_CLUSTERING T_ORDER {$opt=new Option(Option.OPTION_CLUSTERING);}
    | identProp=T_IDENT T_EQUAL valueProp=getSelector[tablename] {$opt=new Option($identProp.text, valueProp);}
;

getSelectors[TableName tablename] returns [ArrayList list]
    @init{
        list = new ArrayList<Selector>();
    }:
    term1=getSelector[tablename] {list.add(term1);}
    (T_COMMA termN=getSelector[tablename] {list.add(termN);})*
;

getAliasedTableID[Map tablesAliasesMap] returns [TableName result]:
	tableN=getTableName (alias=T_IDENT {tablesAliasesMap.put($alias.text, tableN.toString());})?
	{result = tableN;}
;

getColumnName[TableName tn] returns [ColumnName columnName]:
    (ident1=T_IDENT {$columnName = normalizeColumnName(tn, $ident1.text);}
    | ident2=T_KS_AND_TN {$columnName = normalizeColumnName(tn, $ident2.text);}
    | ident3=T_CTLG_TBL_COL {$columnName = normalizeColumnName(tn, $ident3.text);})
;

getTableName returns [TableName tablename]:
    (ident1=T_IDENT {tablename = normalizeTableName($ident1.text);}
    | ident2=T_KS_AND_TN {tablename = normalizeTableName($ident2.text);})
;

getConstant returns [String constStr]:
    constToken=T_CONSTANT {$constStr = new String($constToken.text);}
    | '1' {$constStr = new String("1");}
;

getFloat returns [String floating]:
    termToken=T_TERM {$floating=$termToken.text;}
    | floatToken = T_FLOAT {$floating=$floatToken.text;}
;

getJson returns [String strJson]:
    (objectJson=getObjectJson {strJson=objectJson;}
    | arrayJson=getArrayJson {strJson=arrayJson;})
;

getObjectJson returns [String strJson]
    @init{
        StringBuilder sb = new StringBuilder();
    }
    @after{
        strJson = sb.toString();
    }:
    (T_START_SBRACKET {sb.append("{");} pairJson=getPairJson {sb.append(pairJson);} (T_COMMA {sb.append(", ");} pairJsonN=getPairJson {sb.append(pairJsonN);})* T_END_SBRACKET {sb.append("}");}
    | T_START_SBRACKET {sb.append("{");} T_END_SBRACKET {sb.append("}");})
;

getPairJson returns [String strJson]
    @init{
        StringBuilder sb = new StringBuilder();
    }
    @after{
        strJson = sb.toString();
    }:
    keyTerm=getSelector[null] {sb.append(keyTerm.toString());}
    T_COLON {sb.append(": ");}
    valueJson=getValueJson {sb.append(valueJson);}
;

getArrayJson returns [String strJson]
    @init{
        StringBuilder sb = new StringBuilder();
    }
    @after{
        strJson = sb.toString();
    }:
    (T_START_BRACKET {sb.append("[");} valueJson=getValueJson {sb.append(valueJson);}
        (T_COMMA {sb.append(", ");} valueJsonN=getValueJson {sb.append(valueJsonN);})* T_END_BRACKET {sb.append("]");}
    | T_START_BRACKET {sb.append("[");} T_END_BRACKET {sb.append("]");})
;

getValueJson returns [String strJson]
    @init{
        StringBuilder sb = new StringBuilder();
    }
    @after{
        strJson = sb.toString();
    }:
    (tokenTerm=getSelector[null] {sb.append(tokenTerm.toString());}
    | objectJson=getObjectJson {sb.append(objectJson);}
    | arrayJson=getArrayJson {sb.append(arrayJson);})
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
            column = new ColumnName(null, null, columnTokens[0]);
        }else if(columnTokens.length == 2){
            column = new ColumnName(null, columnTokens[0], columnTokens[1]);
        }else{
            column = new ColumnName(columnTokens[0], columnTokens[1], columnTokens[2]);
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
         table = new TableName(tableTokens[0], tableTokens[1]);
        }else{
         table = new TableName(null, tableName);
        }

    }:
    (ident1=T_IDENT {t1 = $ident1.text;}
    | ident2=T_KS_AND_TN {t2 = $ident2.text;})
;

// ===================================================

WS: (' ' | '\t' | '\n' | '\r')+ { 
        $channel = HIDDEN; 
    }
;
