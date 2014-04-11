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

package com.stratio.meta.core.statements;

import com.datastax.driver.core.ColumnMetadata;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.TableMetadata;
import com.datastax.driver.core.querybuilder.Clause;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.datastax.driver.core.querybuilder.Select.Where;
import com.stratio.meta.common.data.DeepResultSet;
import com.stratio.meta.common.result.QueryResult;
import com.stratio.meta.common.result.Result;
import com.stratio.meta.core.metadata.CustomIndexMetadata;
import com.stratio.meta.core.metadata.MetadataManager;
import com.stratio.meta.core.structures.*;
import com.stratio.meta.core.utils.MetaPath;
import com.stratio.meta.core.utils.MetaStep;
import com.stratio.meta.core.utils.ParserUtils;
import com.stratio.meta.core.utils.Tree;

import java.util.*;
import java.util.regex.Pattern;

/**
 * A object with the mapping of a SELECT statement from the META language.
 */
public class SelectStatement extends MetaStatement {

    private SelectionClause selectionClause;
    private boolean keyspaceInc = false;
    private String keyspace;
    private String tableName;
    private boolean windowInc;
    private WindowSelect window;
    private boolean joinInc;
    private InnerJoin join;
    private boolean whereInc;
    private ArrayList<Relation> where;
    private boolean orderInc;
    private ArrayList<com.stratio.meta.core.structures.Ordering> order;
    private boolean groupInc;
    private GroupBy group;    
    private boolean limitInc;
    private int limit;
    private boolean disableAnalytics;
    private boolean needsAllowFiltering = false;


    //TODO: We should probably remove this an pass it as parameters.
    private MetadataManager _metadata = null;
    private TableMetadata _tableMetadataFrom = null;
    private TableMetadata _tableMetadataJoin = null;
    private HashMap<String, Collection<ColumnMetadata>> columns = new HashMap<>();

    public SelectStatement(SelectionClause selectionClause, String tableName,
                           boolean windowInc, WindowSelect window, 
                           boolean joinInc, InnerJoin join, 
                           boolean whereInc, ArrayList<Relation> where,
                           boolean orderInc, ArrayList<com.stratio.meta.core.structures.Ordering> order,
                           boolean groupInc, GroupBy group, 
                           boolean limitInc, int limit, 
                           boolean disableAnalytics) {
        this.command = false;
        this.selectionClause = selectionClause;
        this.tableName = tableName;
        if(this.tableName.contains(".")){
            String[] ksAndTablename = this.tableName.split("\\.");
            keyspace = ksAndTablename[0];
            this.tableName = ksAndTablename[1];
            keyspaceInc = true;
        }
        this.windowInc = windowInc;
        this.window = window;
        this.joinInc = joinInc;
        this.join = join;
        this.whereInc = whereInc;
        this.where = where;
        this.orderInc = orderInc;
        this.order = order;
        this.groupInc = groupInc;
        this.group = group;
        this.limitInc = limitInc;
        this.limit = limit;
        this.disableAnalytics = disableAnalytics;
    }        
    
    public SelectStatement(SelectionClause selectionClause, String tableName) {
        this(selectionClause, tableName, false, null, false, null, false, null, false, null, false, null, false, 0, false);
    }             
    
    public SelectStatement(String tableName) {
        this(null, tableName, false, null, false, null, false, null, false, null, false, null, false, 0, false);
    }

    public String getKeyspace() {
        return keyspace;
    }

    public void setKeyspace(String keyspace) {
        this.keyspaceInc = true;
        this.keyspace = keyspace;
    }        
    
    public String getTableName() {
        return tableName;
    }

    public SelectionClause getSelectionClause() {
        return selectionClause;
    }

    public void setWindow(WindowSelect window) {
        this.windowInc = true;
        this.window = window;
    }

    public InnerJoin getJoin() {
        return join;
    }

    public void setJoin(InnerJoin join) {
        this.joinInc = true;
        this.join = join;
    }        

    public ArrayList<Relation> getWhere() {
        return where;
    }

