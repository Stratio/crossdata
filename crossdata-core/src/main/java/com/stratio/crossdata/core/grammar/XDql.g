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
    backtrack = true;
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
    import java.util.LinkedHashSet;
    import java.util.Map;
    import java.util.Set;
    import java.util.HashSet;
    import org.apache.commons.lang3.tuple.MutablePair;
    import com.stratio.crossdata.common.exceptions.*;
    import com.stratio.crossdata.common.utils.Constants;
    import java.util.UUID;
}

@members {

    private String sessionCatalog = "";

    private TableName workaroundTable = null;

    private Map workaroundTablesAliasesMap = new LinkedHashMap<String, String>();

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
            if((sessionCatalog == null) || (sessionCatalog.isEmpty())){
                throwParsingException("Catalog can't be empty");
            }
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
T_TRUNCATE: T R U N C A T E;
T_CREATE: C R E A T E;
T_REGISTER: R E G I S T E R;
T_ALTER: A L T E R;
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
T_TRUE: T R U E;
T_FALSE: F A L S E;
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
T_WHEN: W H E N;
T_IN: I N;
T_FROM: F R O M;
T_DELETE: D E L E T E;
T_WINDOW: W I N D O W;
T_LAST: L A S T;
T_ROWS: R O W S;
T_INNER: I N N E R;
T_JOIN: J O I N;
T_OUTER: O U T E R;
T_LEFT: L E F T;
T_RIGHT: R I G H T;
T_CROSS: C R O S S;
T_NATURAL: N A T U R A L;
T_FULL: F U L L;
T_BY: B Y;
T_LIMIT: L I M I T;
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
T_MIN: M I N;
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
T_BOOL: B O O L;
T_BOOLEAN: B O O L E A N;
T_VARCHAR: V A R C H A R;
T_TEXT: T E X T;
T_BIGINT: B I G I N T;

T_IMPORT: I M P O R T;
T_DISCOVER: D I S C O V E R;
T_METADATA: M E T A D A T A;
T_PRIORITY: P R I O R I T Y;
T_PAGINATION: P A G I N A T I O N;

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
    (T_WITH (T_OPTIONS)? j=getJson)?
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
        acs = new AlterClusterStatement(new ClusterName($clusterName.text), ifExists, j);
    }:
    T_ALTER T_CLUSTER (T_IF T_EXISTS {ifExists = true;} )? clusterName=T_IDENT T_WITH j=getJson
;

// ========================================================
// CONNECTOR
// ========================================================

attachConnectorStatement returns [AttachConnectorStatement acs]
    @init{
        optionsJson = "";
        int priority = 5;
        int pagination = 0;
    }
    @after{
        $acs = new AttachConnectorStatement(new ConnectorName($connectorName.text),
        new ClusterName($clusterName.text), optionsJson, priority, pagination);
    }:
    T_ATTACH T_CONNECTOR connectorName=T_IDENT T_TO clusterName=T_IDENT (T_WITH (T_OPTIONS)? optionsJson=getJson)?
    (T_AND T_PRIORITY T_EQUAL number=T_CONSTANT { priority = Integer.parseInt($number.text); } )?
    (T_AND T_PAGINATION T_EQUAL pageSize=T_CONSTANT { pagination = Integer.parseInt($pageSize.text); } )?
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

//DELETE FROM table1 WHERE field1=value1 AND field2=value2;
deleteStatement returns [DeleteStatement ds]
	@after{
		$ds = new DeleteStatement(tablename, whereClauses);
	}:
	T_DELETE T_FROM tablename=getTableName
	(T_WHERE whereClauses=getWhereClauses[tablename]
	    { if(!checkWhereClauses(whereClauses)) throwParsingException("Left terms of where clauses must be a column name"); }
	)?
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
//CREATE FULL_TEXT INDEX index1 ON table1 (field1, field2) WITH {'class': 'com.company.Index.class'};
//CREATE CUSTOM INDEX index1 ON table1 (field1, field2) WITH {'opt1': 'val1', 'opt2': 'val2'};
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
		field=getColumnName[tablename] {
		    if(!$cis.addColumn(field))
		        throwParsingException("Identifier " + field + " is repeated.");
		}
	)*
	T_END_PARENTHESIS
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
        ArrayList<Relation> assignations = new ArrayList<>();
    }:
    T_UPDATE tablename=getTableName
    T_SET assig1=getAssignment[tablename] {assignations.add(assig1);} (T_COMMA assigN=getAssignment[tablename] {assignations.add(assigN);})*
    (T_WHERE whereClauses=getWhereClauses[tablename])?
    (T_WITH j=getJson)?
    {
        if(!checkWhereClauses(whereClauses)) throwParsingException("Left terms of where clauses must be a column name");
        $pdtbst = new UpdateTableStatement(tablename, assignations, whereClauses, j);
    }
