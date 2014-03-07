package com.stratio.meta.statements;

import com.datastax.driver.core.ColumnDefinitions;
import com.datastax.driver.core.ColumnDefinitions.Definition;
import com.datastax.driver.core.DataType;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.querybuilder.*;
import com.datastax.driver.core.querybuilder.Select.Where;
import com.stratio.meta.structures.GroupBy;
import com.stratio.meta.structures.InnerJoin;
import com.stratio.meta.structures.MetaOrdering;
import com.stratio.meta.structures.MetaRelation;
import com.stratio.meta.structures.OrderDirection;
import com.stratio.meta.structures.RelationCompare;
import com.stratio.meta.structures.RelationIn;
import com.stratio.meta.structures.RelationToken;
import com.stratio.meta.structures.Selection;
import com.stratio.meta.structures.SelectionClause;
import com.stratio.meta.structures.SelectionList;
import com.stratio.meta.structures.SelectionSelector;
import com.stratio.meta.structures.SelectionSelectors;
import com.stratio.meta.structures.SelectorFunction;
import com.stratio.meta.structures.SelectorIdentifier;
import com.stratio.meta.structures.SelectorMeta;
import com.stratio.meta.structures.Term;
import com.stratio.meta.structures.WindowSelect;
import com.stratio.meta.utils.DeepResult;
import com.stratio.meta.utils.MetaPath;
import com.stratio.meta.utils.MetaStep;
import com.stratio.meta.utils.MetaUtils;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

public class SelectStatement extends MetaStatement {

    private SelectionClause selectionClause;
    private boolean keyspaceInc = false;
    private String keyspace;
    private String tablename;
    private boolean windowInc;
    private WindowSelect window;
    private boolean joinInc;
    private InnerJoin join;
    private boolean whereInc;
    private List<MetaRelation> where;
    private boolean orderInc;
    private List<MetaOrdering> order;    
    private boolean groupInc;
    private GroupBy group;    
    private boolean limitInc;
    private int limit;
    private boolean disableAnalytics;
    private boolean needsAllowFiltering = false;