    public void setWhere(ArrayList<Relation> where) {
        this.whereInc = true;
        this.where = where;
    }        

    public void setOrder(ArrayList<com.stratio.meta.core.structures.Ordering> order) {
        this.orderInc = true;
        this.order = order;
    }        

    public void setGroup(GroupBy group) {
        this.groupInc = true;
        this.group = group;
    }

    public void setLimit(int limit) {
        this.limitInc = true;
        this.limit = limit;
    }

    public void setDisableAnalytics(boolean disableAnalytics) {
        this.disableAnalytics = disableAnalytics;
    }                   

    public void addSelection(SelectionSelector selSelector){
        if(selectionClause == null){
            SelectionSelectors selSelectors = new SelectionSelectors();
            selectionClause = new SelectionList(selSelectors);
        }
        SelectionList selList = (SelectionList) selectionClause;
        SelectionSelectors selSelectors = (SelectionSelectors) selList.getSelection();
        selSelectors.addSelectionSelector(selSelector);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("SELECT ");
        if(selectionClause != null){
            sb.append(selectionClause.toString());
        }
        sb.append(" FROM ");
        if(keyspaceInc){
            sb.append(keyspace).append(".");
        }
        sb.append(tableName);
        if(windowInc){
            sb.append(" WITH WINDOW ").append(window.toString());
        }
        if(joinInc){
            sb.append(" INNER JOIN ").append(join.toString());
        }
        if(whereInc){
            sb.append(" WHERE ");
            sb.append(ParserUtils.stringList(where, " AND "));
        }
        if(orderInc){
            sb.append(" ORDER BY ").append(ParserUtils.stringList(order, ", "));
        }
        if(groupInc){
            sb.append(group);
        }
        if(limitInc){
            sb.append(" LIMIT ").append(limit);
        }
        if(disableAnalytics){
            sb.append(" DISABLE ANALYTICS");
        }        

        return sb.toString().replace("  ", " ");
    }

    /** {@inheritDoc} */
    @Override
    public Result validate(MetadataManager metadata, String targetKeyspace) {
        //Validate FROM keyspace
        Result result = validateKeyspaceAndTable(metadata, targetKeyspace,
                keyspaceInc, keyspace, tableName);

        if(!result.hasError() && joinInc){
            if(join.getKeyspace() != null){
                result = validateKeyspaceAndTable(metadata, targetKeyspace,
                        join.isKeyspaceInc(), join.getKeyspace(), join.getTablename());
            }
        }

        if(!result.hasError()){
            result = validateOptions();
        }

        String effectiveKs1 = targetKeyspace;
        String effectiveKs2 = targetKeyspace;
        if(keyspaceInc){
            effectiveKs1 = keyspace;
        }
        if(joinInc && join.isKeyspaceInc()){
            effectiveKs2 = join.getKeyspace();
        }

        TableMetadata tableMetadata1 = null;
        TableMetadata tableMetadata2 = null;

        if(!result.hasError()){
            tableMetadata1 = metadata.getTableMetadata(effectiveKs1, tableName);
            if(joinInc) {
                tableMetadata2 = metadata.getTableMetadata(effectiveKs2, join.getTablename());
                _tableMetadataJoin = tableMetadata2;
            }
            //Cache Metadata manager and table metadata for the getDriverStatement.
            _metadata = metadata;
            _tableMetadataFrom = tableMetadata1;

            result = validateSelectionColumns(tableMetadata1, tableMetadata2);
        }
        if(!result.hasError() && whereInc){
            result = validateWhereClause(tableMetadata1, tableMetadata2);
        }

        return result;
    }

    /**
     * Validate the supported select options.
     * @return A {@link com.stratio.meta.common.result.Result} with the validation result.
     */
    private Result validateOptions(){
        Result result = QueryResult.CreateSuccessQueryResult();
        if(windowInc){
            result= QueryResult.CreateFailQueryResult("Select with streaming options not supported.");
        }

        if(groupInc){
            result= QueryResult.CreateFailQueryResult("Select with GROUP BY clause not supported.");
        }

        if(orderInc){
            result= QueryResult.CreateFailQueryResult("Select with ORDER BY clause not supported.");
        }
        return result;
    }