;

createTableStatement returns [CreateTableStatement crtast]
    @init{
        LinkedHashMap<ColumnName, ColumnType> columns = new LinkedHashMap<>();
        LinkedHashSet<ColumnName> partitionKey = new LinkedHashSet<>();
        LinkedHashSet<ColumnName> clusterKey = new LinkedHashSet<>();
        boolean ifNotExists = false;
        boolean isExternal = false;
    }:
    (T_CREATE | T_REGISTER {isExternal = true;}) tableType=getTableType T_TABLE (T_IF T_NOT T_EXISTS {ifNotExists = true;})?
    tablename=getTableName { if(!tablename.isCompletedName()) throwParsingException("Catalog is missing") ; }
    T_ON T_CLUSTER clusterID=T_IDENT
    T_START_PARENTHESIS
        id1=getColumnName[tablename] type1=getDataType (T_PRIMARY T_KEY { if(!partitionKey.add(id1))
                throwParsingException("Identifier " + id1 + " is repeated."); } )?
        { columns.put(id1, type1);}
        (T_COMMA idN=getColumnName[tablename] typeN=getDataType {
                if(columns.put(idN, typeN) != null) throwParsingException("Identifier " + idN + " is repeated.");
        } )*
        (T_COMMA T_PRIMARY T_KEY T_START_PARENTHESIS
                (idPk1=getColumnName[tablename] { if(!partitionKey.isEmpty()) throwParsingException("Partition key was previously defined");
                                                 if(!partitionKey.add(idPk1))
                                                 throwParsingException("Identifier " + idPk1 + " is repeated.");}
                | T_START_PARENTHESIS
                    idParK1=getColumnName[tablename] { if(!partitionKey.isEmpty()) throwParsingException("Partition key was previously defined");
                                                       if(!partitionKey.add(idParK1))
                                                       throwParsingException("Identifier " + idParK1 + " is repeated.");}
                    (T_COMMA idParKN=getColumnName[tablename] { if(!partitionKey.add(idParKN))
                            throwParsingException("Identifier " + idParKN + " is repeated.");}
                    )*
                T_END_PARENTHESIS)
                (T_COMMA idPkN=getColumnName[tablename] { if(!clusterKey.add(idPkN))
                        throwParsingException("Identifier " + idPkN + " is repeated.");})*
        T_END_PARENTHESIS)?
    T_END_PARENTHESIS (T_WITH j=getJson)?
    {
        if(partitionKey.isEmpty()) throwParsingException("Primary Key definition missing");
        $crtast = new CreateTableStatement(tableType, tablename, new ClusterName($clusterID.text), columns,
        partitionKey, clusterKey, isExternal);
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
    ((T_ALTER column=getColumnName[tablename] (T_TYPE)? dataType=getDataType {option=AlterOperation.ALTER_COLUMN;}
        |T_ADD column=getColumnName[tablename] dataType=getDataType {option=AlterOperation.ADD_COLUMN;}
        |T_DROP column=getColumnName[tablename] {option=AlterOperation.DROP_COLUMN;}
    ) (T_WITH j=getJson)?
    | T_WITH {option=AlterOperation.ALTER_OPTIONS;} j=getJson)
    { $altast = new AlterTableStatement(tablename, column, dataType, j, option); }
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
        boolean implicitJoin = false;
        boolean subqueryInc = false;
        JoinType joinType=JoinType.INNER;
    }
    @after{
        slctst.setFieldsAliases(fieldsAliasesMap);
        slctst.setTablesAliases(tablesAliasesMap);
    }:
    T_SELECT selClause=getSelectExpression[fieldsAliasesMap]
     T_FROM (T_START_PARENTHESIS subquery=selectStatement T_END_PARENTHESIS subqueryAlias=getSubqueryAlias { tablename = new TableName(Constants.VIRTUAL_CATALOG_NAME, subqueryAlias); tablename.setAlias(subqueryAlias) ; subqueryInc = true;}
            | tablename=getAliasedTableID[tablesAliasesMap])
    (T_COMMA { implicitJoin = true; workaroundTablesAliasesMap = tablesAliasesMap;}
    identJoin=getAliasedTableID[workaroundTablesAliasesMap] { tablesAliasesMap = workaroundTablesAliasesMap; })?
    {$slctst = new SelectStatement(selClause, tablename);}
    (T_COMMA { implicitJoin = true; workaroundTablesAliasesMap = tablesAliasesMap;}
    identJoin=getAliasedTableID[workaroundTablesAliasesMap] { tablesAliasesMap = workaroundTablesAliasesMap; })?
    (T_WITH T_WINDOW {windowInc = true;} window=getWindow)?
    ((T_INNER {joinType=JoinType.INNER;}
        | T_RIGHT T_OUTER {joinType=JoinType.RIGHT_OUTER;}
        | T_RIGHT {joinType=JoinType.RIGHT;}
        | T_LEFT {joinType=JoinType.LEFT;}
        | T_LEFT T_OUTER {joinType=JoinType.LEFT_OUTER;}
        | T_FULL T_OUTER {joinType=JoinType.FULL_OUTER;}
        | T_NATURAL {joinType=JoinType.NATURAL;}
        | T_CROSS {joinType=JoinType.CROSS;})?
    T_JOIN {workaroundTablesAliasesMap = tablesAliasesMap;}
    identJoin=getAliasedTableID[workaroundTablesAliasesMap]
    T_ON { tablesAliasesMap = workaroundTablesAliasesMap; }
    joinRelations=getWhereClauses[null] {$slctst.addJoin(new InnerJoin(identJoin, joinRelations, joinType));})*
    (T_WHERE { if(!implicitJoin) whereInc = true;} whereClauses=getWhereClauses[null])?
    (T_ORDER T_BY {orderInc = true;} orderByClauses=getOrdering[null])?
    (T_GROUP T_BY {groupInc = true;} groupByClause=getGroupBy[null])?
    (T_LIMIT {limitInc = true;} constant=T_CONSTANT)?
    {
        if(!checkWhereClauses(whereClauses)) throwParsingException("Left terms of where clauses must be a column name");

        if(windowInc)
            $slctst.setWindow(window);
        if(whereInc)
             $slctst.setWhere(whereClauses);
        if(orderInc)
             $slctst.setOrderByClauses(orderByClauses);
        if(groupInc)
             $slctst.setGroupByClause(new GroupByClause(groupByClause));
        if(limitInc)
             $slctst.setLimit(Integer.parseInt($constant.text));
        if(subqueryInc)
             $slctst.setSubquery(subquery, subqueryAlias);
        if(implicitJoin)
             $slctst.addJoin(new InnerJoin(identJoin, whereClauses));

    }