    public SelectStatement(SelectionClause selectionClause, String tablename, 
                           boolean windowInc, WindowSelect window, 
                           boolean joinInc, InnerJoin join, 
                           boolean whereInc, List<MetaRelation> where, 
                           boolean orderInc, List<MetaOrdering> order, 
                           boolean groupInc, GroupBy group, 
                           boolean limitInc, int limit, 
                           boolean disableAnalytics) {
        this.selectionClause = selectionClause;        
        if(tablename.contains(".")){
            String[] ksAndTablename = tablename.split("\\.");
            keyspace = ksAndTablename[0];
            tablename = ksAndTablename[1];
            keyspaceInc = true;
        }
        this.tablename = tablename;        
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
    
    public SelectStatement(SelectionClause selectionClause, String tablename) {        
        this(selectionClause, tablename, false, null, false, null, false, null, false, null, false, null, false, 0, false);
    }                

    public void setSelectionClause(SelectionClause selectionClause) {
        this.selectionClause = selectionClause;
    }        

    public boolean isKeyspaceInc() {
        return keyspaceInc;
    }

    public void setKeyspaceInc(boolean keyspaceInc) {
        this.keyspaceInc = keyspaceInc;
    }

    public String getKeyspace() {
        return keyspace;
    }

    public void setKeyspace(String keyspace) {
        this.keyspace = keyspace;
    }        
    
    public String getTablename() {
        return tablename;
    }

    public void setTablename(String tablename) {
        if(tablename.contains(".")){
            String[] ksAndTablename = tablename.split("\\.");
            keyspace = ksAndTablename[0];
            tablename = ksAndTablename[1];
            keyspaceInc = true;
        }
        this.tablename = tablename;
    }          

    public SelectionClause getSelectionClause() {
        return selectionClause;
    }

    public boolean isWindowInc() {
        return windowInc;
    }

    public void setWindowInc(boolean windowInc) {
        this.windowInc = windowInc;
    }

    public WindowSelect getWindow() {
        return window;
    }

    public void setWindow(WindowSelect window) {
        this.windowInc = true;
        this.window = window;
    }        

    public boolean isJoinInc() {
        return joinInc;
    }

    public void setJoinInc(boolean joinInc) {
        this.joinInc = joinInc;
    }

    public InnerJoin getJoin() {
        return join;
    }

    public void setJoin(InnerJoin join) {
        this.joinInc = true;
        this.join = join;
    }        

    public boolean isWhereInc() {
        return whereInc;
    }

    public void setWhereInc(boolean whereInc) {
        this.whereInc = whereInc;
    }

    public List<MetaRelation> getWhere() {
        return where;
    }

    public void setWhere(List<MetaRelation> where) {
        this.whereInc = true;
        this.where = where;
    }        

    public boolean isOrderInc() {
        return orderInc;
    }

    public void setOrderInc(boolean orderInc) {
        this.orderInc = orderInc;
    }

    public List<MetaOrdering> getOrder() {
        return order;
    }

    public void setOrder(List<MetaOrdering> order) {
        this.orderInc = true;
        this.order = order;
    }        

    public boolean isGroupInc() {
        return groupInc;
    }

    public void setGroupInc(boolean groupInc) {
        this.groupInc = true;
        this.groupInc = groupInc;
    }

    public GroupBy getGroup() {
        return group;
    }

    public void setGroup(GroupBy group) {
        this.groupInc = true;
        this.group = group;
    }

    public boolean isLimitInc() {
        return limitInc;
    }

    public void setLimitInc(boolean limitInc) {
        this.limitInc = limitInc;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limitInc = true;
        this.limit = limit;
    }

    public boolean isDisableAnalytics() {
        return disableAnalytics;
    }

    public void setDisableAnalytics(boolean disableAnalytics) {
        this.disableAnalytics = disableAnalytics;
    }                   

    public boolean isNeedsAllowFiltering() {
        return needsAllowFiltering;
    }

    public void setNeedsAllowFiltering(boolean needsAllowFiltering) {
        this.needsAllowFiltering = needsAllowFiltering;
    }        

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("SELECT ");
        sb.append(selectionClause.toString()).append(" FROM ");
        if(keyspaceInc){
            sb.append(keyspace).append(".");
        }
        sb.append(tablename);
        if(windowInc){
            sb.append(" WITH WINDOW ").append(window.toString());
        }
        if(joinInc){
            sb.append(" INNER JOIN ").append(join.toString());
        }
        if(whereInc){
            sb.append(" WHERE ");
            sb.append(MetaUtils.StringList(where, " AND "));
        }
        if(orderInc){
            sb.append(" ORDER BY ").append(MetaUtils.StringList(order, ", "));
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
        //sb.append(";");
        return sb.toString();
    }

    @Override
    public void validate() {
        
    }

    @Override
    public String getSuggestion() {
        return this.getClass().toString().toUpperCase()+" EXAMPLE";
    }

    @Override
    public String translateToCQL() {
        StringBuilder sb = new StringBuilder(this.toString());     
        System.out.println(sb.toString());        
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
        System.out.println(sb.toString());
        return sb.toString();
    }
    
    @Override
    public String parseResult(ResultSet resultSet) {
        ColumnDefinitions colDefs = resultSet.getColumnDefinitions();
        int nCols = colDefs.size();
        List<Row> rows = resultSet.all();
        if(rows.isEmpty()){
            return "Empty"+System.getProperty("line.separator");
        }
        ArrayList<ArrayList<String>> table = new ArrayList<>();
        HashMap<Integer, Integer> lenghts = new HashMap<>(); 
        int nColumn = 0;
        int extraSpace = 2;
        for(Definition def: colDefs){
            lenghts.put(nColumn, def.getName().length()+extraSpace);
            nColumn++;
        }        
        
        if((lenghts.get(0)-extraSpace) < (rows.size()+" rows").length()){
            lenghts.put(0, (rows.size()+" rows").length()+extraSpace);
        }
        
        for(Row row: rows){
            ArrayList<String> currentRow = new ArrayList<>();
            for(int nCol=0; nCol<nCols; nCol++){
                String cell = "null";
                com.datastax.driver.core.DataType cellType = colDefs.getType(nCol);  
                if((cellType == com.datastax.driver.core.DataType.varchar()) || (cellType == com.datastax.driver.core.DataType.text())){
                    cell = row.getString(nCol);
                } else if (cellType == com.datastax.driver.core.DataType.cint()){
                    cell = Integer.toString(row.getInt(nCol));
                } else if (cellType == com.datastax.driver.core.DataType.uuid() || cellType == com.datastax.driver.core.DataType.timeuuid()){
                    UUID uuid = row.getUUID(nCol);
                    if(uuid!=null){
                        cell = uuid.toString();
                    }
                } else if (cellType == com.datastax.driver.core.DataType.bigint()){
                    //BigInteger bi = row.getVarint(nCol);
                    //cell = bi.toString();
                    ByteBuffer bb = row.getBytesUnsafe(nCol);
                    IntBuffer intbb = bb.asIntBuffer();
                    int tmpInt = 0;
                    while(intbb.remaining() > 0){
                        tmpInt = intbb.get();
                        //System.out.println(tmpInt);
                    }
                    //int tmp = bb.asIntBuffer().get(bb.remaining()-1);
                    cell = Integer.toString(tmpInt);
                } else if (cellType == com.datastax.driver.core.DataType.timestamp()){
                    Date date = row.getDate(nCol);
                    if (date != null){
                        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss.SSS");
                        cell = sdf.format(date); 
                    }
                }
                // TODO: add all data types
                currentRow.add(cell);
                if((cell != null) && ((lenghts.get(nCol)-extraSpace) < cell.length())){
                    lenghts.put(nCol, cell.length()+extraSpace);
                }
                /*if(lenghts.containsKey(nCol)){
                    if(lenghts.get(nCol) < cell.length()){
                        lenghts.put(nCol, cell.length());
                    }
                } else {
                    lenghts.put(nCol, cell.length());
                }*/                
            }
            table.add(currentRow);
        }        
        
        // ADD ENDING ROW
        ArrayList<String> currentRow = new ArrayList<>();
        for(int nCol=0; nCol<nCols; nCol++){
            char[] chars = new char[lenghts.get(nCol)];
            Arrays.fill(chars, '-');
            currentRow.add(new String(chars));
        }
        table.add(currentRow);
        table.add(currentRow);
        
        // ADD STARTING ROW
        table.add(0, currentRow);
        
        // ADD SEPARATING ROW
        table.add(1, currentRow);
        
        // ADD HEADER ROW
        currentRow = new ArrayList<>();
        for(int nCol=0; nCol<nCols; nCol++){
            currentRow.add(colDefs.getName(nCol));
        }
        table.add(1, currentRow);        
        
        // ADD INFO ROW
        currentRow = new ArrayList<>();
        if(rows.size() != 1){
            currentRow.add(rows.size()+" rows");        
        } else {
            currentRow.add("1 row");
        }
        for(int nCol=1; nCol<nCols; nCol++){
            currentRow.add(" ");
        }
        table.add(table.size()-1, currentRow);
        
          
        /*for(Definition definition: resultSet.getColumnDefinitions().asList()){
            sb.append("\033[4m").append(definition.getName()).append("\033[0m");
            //sb.append(": ").append(definition.getType().getName().toString()).append(" | ");
        }*/
        //sb.append(System.getProperty("line.separator"));
        /*Properties props = System.getProperties();
        for(String propKey: props.stringPropertyNames()){
            System.out.println(propKey+": "+props.getProperty(propKey));
        }*/
        /*for(Row row: rows){
            sb.append("\t").append(row.toString()).append(System.getProperty("line.separator"));
        }*/
        StringBuilder sb = new StringBuilder(System.getProperty("line.separator"));
        sb.append(System.getProperty("line.separator"));
        int nRow = 0;
        for(ArrayList<String> tableRow: table){
            if((nRow == 0) || (nRow == 2) || (nRow == (table.size()-3)) || (nRow == (table.size()-1))){
                sb.append(" ").append("+");
            } else {
                sb.append(" ").append("|");
            }
            int nCol = 0;
            StringUtils.leftPad(query, nCol);
            for(String cell: tableRow){
                if(cell == null){
                    cell = "null";
                }
                if((colDefs.getType(nCol) != com.datastax.driver.core.DataType.cint()) && (colDefs.getType(nCol) != com.datastax.driver.core.DataType.bigint())){
                    cell = StringUtils.rightPad(cell, lenghts.get(nCol)-1);
                    cell = StringUtils.leftPad(cell, lenghts.get(nCol));                         
                } else {
                    cell = StringUtils.leftPad(cell, lenghts.get(nCol)-1);
                    cell = StringUtils.rightPad(cell, lenghts.get(nCol));
                }    
                if(nRow == 1){
                    sb.append("\033[33;1m").append(cell).append("\033[0m");
                } else {
                    sb.append(cell);                    
                }

                if((nRow == 0) || (nRow == (table.size()-3)) || (nRow == (table.size()-1))){
                    /*
                    if((nCol < (tableRow.size()-1)) && (nCol > 0)){
                        sb.append("-");
                    }  else {
                        sb.append("+");
                    }
                    */
                    if(nCol == (tableRow.size()-1)){
                        sb.append("+");
                    } else {
                        sb.append("-");
                    }
                } else if(nRow == 2) {
                    //if(nCol < (tableRow.size()-1)){
                        sb.append("+");
                    //} else {
                    //    sb.append("|");
                    //}
                } else if(nRow == (table.size()-2)){
                    if(nCol > tableRow.size()-2){
                        sb.append("|");
                    } else {
                        sb.append(" ");
                    } 
                } else {
                    sb.append("|");
                }
                nCol++;
            }
            sb.append(System.getProperty("line.separator"));      
            nRow++;
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
                        List<SelectorMeta> params = selFunction.getParams();
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
            sel = builder.from(this.keyspace, this.tablename);
        } else {
            sel = builder.from(this.tablename);
        }
        
        if(this.limitInc){
            sel.limit(this.limit);
        }                
        
        if(this.orderInc){
            Ordering[] orderings = new Ordering[order.size()];
            int nOrdering = 0;
            for(MetaOrdering metaOrdering: this.order){
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
            for(MetaRelation metaRelation: this.where){
                Clause clause = null;
                String name;
                Object value;                
                switch(metaRelation.getType()){
                    case MetaRelation.TYPE_COMPARE:
                        RelationCompare relCompare = (RelationCompare) metaRelation;
                        name = relCompare.getIdentifiers().get(0);
                        value = relCompare.getTerms().get(0).getTerm();
                        if(value.toString().matches("[0123456789\\.]+")){
                            value = Integer.parseInt(value.toString());
                        } else if (value.toString().contains("-")) {
                            value = UUID.fromString(value.toString());
                        }
                        switch(relCompare.getOperator()){
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
                            default:
                                clause = null;
                                throw new UnsupportedOperationException("'"+relCompare.getOperator()+"' operator not supported by C*");
                        }                                  
                        break;
                    case MetaRelation.TYPE_IN:
                        RelationIn relIn = (RelationIn) metaRelation;
                        List<Term> terms = relIn.getTerms();                        
                        Object[] values = new Object[relIn.numberOfTerms()];                        
                        int nTerm = 0;
                        for(Term term: terms){
                            values[nTerm] = term.getTerm();
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
                    case MetaRelation.TYPE_TOKEN:
                        RelationToken relToken = (RelationToken) metaRelation;
                        List<String> names = relToken.getIdentifiers();
                        if(!relToken.isRighSideTokenType()){
                            value = relToken.getTerms().get(0).getTerm();
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
                            /*
                            List<Term> termsOfToken = relToken.getTerms();
                            List<String> termsOfTokenStr = new ArrayList<>();
                            for(Term term: termsOfToken){
                                termsOfTokenStr.add(term.toString());
                            }
                            String[] namesStr = MetaUtils.fromStringListToArray(names);
                            String[] termsStr = MetaUtils.fromStringListToArray(termsOfTokenStr);
                            switch(relToken.getOperator()){
                                case "=":
                                    clause = QueryBuilder.eq(QueryBuilder.token(namesStr), QueryBuilder.token(termsStr));
                                    break;
                                case ">":
                                    clause = QueryBuilder.gt(QueryBuilder.token(namesStr), QueryBuilder.token(termsStr));
                                    break;
                                case ">=":
                                    clause = QueryBuilder.gte(QueryBuilder.token(namesStr), QueryBuilder.token(termsStr));
                                    break;
                                case "<":
                                    clause = QueryBuilder.lt(QueryBuilder.token(namesStr), QueryBuilder.token(termsStr));
                                    break;
                                case "<=":
                                    clause = QueryBuilder.lte(QueryBuilder.token(namesStr), QueryBuilder.token(termsStr));
                                    break;
                                default:
                                    clause = null;
                                    throw new UnsupportedOperationException("'"+relToken.getOperator()+"' operator not supported by C*");
                            }*/  
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
    public DeepResult executeDeep() {
        return new DeepResult("Success", new ArrayList<>(Arrays.asList("Not supported yet")));
    }

    @Override
    public List<MetaStep> getPlan() {
        ArrayList<MetaStep> steps = new ArrayList<>();
        if(joinInc){             
            String tableFrom = tablename;
            String tableJoin = this.join.getTablename();
            SelectionList selectionList = (SelectionList) this.selectionClause;
            SelectionSelectors selection = (SelectionSelectors) selectionList.getSelection();
            StringBuilder sb1 = new StringBuilder("SELECT ");
            StringBuilder sb2 = new StringBuilder("SELECT ");
            for (SelectionSelector ss: selection.getSelectors()){
                SelectorIdentifier si = (SelectorIdentifier) ss.getSelector();
                System.out.println(si.getIdentifier());
                if(si.getTablename().equalsIgnoreCase(tableFrom)){
                    sb1.append(si.getColumnName()).append(", ");
                } else {
                    sb2.append(si.getColumnName()).append(", ");
                }
            }
            Map<String, String> fields = this.join.getFields();
            for(String key: fields.keySet()){
                String value = fields.get(key);
                if(key.split("\\.")[0].trim().equalsIgnoreCase(tableFrom.trim())){
                    sb1.append(key.split("\\.")[1]).append(", ");
                    sb2.append(value.split("\\.")[1]).append(", ");
                } else {
                    sb1.append(value.split("\\.")[1]).append(", ");
                    sb2.append(key.split("\\.")[1]).append(", ");
                }                
            }
            sb1.deleteCharAt(sb1.length()-2);
            sb2.deleteCharAt(sb2.length()-2);
            sb1.append("FROM ").append(tableFrom).append(";");
            sb2.append("FROM ").append(tableJoin).append(";");
            steps.add(new MetaStep(MetaPath.CASSANDRA, "ResultSet rFrom = CassandraClient.executeQuery(\""+sb1.toString()+"\")"));            
            steps.add(new MetaStep(MetaPath.CASSANDRA, "ResultSet rJoin = CassandraClient.executeQuery(\""+sb2.toString()+"\")"));           
            // JOIN IN DEEP
            steps.add(new MetaStep(MetaPath.DEEP, "Deep.select.join(rFrom, rJoin, metaStatement)"));
        }
        return steps;
    }
    
}