    /**
     * Validate that the where clause is valid by checking that columns exists on the target
     * table and that the comparisons are semantically valid.
     * @param tableFrom The {@link com.datastax.driver.core.TableMetadata} associated with the FROM table.
     * @param tableJoin The {@link com.datastax.driver.core.TableMetadata} associated with the JOIN table.
     * @return A {@link com.stratio.meta.common.result.Result} with the validation result.
     */
    private Result validateWhereClause(TableMetadata tableFrom, TableMetadata tableJoin){
        //TODO: Check that the MATCH operator is only used in Lucene mapped columns.

        Result result = QueryResult.CreateSuccessQueryResult();
        for(Relation relation : where){
            if(Relation.TYPE_COMPARE == relation.getType()) {
                //Check comparison, =, >, <, etc.
                RelationCompare rc = RelationCompare.class.cast(relation);
                String column = rc.getIdentifiers().get(0);
                //Determine the target table the column belongs to.
                String targetTable = "any";
                if(column.contains(".")){
                    String[] tableAndColumn = column.split("\\.");
                    targetTable = tableAndColumn[0];
                    column = tableAndColumn[1];
                }

                //Check that the column exists.
                Result columnResult = findColumn(targetTable, column);
                if(columnResult.hasError()){
                    result = columnResult;
                }

                //Get the term and determine its type.
                Term t = Term.class.cast(rc.getTerms().get(0));
                boolean found = false;
                Iterator<ColumnMetadata> it = columns.get(targetTable).iterator();
                while(!found && it.hasNext()) {
                    ColumnMetadata cm = it.next();

                    if (cm.getName().equals(column)) {
                        found = true;

                        if (!cm.getType().asJavaClass().equals(t.getTermClass())) {
                            result = QueryResult.CreateFailQueryResult("Column " + column
                                    + " of type " + cm.getType().asJavaClass()
                                    + " does not accept " + t.getTermClass()
                                    + " values (" + t.toString() + ")");
                        }

                        if (Boolean.class.equals(cm.getType().asJavaClass())) {
                            boolean supported = true;
                            switch (rc.getOperator()) {
                                case ">":
                                    supported = false;
                                    break;
                                case "<":
                                    supported = false;
                                    break;
                                case ">=":
                                    supported = false;
                                    break;
                                case "<=":
                                    supported = false;
                                    break;
                                default:
                                    break;
                            }
                            if (!supported) {
                                result = QueryResult.CreateFailQueryResult("Operand " + rc.getOperator() + " not supported for" +
                                        " column " + column + ".");
                            }
                        }
                    }
                }

            if(!found) {
                    result= QueryResult.CreateFailQueryResult("Column " + column + " not found in table " + tableName);
            }


            }else if(Relation.TYPE_IN == relation.getType()){
                //TODO: Check IN relation
                result= QueryResult.CreateFailQueryResult("IN clause not supported.");
            }else if(Relation.TYPE_TOKEN == relation.getType()){
                //TODO: Check IN relation
                result= QueryResult.CreateFailQueryResult("TOKEN function not supported.");
            }else if(Relation.TYPE_BETWEEN == relation.getType()){
                //TODO: Check IN relation
                result= QueryResult.CreateFailQueryResult("BETWEEN clause not supported.");
            }
        }

        return result;
    }

    /**
     * Find a column in the selected tables.
     * @param table The target table of the column.
     * @param column The name of the column.
     * @return A {@link com.stratio.meta.common.result.Result}.
     */
    private Result findColumn(String table, String column){

        Result result = QueryResult.CreateSuccessQueryResult();
        boolean found = false;

        if(columns.get(table) != null){

            Iterator<ColumnMetadata> it = columns.get(table).iterator();
            while(!found && it.hasNext()) {
                ColumnMetadata cm = it.next();
                if (cm.getName().equals(column)) {
                    found = true;
                }
            }
            if(!found){
                result= QueryResult.CreateFailQueryResult("Column " + column + " does not " +
                        "exists in table " + table);
            }

        }else{
            result = QueryResult.CreateFailQueryResult("Column " + column
                    + " refers to table " + table + " that has not been specified on query.");
        }
        return result;
    }