;

getSubqueryAlias returns [String sAlias]
   @init{
        boolean aliasInc = false;
   }
   @after{
        if(aliasInc)
            $sAlias = $alias.text;
        else
            $sAlias = UUID.randomUUID().toString();
   }:
((T_AS)? alias=T_IDENT {aliasInc = true;})?
;


insertIntoStatement returns [InsertIntoStatement nsntst]
    @init{
        LinkedList<ColumnName> ids = new LinkedList<>();
        boolean ifNotExists = false;
        int typeValues = InsertIntoStatement.TYPE_VALUES_CLAUSE;
        LinkedList<Selector> cellValues = new LinkedList<>();
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
    (T_WHEN whereClauses=getWhereClauses[tablename])?
    (T_WITH j=getJson)?
    {
        if((!ids.isEmpty()) && (!cellValues.isEmpty()) && (ids.size() != cellValues.size())) throwParsingException("Number of columns and number of values differ");
        $nsntst = new InsertIntoStatement(tablename, ids, selectStmnt, cellValues, ifNotExists, whereClauses, j, typeValues);
    }
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
	T_TRUNCATE tablename=getTableName
    {
        $trst = new TruncateStatement(tablename);
	}
;

// ========================================================
// IMPORTS
// ========================================================

// DISCOVER METADATA ON CLUSTER cluster_name;
// IMPORT CATALOGS FROM CLUSTER cluster_name;
// IMPORT CATALOG catalog_name FROM CLUSTER cluster_name;
// IMPORT TABLE catalog_name.table_name FROM CLUSTER cluster_name;
importMetadataStatement returns [ImportMetadataStatement imst]
    @init{
        boolean discover = false;
        CatalogName catalog = null;
    }:
    (T_DISCOVER { discover = true; } T_METADATA T_ON
    | T_IMPORT
        ( T_CATALOGS
        | T_CATALOG catalogName=T_IDENT { catalog = new CatalogName($catalogName.text); }
        | T_TABLE table=getTableName)
      T_FROM
    )
    T_CLUSTER clusterName=T_IDENT
    {
        $imst = new ImportMetadataStatement(new ClusterName($clusterName.text), catalog, table, discover);
    }
;

// ========================================================
// CROSSDATA STATEMENT
// ========================================================

crossdataStatement returns [CrossdataStatement st]:
    (T_START_BRACKET
        ( gID=getGenericID { sessionCatalog = gID;} )?
    T_END_BRACKET T_COMMA)?
    (
    st_nsnt  = insertIntoStatement { $st = st_nsnt;}
    | st_slct = selectStatement { $st = st_slct;}
    | st_crta = createTableStatement { $st = st_crta;}
    | st_altt = alterTableStatement { $st = st_altt;}
    | st_pdtb = updateTableStatement { $st = st_pdtb; }
    | st_tbdr = dropTableStatement { $st = st_tbdr; }
    | st_trst = truncateStatement { $st = st_trst; }
    | st_dlst = deleteStatement { $st = st_dlst; }
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
    | st_imst = importMetadataStatement { $st = st_imst; }
    )
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
    T_BIGINT { $dataType=new ColumnType(DataType.BIGINT); }
    | T_BOOL { $dataType=new ColumnType(DataType.BOOLEAN); }
    | T_BOOLEAN { $dataType=new ColumnType(DataType.BOOLEAN); }
    | T_DOUBLE { $dataType=new ColumnType(DataType.DOUBLE); }
    | T_FLOAT { $dataType=new ColumnType(DataType.FLOAT); }
    | T_INT { $dataType=new ColumnType(DataType.INT); }
    | T_INTEGER { $dataType=new ColumnType(DataType.INT); }
    | T_TEXT { $dataType=new ColumnType(DataType.TEXT); }
    | T_VARCHAR { $dataType=new ColumnType(DataType.VARCHAR); }
    | nativeType=T_IDENT { $dataType = new ColumnType(DataType.NATIVE);
                           $dataType.setDBMapping($nativeType.text, Object.class);
                           $dataType.setODBCType($nativeType.text);
                         }
;

getCollectionType returns [ColumnType dataType]:
    T_SET { $dataType = new ColumnType(DataType.SET); }
    | T_LIST { $dataType = new ColumnType(DataType.LIST); }
;

getMapType returns [ColumnType dataType]:
    T_MAP { $dataType = new ColumnType(DataType.MAP); }
;

getOrdering[TableName tablename] returns [List<OrderByClause> orderByClauses]
    @init{
        List<OrderByClause> sels = new ArrayList<>();
        OrderDirection dir = null;
    }
    @after{
        $orderByClauses = sels;
    }:
    ident1=getSelector[tablename] {dir = OrderDirection.ASC;}
        (T_ASC | T_DESC { dir = OrderDirection.DESC; } )?
    {sels.add(new OrderByClause(dir, ident1));}
    (T_COMMA identN=getSelector[tablename] {dir = OrderDirection.ASC;}
        (T_ASC | T_DESC { dir = OrderDirection.DESC; } )?
    {sels.add(new OrderByClause(dir, identN));})*
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
        workaroundTable = tablename;
    }:
    rel1=getRelation[tablename] {clauses.add(rel1);}
        (T_AND relN=getRelation[workaroundTable] {clauses.add(relN);})*
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
        List<Selector> selectors = new ArrayList<>();
    }
    @after{
        se = new SelectExpression(selectors);
    }:
    s=getSelector[null] { if(s == null) throwParsingException("Column name not found");}
        (T_AS alias1=getGenericID {
            s.setAlias(alias1);
            fieldsAliasesMap.put(alias1, s.toString());})? {selectors.add(s);}
    (T_COMMA s=getSelector[null] { if(s == null) throwParsingException("Column name not found");}
        (T_AS aliasN=getGenericID {
            s.setAlias(aliasN);
            fieldsAliasesMap.put(aliasN, s.toString());})? {selectors.add(s);})*
