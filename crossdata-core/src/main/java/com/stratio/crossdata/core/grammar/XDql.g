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

grammar XDql;

options {
    k = 0;
    language = Java;
    backtrack = false;
    memoize = true;
}
 
@header {
    package com.stratio.crossdata.core.grammar.generated;
    import com.stratio.crossdata.common.data.*;
    import com.stratio.crossdata.common.statements.structures.*;
    import com.stratio.crossdata.common.statements.structures.window.*;
    import com.stratio.crossdata.core.statements.*;
    import com.stratio.crossdata.core.structures.*;
    import com.stratio.crossdata.core.structures.*;
    import com.stratio.crossdata.core.utils.*;
    import com.stratio.crossdata.common.metadata.*;
    import com.stratio.crossdata.common.metadata.structures.*;
    import java.util.LinkedHashMap;
    import java.util.LinkedList;
    import java.util.Map;
    import java.util.Set;
    import java.util.HashSet;
    import org.apache.commons.lang3.tuple.MutablePair;
    import com.stratio.crossdata.common.exceptions.*;
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
        if(columnTokens.length == 1) {
            return new ColumnName(getEffectiveTable(tn), columnTokens[0]);
        } else if(columnTokens.length == 2) {
            return new ColumnName(getEffectiveCatalog(tn), columnTokens[0], columnTokens[1]);
        } else {
            return new ColumnName(columnTokens[0], columnTokens[1], columnTokens[2]);
        }
    }

    public IndexName normalizeIndexName(String str){
            String [] indexTokens = str.split("\\.");
            if((indexTokens.length) == 2 && (!sessionCatalog.isEmpty())){
                return new IndexName(sessionCatalog, indexTokens[0], indexTokens[1]);
            } else if(indexTokens.length == 3) {
                return new IndexName(indexTokens[0], indexTokens[1], indexTokens[2]);
            } else {
                throwParsingException("Catalog can't be empty");
            }
            return null;
    }

    private ErrorsHelper foundErrors = new ErrorsHelper();

    public ErrorsHelper getFoundErrors(){
        return foundErrors;
    }

    public void throwParsingException(String message) {
        throw new ParsingException(message);
    }

    public boolean checkWhereClauses(List<Relation> whereClauses){
        if((whereClauses == null) || (whereClauses.isEmpty())){
            return true;
        }
        for(Relation relation: whereClauses){
            if(!(relation.getLeftTerm() instanceof ColumnSelector)){
                return false;
            }
        }
        return true;
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
    package com.stratio.crossdata.core.grammar.generated;
    import com.stratio.crossdata.common.exceptions.*;
}