    /**
     * Validate that the columns specified in the select are valid by checking
     * that the selection columns exists in the table.
     * @param tableFrom The {@link com.datastax.driver.core.TableMetadata} associated with the FROM table.
     * @param tableJoin The {@link com.datastax.driver.core.TableMetadata} associated with the JOIN table.
     * @return A {@link com.stratio.meta.common.result.Result} with the validation result.
     */
    private Result validateSelectionColumns(TableMetadata tableFrom, TableMetadata tableJoin) {
        Result result = QueryResult.CreateSuccessQueryResult();

        //Create a HashMap with the columns
        Collection<ColumnMetadata> allColumns = new ArrayList<>();
        columns.put(tableFrom.getName(), tableFrom.getColumns());
        allColumns.addAll(tableFrom.getColumns());
        if(joinInc){
            columns.put(tableJoin.getName(), tableJoin.getColumns());
            allColumns.addAll(tableJoin.getColumns());
        }
        columns.put("any", allColumns);

        Result columnResult = null;

        //Iterate through the selection columns. If the user specified count(*) skip
        if(selectionClause.getType() == SelectionClause.TYPE_SELECTION){
            SelectionList sl = SelectionList.class.cast(selectionClause);
            //Check columns only if an asterisk is not selected.
            if(sl.getSelection().getType() == Selection.TYPE_SELECTOR){
                SelectionSelectors ss = SelectionSelectors.class.cast(sl.getSelection());
                for(SelectionSelector selector : ss.getSelectors()){
                    if(selector.getSelector().getType() == SelectorMeta.TYPE_IDENT){
                        SelectorIdentifier si = SelectorIdentifier.class.cast(selector.getSelector());

                        String targetTable = "any";
                        if(si.getTablename() != null){
                            targetTable = si.getTablename();
                        }

                        columnResult = findColumn(targetTable, si.getColumnName());
                        if(columnResult.hasError()){
                            result = columnResult;
                        }
                    }else{
                        result= QueryResult.CreateFailQueryResult("Functions on selected fields not supported.");
                    }
                }
            }
        }
        return result;
    }



    /**
     * Get the processed where clause to be sent to Cassandra related with lucene
     * indexes.
     * @param metadata The {@link com.stratio.meta.core.metadata.MetadataManager} that provides
     *                 the required information.
     * @param tableMetadata The associated {@link com.datastax.driver.core.TableMetadata}.
     * @return A String array with the column name and the lucene query, or null if no index is found.
     */
    public String [] getLuceneWhereClause(MetadataManager metadata, TableMetadata tableMetadata){
        String [] result = null;
        CustomIndexMetadata luceneIndex = metadata.getLuceneIndex(tableMetadata);

        if(luceneIndex != null) {

            //TODO: Check in the validator that the query uses AND with the lucene mapped columns.
            StringBuilder sb = new StringBuilder("{query:{type:\"boolean\",must:[");

            //Iterate throughout the relations of the where clause looking for MATCH.
            for (Relation relation : where) {
                if (Relation.TYPE_COMPARE == relation.getType()
                        && relation.getOperator().equalsIgnoreCase("MATCH")) {
                    RelationCompare rc = RelationCompare.class.cast(relation);
                    String column = rc.getIdentifiers().get(0);
                    String value = rc.getTerms().get(0).toString();
                    //Generate query for column
                    String [] processedQuery = processLuceneQueryType(value);
                    sb.append("{type:\"");
                    sb.append(processedQuery[0]);
                    sb.append("\",field:\"");
                    sb.append(column);
                    sb.append("\",value:\"");
                    sb.append(processedQuery[1]);
                    sb.append("\"},");
                }
            }
            sb.replace(sb.length()-1, sb.length(), "");
            sb.append("]}}");
            result = new String[]{luceneIndex.getIndexName(), sb.toString()};
        }
        return result;
    }