;

getSelector[TableName tablename] returns [Selector sel]
    @init{
        LinkedList<Selector> params = new LinkedList<>();
        String name = null;
        boolean relationSelector = false;
        Selector firstSelector = null;
        workaroundTable = tablename;
    }
    @after{
        if(relationSelector)
            sel = new RelationSelector(new Relation(firstSelector, operator, secondSelector));
        else
            sel = firstSelector;
    }:
    (
        T_START_PARENTHESIS
            selectStmnt=selectStatement { firstSelector = new ExtendedSelectSelector(selectStmnt, sessionCatalog); }
        T_END_PARENTHESIS
    |
        functionName=getFunctionName
        T_START_PARENTHESIS
            (select1=getSelector[tablename] {params.add(select1);}
                (T_COMMA selectN=getSelector[tablename] {params.add(selectN);})*
            )?
        T_END_PARENTHESIS { String functionStr = functionName;
                            firstSelector = new FunctionSelector(tablename, functionStr, params);}
    |
        (columnName=getColumnName[tablename] {firstSelector = new ColumnSelector(columnName);}
        | floatingNumber=T_FLOATING {firstSelector = new FloatingPointSelector(tablename, $floatingNumber.text);}
        | constant=T_CONSTANT {firstSelector = new IntegerSelector(tablename, $constant.text);}
        | T_FALSE {firstSelector = new BooleanSelector(tablename, false);}
        | T_TRUE {firstSelector = new BooleanSelector(tablename, true);}
        | T_ASTERISK {firstSelector = new AsteriskSelector(tablename);}
        | qLiteral=QUOTED_LITERAL {firstSelector = new StringSelector(tablename, $qLiteral.text);})
    )
    (operator=getOperator {relationSelector=true;} secondSelector=getSelector[workaroundTable])?
