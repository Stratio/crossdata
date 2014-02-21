package com.stratio.sdh.meta.statements;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.querybuilder.*;
import com.datastax.driver.core.querybuilder.Select.Where;
import com.stratio.sdh.meta.structures.GroupBy;
import com.stratio.sdh.meta.structures.InnerJoin;
import com.stratio.sdh.meta.structures.MetaOrdering;
import com.stratio.sdh.meta.structures.MetaRelation;
import com.stratio.sdh.meta.structures.OrderDirection;
import com.stratio.sdh.meta.structures.Path;
import com.stratio.sdh.meta.structures.RelationCompare;
import com.stratio.sdh.meta.structures.RelationIn;
import com.stratio.sdh.meta.structures.RelationToken;
import com.stratio.sdh.meta.structures.Selection;
import com.stratio.sdh.meta.structures.SelectionClause;
import com.stratio.sdh.meta.structures.SelectionList;
import com.stratio.sdh.meta.structures.SelectionSelector;
import com.stratio.sdh.meta.structures.SelectionSelectors;
import com.stratio.sdh.meta.structures.SelectorIdentifier;
import com.stratio.sdh.meta.structures.SelectorMeta;
import com.stratio.sdh.meta.structures.Term;
import com.stratio.sdh.meta.structures.WindowSelect;
import com.stratio.sdh.meta.utils.MetaUtils;
import java.util.List;

public class SelectStatement extends MetaStatement {

    private SelectionClause selectionClause;
    private boolean keyspaceInc;
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
    
    public String getTablename() {
        return tablename;
    }

    public void setTablename(String tablename) {
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
    
    @Override
    public Path estimatePath() {
        return Path.CASSANDRA;
    }    

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("SELECT ");
        sb.append(selectionClause.toString()).append(" FROM ").append(tablename);
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
    public boolean validate() {
        return true;
    }

    @Override
    public String getSuggestion() {
        return this.getClass().toString().toUpperCase()+" EXAMPLE";
    }

    @Override
    public String translateToCQL() {
        return this.toString();
    }
    
    @Override
    public String parseResult(ResultSet resultSet) {
        StringBuilder sb = new StringBuilder();  
        /*Properties props = System.getProperties();
        for(String propKey: props.stringPropertyNames()){
            System.out.println(propKey+": "+props.getProperty(propKey));
        }*/
        for(Row row: resultSet){
            sb.append("\t").append(row.toString()).append(System.getProperty("line.separator"));
        }
        return sb.toString();
    }
    
    @Override
    public Statement getDriverStatement() {
        //Statement sel = QueryBuilder.select().from("tks","testselectin").where(in("key",list));
        
        //Select sel = QueryBuilder.select().from(tablename);     
        
        Select sel = null;
        
        SelectionClause selClause = this.selectionClause;
        
        if(this.selectionClause.getType() == SelectionClause.TYPE_COUNT){
            return null;
        }
        
        SelectionList selList = (SelectionList) selClause;
        
        Select.Builder builder = null;
        
        if(selList.getSelection().getType() == Selection.TYPE_ASTERISK){
            builder = QueryBuilder.select();
        } else {
            SelectionSelectors selSelectors = (SelectionSelectors) selList.getSelection();
            String[] columns = {};
            int nCol = 0;
            for(SelectionSelector selSelector: selSelectors.getSelectors()){
                SelectorMeta selectorMeta = selSelector.getSelector();
                if(selectorMeta.getType() == SelectorMeta.TYPE_IDENT){
                    SelectorIdentifier selIdent = (SelectorIdentifier) selectorMeta;
                    columns[0] = selIdent.getIdentifier();
                    nCol++;
                }
            }
            builder = QueryBuilder.select(columns);
        }              
        
        if(this.keyspaceInc){
            sel = builder.from(this.keyspace, this.tablename);
        } else {
            sel = builder.from(this.tablename);
        }
        
        if(this.limitInc){
            sel.limit(this.limit);
        }
        
        if(this.orderInc){
            Ordering[] orderings = {};
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
        
        Where whereStmt = null;
        if(this.whereInc){
            for(MetaRelation metaRelation: this.where){
                Clause clause = null;
                String name = "";
                Object value = null;
                switch(metaRelation.getType()){
                    case MetaRelation.TYPE_COMPARE:
                        RelationCompare relCompare = (RelationCompare) metaRelation;
                        name = relCompare.getIdentifiers().get(0);
                        value = relCompare.getTerms().get(0).getTerm();
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
                        }                                  
                        break;
                    case MetaRelation.TYPE_IN:
                        RelationIn relIn = (RelationIn) metaRelation;
                        List<Term> terms = relIn.getTerms();
                        Object[] values = {};
                        int nTerm = 0;
                        for(Term term: terms){
                            values[nTerm] = term.getTerm();
                            nTerm++;
                        }
                        clause = QueryBuilder.in(relIn.getIdentifiers().get(0), values);
                        break;
                    case MetaRelation.TYPE_TOKEN:
                        RelationToken relToken = (RelationToken) metaRelation;
                        List<String> names = relToken.getIdentifiers();
                        value = relToken.getTerms().get(0).getTerm();
                        switch(relToken.getOperator()){
                            case "=":
                                clause = QueryBuilder.eq(QueryBuilder.token((String[]) names.toArray()), value);
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
                        }
                        break;
                }
                if(whereStmt == null){
                    whereStmt = sel.where(clause);
                } else {
                    whereStmt = whereStmt.and(clause);
                }
            }            
            /*
            Object[] complexUriArr;
            Select.Where tmp = QueryBuilder.select().all().from("myTable").where(QueryBuilder.eq("col_1", QueryBuilder.bindMarker())).and(QueryBuilder.in("col_2", complexUriArr));            
            sql = sel.where(QueryBuilder.lt(query, sel)).and(QueryBuilder.eq(query, sel));
            sel.where(Clause clause).where(Clause clause);
            */
        } else {
            whereStmt = sel.where();
        }
        
        return whereStmt;
    }
    
}