    /**
     * Process a query pattern to determine the type of Lucene query.
     * The supported types of queries are:
     * <li>
     *     <ul>Wildcard: The query contains * or ?.</ul>
     *     <ul>Fuzzy: The query ends with ~ and a number.</ul>
     *     <ul>Regex: The query contains [ or ].</ul>
     *     <ul>Match: Default query, supporting escaped symbols: *, ?, [, ], etc.</ul>
     * </li>
     * @param query The user query.
     * @return An array with the type of query and the processed query.
     */
    protected String [] processLuceneQueryType(String query){
        String [] result = {"", ""};
        Pattern escaped = Pattern.compile(".*\\\\\\*.*|.*\\\\\\?.*|.*\\\\\\[.*|.*\\\\\\].*");
        Pattern wildcard = Pattern.compile(".*\\*.*|.*\\?.*");
        Pattern regex = Pattern.compile(".*\\].*|.*\\[.*");
        Pattern fuzzy = Pattern.compile(".*~\\d+");
        if(escaped.matcher(query).matches()){
            result[0] = "match";
            result[1] = query.replace("\\*", "*").replace("\\?", "?").replace("\\]", "]").replace("\\[", "[");
        }else if(regex.matcher(query).matches()) {
            result[0] = "regex";
            result[1] = query;
        }else if(fuzzy.matcher(query).matches()) {
            result[0] = "fuzzy";
            result[1] = query;
        }else if(wildcard.matcher(query).matches()) {
            result[0] = "wildcard";
            result[1] = query;
        }else{
            result[0] = "match";
            result[1] = query;
        }
        //C* Query builder doubles the ' character.
        result[1] = result[1].replaceAll("^'", "").replaceAll("'$","");
        return result;
    }

    @Override
    public String getSuggestion() {
        return this.getClass().toString().toUpperCase()+" EXAMPLE";
    }