;

getGenericSelector[TableName tablename] returns [Selector selector]
    @init{
        Selector firstSelector = null;
        boolean relationSelector = false;
        workaroundTable = tablename;
    }
    @after{
        if(relationSelector)
            selector = new RelationSelector(new Relation(firstSelector, operator, secondSelector));
        else
            selector = firstSelector;
    }:
    (columnName=getColumnName[tablename] {firstSelector = new ColumnSelector(columnName);}
    | floatingNumber=T_FLOATING {firstSelector = new FloatingPointSelector(tablename, $floatingNumber.text);}
    | constant=T_CONSTANT {firstSelector = new IntegerSelector(tablename, $constant.text);}
    | T_FALSE {firstSelector = new BooleanSelector(tablename, false);}
    | T_TRUE {firstSelector = new BooleanSelector(tablename, true);}
    | T_ASTERISK {firstSelector = new AsteriskSelector(tablename);}
    | qLiteral=QUOTED_LITERAL {firstSelector = new StringSelector(tablename, $qLiteral.text);})
    (operator=getOperator
        secondSelector=getRightTermInAssignment[workaroundTable] {relationSelector = true;} )?
;

getAssignment[TableName tablename] returns [Relation assign]
    @init{
        ColumnSelector leftTerm = null;
    }
    @after{
        $assign = new Relation(leftTerm, Operator.EQ, rightTerm);
    }:
    columnName=getColumnName[tablename] {leftTerm = new ColumnSelector(columnName);} T_EQUAL rightTerm=getRightTermInAssignment[tablename]
;


getRightTermInAssignment[TableName tablename] returns [Selector leftSelector]
    @init{
        boolean relationSelector = false;
        workaroundTable = tablename;
    }
    @after{
        if(relationSelector)
            $leftSelector = new RelationSelector(tablename, new Relation(firstSel, operator, secondSel));
        else
            $leftSelector = firstSel;
    }:
    firstSel=getSelector[tablename]
    (operator=getOperator
        secondSel=getRightTermInAssignment[workaroundTable] { relationSelector = true; })?
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

getSelectors[TableName tablename] returns [ArrayList list]
    @init{
        list = new ArrayList<Selector>();
    }:
    term1=getSelector[tablename] {list.add(term1);}
    (T_COMMA termN=getSelector[tablename] {list.add(termN);})*
;

getAliasedTableID[Map tablesAliasesMap] returns [TableName result]:
	tableN=getTableName ((T_AS)? alias=T_IDENT {tablesAliasesMap.put($alias.text, tableN.toString()); tableN.setAlias
	($alias.text); })?
	{result = tableN;}
;

getFunctionName returns [String functionName]:
    ( ident1=T_IDENT {$functionName = $ident1.text;}
    | allowedReservedWord=getAllowedReservedWord {$functionName = allowedReservedWord;})
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
    | T_MIN
    | T_MINS
    | T_MINUTE
    | T_MINUTES
    | T_HOUR
    | T_HOURS
    | T_DAY
    | T_DAYS
    | T_PLAN
    | T_TYPE
    | T_LIMIT
    | T_PROCESS
    | T_STORAGE
    | T_OPTIONS
    | T_CATALOG
    | T_MAP
    | T_INT
    | T_BOOL
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