@lexer::members {
    @Override
    public void recover(RecognitionException re) {
        throw new ParsingException("line " + input.getLine() + ":" + input.getCharPositionInLine()
                    + ": no viable alternative");
    }
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
T_CLUSTERS: C L U S T E R S;
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
T_DATASTORES: D A T A S T O R E S;
T_CONNECTOR: C O N N E C T O R;
T_CONNECTORS: C O N N E C T O R S;

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
T_FULL_TEXT: F U L L '_' T E X T;
T_START_BRACKET: '[';
T_END_BRACKET: ']';
T_PLUS: '+';
T_SUBTRACT: '-';
T_SLASH: '/';
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
T_DOUBLE: D O U B L E;
T_FLOAT: F L O A T;
T_MAP: M A P;
T_INT: I N T;
T_INTEGER: I N T E G E R;
T_BOOLEAN: B O O L E A N;
T_VARCHAR: V A R C H A R;
T_TEXT: T E X T;
T_BIGINT: B I G I N T;

fragment LETTER: ('A'..'Z' | 'a'..'z');
fragment DIGIT: '0'..'9';

QUOTED_LITERAL
    @init{
        StringBuilder sb = new StringBuilder();
    }
    @after{
        setText(sb.toString());
    }:
      ('"')   (c=~('"') { sb.appendCodePoint(c);})*  ('"')
    | ('\'') (c=~('\'') { sb.appendCodePoint(c);})* ('\'')
;

T_CONSTANT: '-'? (DIGIT)+;

T_IDENT: LETTER (LETTER | DIGIT | '_')*;

T_KS_AND_TN: T_IDENT (POINT T_IDENT)?;

T_CTLG_TBL_COL: T_IDENT (POINT T_IDENT (POINT T_IDENT)?)?;

T_FLOATING: '-'? (('0'..'9')+ POINT ('0'..'9')+ EXPONENT? | POINT ('0'..'9')+ EXPONENT? | ('0'..'9')+ EXPONENT);

T_TERM: (LETTER | DIGIT | '_' | POINT)+;

// ========================================================
// CLUSTER
// ========================================================

attachClusterStatement returns [AttachClusterStatement acs]
    @init{
        boolean ifNotExists = false;
        j = "";
    }
    @after{
        acs = new AttachClusterStatement(
            new ClusterName($clusterName.text),
            ifNotExists,
            new DataStoreName($dataStoreName.text),
            j);
    }:
    T_ATTACH T_CLUSTER
    (T_IF T_NOT T_EXISTS {ifNotExists = true;})?
    clusterName=T_IDENT
    T_ON T_DATASTORE dataStoreName=T_IDENT
    (T_WITH T_OPTIONS j=getJson)?
;

detachClusterStatement returns [DetachClusterStatement dcs]
    @after{
        dcs = new DetachClusterStatement($clusterName.text);
    }:
    T_DETACH T_CLUSTER clusterName=T_IDENT
;

alterClusterStatement returns [AlterClusterStatement acs]
    @init{
        boolean ifExists = false;
    }
    @after{
        acs = new AlterClusterStatement($clusterName.text, ifExists, j);
    }:
    T_ALTER T_CLUSTER (T_IF T_EXISTS {ifExists = true;} )? clusterName=T_IDENT T_WITH T_OPTIONS j=getJson
;

// ========================================================
// CONNECTOR
// ========================================================

attachConnectorStatement returns [AttachConnectorStatement acs]
    @init{
        optionsJson = "";
    }
    @after{
        $acs = new AttachConnectorStatement(new ConnectorName($connectorName.text),
        new ClusterName($clusterName.text), optionsJson);
    }:
    T_ATTACH T_CONNECTOR connectorName=T_IDENT T_TO clusterName=T_IDENT (T_WITH T_OPTIONS optionsJson=getJson)?
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
    { $crctst = new CreateCatalogStatement(new CatalogName($catalogName.text), ifNotExists, j); }
;

dropCatalogStatement returns [DropCatalogStatement drcrst]
    @init{
        boolean ifExists = false;
    }:
    T_DROP T_CATALOG
    (T_IF T_EXISTS {ifExists = true;})?
    catalogName=T_IDENT
    { $drcrst = new DropCatalogStatement(new CatalogName($catalogName.text), ifExists);}
;

alterCatalogStatement returns [AlterCatalogStatement alctst]:
    T_ALTER T_CATALOG
    catalogName=T_IDENT
    T_WITH j=getJson
    { $alctst = new AlterCatalogStatement(new CatalogName($catalogName.text), j); }
;

// ========================================================
// TABLE
// ========================================================


//STATEMENTS

describeStatement returns [DescribeStatement descs]:
    T_DESCRIBE (
        T_CATALOG genericID=getGenericID {$descs = new DescribeStatement(DescribeType.CATALOG); $descs.setCatalog(new CatalogName(genericID));}
    	| T_CATALOGS {$descs = new DescribeStatement(DescribeType.CATALOGS);}
        | T_TABLE tablename=getTableName { $descs = new DescribeStatement(DescribeType.TABLE); $descs.setTableName(tablename);}
        | T_TABLES {$descs = new DescribeStatement(DescribeType.TABLES);}
        | T_CLUSTER genericID=getGenericID {$descs = new DescribeStatement(DescribeType.CLUSTER); $descs.setClusterName(new ClusterName(genericID));}
        | T_CLUSTERS {$descs = new DescribeStatement(DescribeType.CLUSTERS); }
        | T_DATASTORE genericID=getGenericID {$descs = new DescribeStatement(DescribeType.DATASTORE); $descs.setDataStoreName(new DataStoreName(genericID));}
        | T_DATASTORES {$descs = new DescribeStatement(DescribeType.DATASTORES);}
        | T_CONNECTOR genericID=getGenericID {$descs = new DescribeStatement(DescribeType.CONNECTOR); $descs.setConnectorName(new ConnectorName(genericID));}
        | T_CONNECTORS {$descs = new DescribeStatement(DescribeType.CONNECTORS);}
    )
;

//DELETE FROM table1 WHERE field1=value1 AND field2=value2;
deleteStatement returns [DeleteStatement ds]
	@after{
		$ds = new DeleteStatement(tablename, whereClauses);
	}:
	T_DELETE T_FROM tablename=getTableName
	T_WHERE whereClauses=getWhereClauses[tablename] { if(!checkWhereClauses(whereClauses)) throwParsingException("Left terms of where clauses must be a column name"); }
;

//ADD \"index_path\";
addStatement returns [AddStatement as]:
	T_ADD name=QUOTED_LITERAL {$as = new AddStatement($name.text);}
;

//DROP (DATASTORE | CONNECTOR) \"name\";
dropManifestStatement returns [CrossdataStatement dms]
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
	T_REMOVE T_UDF jar=QUOTED_LITERAL {$rus = new RemoveUDFStatement($jar.text);}
;

//DROP INDEX IF EXISTS index_name;
dropIndexStatement returns [DropIndexStatement dis]
	@init{
		$dis = new DropIndexStatement();
	}:
	T_DROP T_INDEX (T_IF T_EXISTS { $dis.setDropIfExists(); } )? name=getIndexName { $dis.setName(name); }
;

//CREATE INDEX myIdx ON table1 (field1, field2);
//CREATE DEFAULT INDEX ON table1 (field1, field2);
//CREATE FULL_TEXT INDEX index1 ON table1 (field1, field2) USING com.company.Index.class;
//CREATE CUSTOM INDEX index1 ON table1 (field1, field2) WITH OPTIONS opt1=val1 AND opt2=val2;
createIndexStatement returns [CreateIndexStatement cis]
	@init{
		$cis = new CreateIndexStatement();
	}
	@after{
	    cis.normalizeIndexName();
	}:
	T_CREATE {$cis.setIndexType("default");} (indexType=getIndexType {$cis.setIndexType(indexType);})? T_INDEX
	(T_IF T_NOT T_EXISTS {$cis.setCreateIfNotExists();})?
	(name=getColumnName[null] { $cis.setName(name);} )?
	T_ON tablename=getTableName {$cis.setTableName(tablename);}
	T_START_PARENTHESIS
        firstField=getColumnName[tablename] {$cis.addColumn(firstField);}
	(T_COMMA
		field=getColumnName[tablename] {$cis.addColumn(field);}
	)*
	T_END_PARENTHESIS
	(T_USING usingClass=QUOTED_LITERAL {$cis.setUsingClass($usingClass.text);})?
	(T_WITH j=getJson {$cis.setOptionsJson(j);} )?
;

//FUNCTIONS
getIndexType returns [String indexType]:
    ( idxType=T_DEFAULT
    | idxType=T_FULL_TEXT
    | idxType=T_CUSTOM)
    {$indexType=$idxType.text;}
;

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
        Map<Selector, Selector> conditions = new LinkedHashMap<>();
    }:
    T_UPDATE tablename=getTableName
    (T_USING opt1=getOption[tablename] {optsInc = true; options.add(opt1);} (T_AND optN=getOption[tablename] {options.add(optN);})*)?
    T_SET assig1=getAssignment[tablename] {assignations.add(assig1);} (T_COMMA assigN=getAssignment[tablename] {assignations.add(assigN);})*
    (T_WHERE whereClauses=getWhereClauses[tablename])?
    (T_IF id1=getSelector[tablename] T_EQUAL term1=getSelector[tablename] {condsInc = true; conditions.put(id1, term1);}
                    (T_AND idN=getSelector[tablename] T_EQUAL termN=getSelector[tablename] {conditions.put(idN, termN);})*)?
    {
        if(!checkWhereClauses(whereClauses)) throwParsingException("Left terms of where clauses must be a column name");
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
    T_STOP T_PROCESS tablename=T_IDENT { $stprst = new StopProcessStatement($tablename.text); }
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
        LinkedHashMap<ColumnName, ColumnType> columns = new LinkedHashMap<>();
        LinkedList<ColumnName> partitionKey = new LinkedList<>();
        LinkedList<ColumnName> clusterKey = new LinkedList<>();
        boolean ifNotExists = false;
    }:
    T_CREATE tableType=getTableType T_TABLE (T_IF T_NOT T_EXISTS {ifNotExists = true;})?
    tablename=getTableName { if(!tablename.isCompletedName()) throwParsingException("Catalog is missing") ; }
    T_ON T_CLUSTER clusterID=T_IDENT
    T_START_PARENTHESIS
        id1=getColumnName[tablename] type1=getDataType (T_PRIMARY T_KEY { partitionKey.add(id1); } )? { columns.put(id1, type1);}
        (T_COMMA idN=getColumnName[tablename] typeN=getDataType { columns.put(idN, typeN); } )*
        (T_COMMA T_PRIMARY T_KEY T_START_PARENTHESIS
                (idPk1=getColumnName[tablename] { if(!partitionKey.isEmpty()) throwParsingException("Partition key was previously defined");
                                                 partitionKey.add(idPk1); }
                | T_START_PARENTHESIS
                    idParK1=getColumnName[tablename] { if(!partitionKey.isEmpty()) throwParsingException("Partition key was previously defined");
                                                       partitionKey.add(idParK1); }
                    (T_COMMA idParKN=getColumnName[tablename] { partitionKey.add(idParKN); }
                    )*
                T_END_PARENTHESIS)
                (T_COMMA idPkN=getColumnName[tablename] { clusterKey.add(idPkN); })*
        T_END_PARENTHESIS)?
    T_END_PARENTHESIS (T_WITH j=getJson)?
    {
        if(partitionKey.isEmpty()) throwParsingException("Primary Key definition missing");
        $crtast = new CreateTableStatement(tableType, tablename, new ClusterName($clusterID.text), columns,
        partitionKey, clusterKey);
        $crtast.setProperties(j);
        $crtast.setIfNotExists(ifNotExists);
    }