    @Override
    public String translateToCQL() {
        StringBuilder sb = new StringBuilder(this.toString());     

        if(sb.toString().contains("TOKEN(")){
            int currentLength = 0;
            int newLength = sb.toString().length();
            while(newLength!=currentLength){
                currentLength = newLength;
                //sb = new StringBuilder(sb.toString().replaceAll("(.*[=|<|>|<=|>=|<>|LIKE][\\s]?TOKEN\\()([^'][^\\)]+)(\\).*)", "$1'$2'$3"));
                sb = new StringBuilder(sb.toString().replaceAll("(.*)" //$1
                        + "(=|<|>|<=|>=|<>|LIKE)" //$2
                        + "(\\s?)" //$3
                        + "(TOKEN\\()" //$4
                        + "([^'][^\\)]+)" //$5
                        + "(\\).*)", //$6
                "$1$2$3$4'$5'$6"));
                sb = new StringBuilder(sb.toString().replaceAll("(.*TOKEN\\(')" //$1
                        + "([^,]+)" //$2
                        + "(,)" //$3
                        + "(\\s*)" //$4
                        + "([^']+)" //$5
                        + "(')" //$6
                        + "(\\).*)", //$7 
                "$1$2'$3$4'$5$6$7"));
                sb = new StringBuilder(sb.toString().replaceAll("(.*TOKEN\\(')" //$1
                        + "(.+)" //$2
                        + "([^'])" //$3
                        + "(,)" //$4
                        + "(\\s*)" //$5
                        + "([^']+)" //$6
                        + "(')" //$7
                        + "(\\).*)", //$8 
                "$1$2$3'$4$5'$6$7$8"));
                sb = new StringBuilder(sb.toString().replaceAll("(.*TOKEN\\(')" //$1
                        + "(.+)" //$2
                        + "([^'])" //$3
                        + "(,)" //$4
                        + "(\\s*)" //$5
                        + "([^']+)" //$6
                        + "(')" //$7
                        + "([^TOKEN]+)" //$8
                        + "('\\).*)", //$9 
                "$1$2$3'$4$5'$6$7$8$9"));
                newLength = sb.toString().length();
            }          
        }

        return sb.toString();
    }

    
    @Override
    public Statement getDriverStatement() {                
        SelectionClause selClause = this.selectionClause;                                                
        Select.Builder builder;
        
        if(this.selectionClause.getType() == SelectionClause.TYPE_COUNT){
            builder = QueryBuilder.select().countAll();
        } else {
            SelectionList selList = (SelectionList) selClause;
            if(selList.getSelection().getType() != Selection.TYPE_ASTERISK){            
                Select.Selection selection = QueryBuilder.select();
                if(selList.isDistinct()){
                    selection = selection.distinct();
                }
                SelectionSelectors selSelectors = (SelectionSelectors) selList.getSelection();
                for(SelectionSelector selSelector: selSelectors.getSelectors()){
                    SelectorMeta selectorMeta = selSelector.getSelector();
                    if(selectorMeta.getType() == SelectorMeta.TYPE_IDENT){
                        SelectorIdentifier selIdent = (SelectorIdentifier) selectorMeta;    
                        if(selSelector.isAliasInc()){                            
                            selection = selection.column(selIdent.getIdentifier()).as(selSelector.getAlias());
                        } else {
                            selection = selection.column(selIdent.getIdentifier());                        
                        }
                    } else if (selectorMeta.getType() == SelectorMeta.TYPE_FUNCTION){                        
                        SelectorFunction selFunction = (SelectorFunction) selectorMeta;
                        ArrayList<SelectorMeta> params = selFunction.getParams();
                        Object[] innerFunction = new Object[params.size()];
                        int pos = 0;
                        for(SelectorMeta selMeta: params){
                            innerFunction[pos] = QueryBuilder.raw(selMeta.toString());
                            pos++;
                        }
                        selection = selection.fcall(selFunction.getName(), innerFunction);
                    }               
                }
                builder = selection;
            } else {
                builder = QueryBuilder.select().all();
            }             
        }                                     
                        
        Select sel;
        
        if(this.keyspaceInc){
            sel = builder.from(this.keyspace, this.tableName);
        } else {
            sel = builder.from(this.tableName);
        }
        
        if(this.limitInc){
            sel.limit(this.limit);
        }                
        
        if(this.orderInc){
            com.datastax.driver.core.querybuilder.Ordering[] orderings = new com.datastax.driver.core.querybuilder.Ordering[order.size()];
            int nOrdering = 0;
            for(com.stratio.meta.core.structures.Ordering metaOrdering: this.order){
                if(metaOrdering.isDirInc() && (metaOrdering.getOrderDir() == OrderDirection.DESC)){
                    orderings[nOrdering] = QueryBuilder.desc(metaOrdering.getIdentifier());
                } else {
                    orderings[nOrdering] = QueryBuilder.asc(metaOrdering.getIdentifier());
                }
                nOrdering++;
            }
            sel.orderBy(orderings); 
        }
        
        if(this.needsAllowFiltering){
            sel.allowFiltering();
        }
        
        Where whereStmt = null;
        if(this.whereInc){
            String [] luceneWhere = getLuceneWhereClause(_metadata, _tableMetadataFrom);
            if(luceneWhere != null){
                Clause lc = QueryBuilder.eq(luceneWhere[0], luceneWhere[1]);
                whereStmt = sel.where(lc);
            }
            for(Relation metaRelation: this.where){
                Clause clause = null;
                String name;
                Object value;                
                switch(metaRelation.getType()){
                    case Relation.TYPE_COMPARE:
                        RelationCompare relCompare = (RelationCompare) metaRelation;
                        name = relCompare.getIdentifiers().get(0);
                        value = relCompare.getTerms().get(0).getTermValue();
                        if(value.toString().matches("[0123456789\\.]+")){
                            value = Integer.parseInt(value.toString());
                        } else if (value.toString().contains("-")) {
                            value = UUID.fromString(value.toString());
                        }
                        switch(relCompare.getOperator().toUpperCase()){
                            case "=":
                                clause = QueryBuilder.eq(name, value);
                                break;
                            case ">":
                                clause = QueryBuilder.gt(name, value);
                                break;
                            case ">=":
                                clause = QueryBuilder.gte(name, value);
                                break;
                            case "<":
                                clause = QueryBuilder.lt(name, value);
                                break;
                            case "<=":
                                clause = QueryBuilder.lte(name, value);
                                break;
                            case "MATCH": //Processed as LuceneIndex
                                break;
                            default:
                                clause = null;
                                throw new UnsupportedOperationException("'"+relCompare.getOperator()+"' operator not supported by C*");
                        }                                  
                        break;
                    case Relation.TYPE_IN:
                        RelationIn relIn = (RelationIn) metaRelation;
                        ArrayList<Term> terms = relIn.getTerms();                        
                        Object[] values = new Object[relIn.numberOfTerms()];                        
                        int nTerm = 0;
                        for(Term term: terms){
                            values[nTerm] = term.getTermValue();
                            nTerm++;
                        }     
                        if(values[0].toString().matches("[0123456789\\.]+")){
                            int[] intValues = new int[relIn.numberOfTerms()];
                            for(int i= 0; i<values.length; i++){
                                intValues[i] = Integer.parseInt(values[i].toString());
                            }
                            clause = QueryBuilder.in(relIn.getIdentifiers().get(0), intValues);
                            break;
                        }
                        clause = QueryBuilder.in(relIn.getIdentifiers().get(0), values);
                        break;
                    case Relation.TYPE_TOKEN:
                        RelationToken relToken = (RelationToken) metaRelation;
                        ArrayList<String> names = relToken.getIdentifiers();
                        if(!relToken.isRighSideTokenType()){
                            value = relToken.getTerms().get(0).getTermValue();
                            switch(relToken.getOperator()){
                                case "=":
                                    clause = QueryBuilder.eq(QueryBuilder.token((String[]) names.toArray()), value);
                                    break;
                                case ">":
                                    clause = QueryBuilder.gt(QueryBuilder.token((String[]) names.toArray()), value);
                                    break;
                                case ">=":
                                    clause = QueryBuilder.gte(QueryBuilder.token((String[]) names.toArray()), value);
                                    break;
                                case "<":
                                    clause = QueryBuilder.lt(QueryBuilder.token((String[]) names.toArray()), value);
                                    break;
                                case "<=":
                                    clause = QueryBuilder.lte(QueryBuilder.token((String[]) names.toArray()), value);
                                    break;
                                default:
                                    clause = null;
                                    throw new UnsupportedOperationException("'"+relToken.getOperator()+"' operator not supported by C*");
                            }
                        } else {
                            return null;
                        }
                        break;
                }
                if(clause != null){
                    if(whereStmt == null){                
                        whereStmt = sel.where(clause);
                    } else {
                        whereStmt = whereStmt.and(clause);
                    }
                }
            }
        } else {
            whereStmt = sel.where();
        }

        return whereStmt;
    }
    
