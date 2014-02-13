package com.stratio.sdh.meta.statements;

//import com.stratio.sdh.meta.structures.Option;
import com.stratio.sdh.meta.structures.GroupBy;
import com.stratio.sdh.meta.structures.InnerJoin;
import com.stratio.sdh.meta.structures.MetaRelation;
import com.stratio.sdh.meta.structures.Ordering;
import com.stratio.sdh.meta.structures.Path;
import com.stratio.sdh.meta.structures.SelectionClause;
import com.stratio.sdh.meta.structures.WindowSelect;
import com.stratio.sdh.meta.utils.MetaUtils;
import java.util.List;
//import java.util.List;

public class SelectStatement extends Statement {

    private SelectionClause selectionClause;    
    private String tablename;
    private boolean windowInc;
    private WindowSelect window;
    private boolean joinInc;
    private InnerJoin join;
    private boolean whereInc;
    private List<MetaRelation> where;
    private boolean orderInc;
    private List<Ordering> order;    
    private boolean groupInc;
    private GroupBy group;    
    private boolean limitInc;
    private int limit;
    private boolean disableAnalytics;           

    public SelectStatement(SelectionClause selectionClause, String tablename, 
                           boolean windowInc, WindowSelect window, 
                           boolean joinInc, InnerJoin join, 
                           boolean whereInc, List<MetaRelation> where, 
                           boolean orderInc, List<Ordering> order, 
                           boolean groupInc, GroupBy group, 
                           boolean limitInc, int limit, 
                           boolean disableAnalytics) {
        this.selectionClause = selectionClause;
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

    public List<Ordering> getOrder() {
        return order;
    }

    public void setOrder(List<Ordering> order) {
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
        sb.append(";");
        return sb.toString();
    }
    
}