;

getTableType returns [TableType tableType]
    @init{
        tableType = TableType.DATABASE;
    }:
    ( T_EPHEMERAL { tableType = TableType.EPHEMERAL; } )?
;

alterTableStatement returns [AlterTableStatement altast]
    @init{
        AlterOperation option= null;
    }:
    T_ALTER T_TABLE tablename=getTableName
    (T_ALTER column=getColumnName[tablename] T_TYPE dataType=getDataType {option=AlterOperation.ALTER_COLUMN;}
        |T_ADD column=getColumnName[tablename] dataType=getDataType {option=AlterOperation.ADD_COLUMN;}
        |T_DROP column=getColumnName[tablename] {option=AlterOperation.DROP_COLUMN;}
        |(T_WITH {option=AlterOperation.ALTER_OPTIONS;} j=getJson)?
    )
    {$altast = new AlterTableStatement(tablename, column, dataType, j, option);  }
;

selectStatement returns [SelectStatement slctst]
    @init{
        boolean windowInc = false;
        boolean joinInc = false;
        boolean whereInc = false;
        boolean orderInc = false;
        boolean groupInc = false;
        boolean limitInc = false;
        Map fieldsAliasesMap = new LinkedHashMap<String, String>();
        Map tablesAliasesMap = new LinkedHashMap<String, String>();
        MutablePair<String, String> pair = new MutablePair<>();
    }
    @after{
        slctst.setFieldsAliases(fieldsAliasesMap);
        slctst.setTablesAliases(tablesAliasesMap);
    }:
    T_SELECT selClause=getSelectExpression[fieldsAliasesMap] T_FROM tablename=getAliasedTableID[tablesAliasesMap]
    (T_WITH T_WINDOW {windowInc = true;} window=getWindow)?
    (T_INNER T_JOIN { joinInc = true;} identJoin=getAliasedTableID[tablesAliasesMap] T_ON joinRelations=getWhereClauses[null])?
    (T_WHERE {whereInc = true;} whereClauses=getWhereClauses[null])?
    (T_ORDER T_BY {orderInc = true;} orderBy=getOrdering[null])?
    (T_GROUP T_BY {groupInc = true;} groupBy=getGroupBy[null])?
    (T_LIMIT {limitInc = true;} constant=T_CONSTANT)?
    {
        if(!checkWhereClauses(whereClauses)) throwParsingException("Left terms of where clauses must be a column name");
        $slctst = new SelectStatement(selClause, tablename);
        if(windowInc)
            $slctst.setWindow(window);
        if(joinInc)
            $slctst.setJoin(new InnerJoin(identJoin, joinRelations));
        if(whereInc)
             $slctst.setWhere(whereClauses);
        if(orderInc)
             $slctst.setOrderBy(orderBy);
        if(groupInc)
             $slctst.setGroupBy(new GroupBy(groupBy));
        if(limitInc)
             $slctst.setLimit(Integer.parseInt($constant.text));

        //$slctst.replaceAliasesWithName(fieldsAliasesMap, tablesAliasesMap);
        //$slctst.updateTableNames();
    }