    @Override
    public DeepResultSet executeDeep() {
        return new DeepResultSet();
    }

    @Override
    public Tree getPlan() {
        Tree steps = new Tree();
        if(joinInc){

            SelectStatement firstSelect = new SelectStatement(tableName);
            if(keyspaceInc){
                firstSelect.setKeyspace(this.keyspace);
            }

            SelectStatement secondSelect = new SelectStatement(this.join.getTablename());
            if(this.join.isKeyspaceInc()){
                secondSelect.setKeyspace(this.join.getKeyspace());
            }

            SelectStatement joinSelect = new SelectStatement("");

            // ADD FIELDS OF THE JOIN
            Map<String, String> fields = this.join.getFields();
            for(String key: fields.keySet()){                
                String value = fields.get(key);
                if(key.split("\\.")[0].trim().equalsIgnoreCase(tableName)){
                    firstSelect.addSelection(new SelectionSelector(new SelectorIdentifier(key.split("\\.")[1])));
                    secondSelect.addSelection(new SelectionSelector(new SelectorIdentifier(value.split("\\.")[1])));
                } else {
                    firstSelect.addSelection(new SelectionSelector(new SelectorIdentifier(value.split("\\.")[1])));
                    secondSelect.addSelection(new SelectionSelector(new SelectorIdentifier(key.split("\\.")[1])));
                }
            }

            // ADD FIELDS OF THE SELECT
            SelectionList selectionList = (SelectionList) this.selectionClause;
            SelectionSelectors selection = (SelectionSelectors) selectionList.getSelection();
            for (SelectionSelector ss: selection.getSelectors()){
                SelectorIdentifier si = (SelectorIdentifier) ss.getSelector();

                if(si.getTablename().equalsIgnoreCase(tableName)){
                    firstSelect.addSelection(new SelectionSelector(new SelectorIdentifier(si.getColumnName())));
                } else {
                    secondSelect.addSelection(new SelectionSelector(new SelectorIdentifier(si.getColumnName())));
                }
            }

            // ADD WHERE CLAUSES IF ANY
            if(whereInc){
                ArrayList<Relation> firstWhere = new ArrayList<Relation>();
                ArrayList<Relation> secondWhere = new ArrayList<Relation>();
                for(Relation relation: where){
                    String id = relation.getIdentifiers().iterator().next();
                    if(id.contains(".")){
                        String[] tablenameAndColumnname = id.split("\\.");
                        String whereTablename = tablenameAndColumnname[0];
                        String whereColumnname = tablenameAndColumnname[1];

                        // Where clause corresponding to first table
                        if(tableName.equalsIgnoreCase(whereTablename)){
                            firstWhere.add(new RelationCompare(whereColumnname, relation.getOperator(), relation.getTerms().get(0)));
                            // Add column to Select clauses if applied
                            SelectionList sClause = (SelectionList) firstSelect.getSelectionClause();
                            SelectionSelectors sSelectors = (SelectionSelectors) sClause.getSelection();
                            boolean addCol = true;
                            for (SelectionSelector ss: sSelectors.getSelectors()){
                                SelectorIdentifier si = (SelectorIdentifier) ss.getSelector();
                                String ColName = si.getColumnName();
                                if(ColName.equalsIgnoreCase(whereColumnname)){
                                    addCol = false;
                                    break;
                                }
                            }
                            if(addCol){
                                firstSelect.addSelection(new SelectionSelector(new SelectorIdentifier(whereColumnname)));
                            }
                        } else { // Where clause corresponding to second table
                            secondWhere.add(new RelationCompare(whereColumnname, relation.getOperator(), relation.getTerms().get(0)));
                            // Add column to Select clauses if applied
                            SelectionList sClause = (SelectionList) secondSelect.getSelectionClause();
                            SelectionSelectors sSelectors = (SelectionSelectors) sClause.getSelection();
                            boolean addCol = true;
                            for (SelectionSelector ss: sSelectors.getSelectors()){
                                SelectorIdentifier si = (SelectorIdentifier) ss.getSelector();
                                String ColName = si.getColumnName();
                                if(ColName.equalsIgnoreCase(whereColumnname)){
                                    addCol = false;
                                    break;
                                }
                            }
                            if(addCol){
                                secondSelect.addSelection(new SelectionSelector(new SelectorIdentifier(whereColumnname)));
                            }
                        }
                    }
                }
                if(!firstWhere.isEmpty()){
                    firstSelect.setWhere(firstWhere);
                }
                if(!secondWhere.isEmpty()) {
                    secondSelect.setWhere(secondWhere);
                }
            }


            // ADD MAP OF THE JOIN
            joinSelect.setJoin(new InnerJoin("", fields));

            // ADD STEPS
            steps.setNode(new MetaStep(MetaPath.DEEP, joinSelect));
            steps.addChild(new Tree(new MetaStep(MetaPath.DEEP, firstSelect)));
            steps.addChild(new Tree(new MetaStep(MetaPath.DEEP, secondSelect)));

        } else {

            steps.setNode(new MetaStep(MetaPath.CASSANDRA, this));

        }
        return steps;
    }
    
}