;

insertIntoStatement returns [InsertIntoStatement nsntst]
    @init{
        LinkedList<ColumnName> ids = new LinkedList<>();
        boolean ifNotExists = false;
        int typeValues = InsertIntoStatement.TYPE_VALUES_CLAUSE;
        LinkedList<Selector> cellValues = new LinkedList<>();
        boolean optsInc = false;
        LinkedList<Option> options = new LinkedList<>();
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
        if((!ids.isEmpty()) && (!cellValues.isEmpty()) && (ids.size() != cellValues.size())) throwParsingException("Number of columns and number of values differ");
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
    T_EXPLAIN T_PLAN T_FOR parsedStmnt=crossdataStatement
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

crossdataStatement returns [CrossdataStatement st]:
    (T_START_BRACKET
        ( gID=getGenericID { sessionCatalog = gID;} )?
    T_END_BRACKET T_COMMA)?
    (st_nsnt  = insertIntoStatement { $st = st_nsnt;}
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

query returns [CrossdataStatement st]:
	mtst=crossdataStatement (T_SEMICOLON)+ EOF {
		$st = mtst;
	}
;

getDataType returns [ColumnType dataType]:
    ( ident1=getBasicType
    | ident1=getCollectionType T_LT ident2=getBasicType T_GT { ident1.setDBCollectionType(ident2); }
    | ident1=getMapType T_LT ident2=getBasicType T_COMMA ident3=getBasicType T_GT { ident1.setDBMapType(ident2, ident3); }
    ) { $dataType = ident1; }
;

getBasicType returns [ColumnType dataType]:
    T_BIGINT { $dataType=ColumnType.BIGINT; }
    | T_BOOLEAN { $dataType=ColumnType.BOOLEAN; }
    | T_DOUBLE { $dataType=ColumnType.DOUBLE; }
    | T_FLOAT { $dataType=ColumnType.FLOAT; }
    | T_INT { $dataType=ColumnType.INT; }
    | T_INTEGER { $dataType=ColumnType.INT; }
    | T_TEXT { $dataType=ColumnType.TEXT; }
    | T_VARCHAR { $dataType=ColumnType.VARCHAR; }
;

getCollectionType returns [ColumnType dataType]:
    T_SET { $dataType = ColumnType.SET; }
    | T_LIST { $dataType = ColumnType.LIST; }
;

getMapType returns [ColumnType dataType]:
    T_MAP { $dataType = ColumnType.MAP; }
;

getOrdering[TableName tablename] returns [OrderBy orderBy]
    @init{
        List<Selector> selectorListOrder = new ArrayList<>();
        OrderDirection direction = OrderDirection.ASC;
    }
    @after{
        $orderBy = new OrderBy(direction, selectorListOrder);
    }:
    ident1=getSelector[tablename] {selectorListOrder.add(ident1);}
    (T_COMMA identN=getSelector[tablename] {selectorListOrder.add(identN);})*
    (T_ASC | T_DESC { direction = OrderDirection.DESC; } )?
;

getGroupBy[TableName tablename] returns [ArrayList<Selector> groups]
    @init{
        groups = new ArrayList<>();
    }:
    ident1=getSelector[tablename] {groups.add(ident1);}
    (T_COMMA identN=getSelector[tablename] {groups.add(identN);})*
;

getWhereClauses[TableName tablename] returns [ArrayList<Relation> clauses]
    @init{
        clauses = new ArrayList<>();
    }:
    T_START_PARENTHESIS rel1=getRelation[tablename] {clauses.add(rel1);} (T_AND wcs=getWhereClauses[tablename] {clauses.addAll(wcs);})* T_END_PARENTHESIS (T_AND wcs=getWhereClauses[tablename] {clauses.addAll(wcs);})*
    | rel1=getRelation[tablename] {clauses.add(rel1);} (T_AND wcs=getWhereClauses[tablename] {clauses.addAll(wcs);})*
;

getRelation[TableName tablename] returns [Relation mrel]
    @after{
        $mrel = new Relation(s, operator, rs);
    }:
    s=getSelector[tablename] operator=getComparator rs=getSelector[tablename]
;

getFields[MutablePair pair]:
    ident1L=getTableName { pair.setLeft(ident1L); } T_EQUAL ident1R=getTableName { pair.setRight(ident1R); }
    | T_START_PARENTHESIS ident1L=getTableName { pair.setLeft(ident1L); } T_EQUAL ident1R=getTableName { pair.setRight(ident1R); } T_END_PARENTHESIS
;

getWindow returns [Window ws]:
    (T_LAST {$ws = new Window(WindowType.LAST);}
    | cnstnt=T_CONSTANT (T_ROWS {$ws = new Window(WindowType.NUM_ROWS); $ws.setNumRows(Integer.parseInt($cnstnt.text));}
                       | unit=getTimeUnit {$ws = new Window(WindowType.TEMPORAL); $ws.setTimeWindow(Integer.parseInt($cnstnt.text), unit);}
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
        T_ASTERISK { if(distinct) throwParsingException("Selector DISTINCT doesn't accept '*'");
                     s = new AsteriskSelector(); selectors.add(s);}
        | s=getSelector[null] { if(s == null) throwParsingException("Column name not found");}
                (T_AS alias1=getGenericID {
                    s.setAlias(alias1);
                    fieldsAliasesMap.put(alias1, s.toString());}
                )? {selectors.add(s);}
            (T_COMMA s=getSelector[null] { if(s == null) throwParsingException("Column name not found");}
                    (T_AS aliasN=getGenericID {
                        s.setAlias(aliasN);
                        fieldsAliasesMap.put(aliasN, s.toString());}
                    )? {selectors.add(s);})*
    )
;

getSelector[TableName tablename] returns [Selector s]
    @init{
        List<Selector> params = new ArrayList<>();
        String name = null;
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
        T_END_PARENTHESIS { String functionStr = $functionName.text;
                            if(functionStr.equalsIgnoreCase("count") && (!params.toString().equalsIgnoreCase("[*]")) && (!params.toString().equalsIgnoreCase("[1]"))) throwParsingException("COUNT function only accepts '*' or '1'");
                            s = new FunctionSelector(functionStr, params);}
        |
        (
            columnName=getColumnName[tablename] {s = new ColumnSelector(columnName);}
            | floatingNumber=T_FLOATING {s = new FloatingPointSelector($floatingNumber.text);}
            | constant=T_CONSTANT {s = new IntegerSelector($constant.text);}
            | T_FALSE {s = new BooleanSelector(false);}
            | T_TRUE {s = new BooleanSelector(true);}
            | qLiteral=QUOTED_LITERAL {s = new StringSelector($qLiteral.text);}
        )
    )
;

getListTypes returns [String listType]:
	tablename=(T_PROCESS | T_UDF | T_TRIGGER) {$listType = new String($tablename.text);}
;

getAssignment[TableName tablename] returns [Relation assign]
    @after{
        $assign = new Relation(leftTerm, Operator.EQ, rightTerm);
    }:
    leftTerm=getSelector[tablename] T_EQUAL rightTerm=getRightTermInAssignment[tablename]
;


getRightTermInAssignment[TableName tablename] returns [Selector leftSelector]
    @init{
        boolean relationSelector = false;
    }
    @after{
        if(relationSelector)
            $leftSelector = new RelationSelector(new Relation(firstSel, operator, secondSel));
        else
            $leftSelector = firstSel;
    }:
    firstSel=getSelector[tablename] (operator=getOperator secondSel=getRightTermInAssignment[tablename] { relationSelector = true; })?
    //TODO: Support index for collections (Example: cities[2] = 'Madrid')
;

getOperator returns [Operator op]:
    T_PLUS {$op = Operator.ADD;}
    | T_SUBTRACT {$op = Operator.SUBTRACT;}
    | T_ASTERISK {$op = Operator.MULTIPLICATION;}
    | T_SLASH {$op = Operator.DIVISION;}
;

getComparator returns [Operator op]:
    T_EQUAL {$op = Operator.EQ;}
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
    | identProp=getSelector[tablename] T_EQUAL valueProp=getSelector[tablename] {$opt=new Option(identProp, valueProp);}
;

getSelectors[TableName tablename] returns [ArrayList list]
    @init{
        list = new ArrayList<Selector>();
    }:
    term1=getSelector[tablename] {list.add(term1);}
    (T_COMMA termN=getSelector[tablename] {list.add(termN);})*
;

getAliasedTableID[Map tablesAliasesMap] returns [TableName result]:
	tableN=getTableName (T_AS alias=T_IDENT {tablesAliasesMap.put($alias.text, tableN.toString()); tableN.setAlias($alias.text); })?
	{result = tableN;}
;

getColumnName[TableName tablename] returns [ColumnName columnName]:
    ( ident1=T_IDENT {$columnName = normalizeColumnName(tablename, $ident1.text);}
    | ident2=T_KS_AND_TN {$columnName = normalizeColumnName(tablename, $ident2.text);}
    | ident3=T_CTLG_TBL_COL {$columnName = normalizeColumnName(tablename, $ident3.text);}
    | allowedReservedWord=getAllowedReservedWord {$columnName = normalizeColumnName(tablename, allowedReservedWord);})
;

getIndexName returns [IndexName indexName]:
    ident=(T_KS_AND_TN
          | T_CTLG_TBL_COL
          ) { $indexName = normalizeIndexName($ident.text); }
;

getAllowedReservedWord returns [String str]:
    ident=(T_SEC
    | T_SECS
    | T_SECOND
    | T_SECONDS
    | T_MINS
    | T_MINUTE
    | T_MINUTES
    | T_HOUR
    | T_HOURS
    | T_DAY
    | T_DAYS
    | T_COUNT
    | T_PLAN
    | T_TYPE
    | T_LIMIT
    | T_PROCESS
    | T_STORAGE
    | T_OPTIONS
    | T_CATALOG
    | T_MAP
    | T_INT
    | T_BOOLEAN
    | T_TEXT
    | T_LUCENE
    | T_KEY)
    { $str = new String($ident.text); }
;

getGenericID returns [String str]:
    arw=getAllowedReservedWord { $str = arw; }
    | ident=T_IDENT { $str = $ident.text; }
;

getTableName returns [TableName tablename]:
    (ident1=getGenericID {tablename = normalizeTableName(ident1);}
    | ident2=T_KS_AND_TN {tablename = normalizeTableName($ident2.text);})
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
