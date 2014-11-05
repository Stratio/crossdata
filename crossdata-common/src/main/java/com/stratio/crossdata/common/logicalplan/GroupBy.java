package com.stratio.crossdata.common.logicalplan;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.stratio.crossdata.common.metadata.Operations;
import com.stratio.crossdata.common.statements.structures.Selector;

public class GroupBy extends TransformationStep {

    /**
     * Identifiers.
     */
    private List<Selector> ids = new ArrayList<>();

    /**
     * Class constructor.
     *
     * @param operation The operation to be applied.
     * @param ids Identifiers.
     */
    public GroupBy(Operations operation, List<Selector> ids) {
        super(operation);
        this.ids = ids;
    }

    /**
     * Get Identifiers.
     * @return Identifiers.
     */
    public List<Selector> getIds() {
        return ids;
    }

    /**
     * Set identifiers.
     * @param ids Identifiers to be assigned.
     */
    public void setIds(List<Selector> ids) {
        this.ids = ids;
    }

    @Override public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("GROUP BY ");
        Iterator<Selector> iter = ids.iterator();
        while(iter.hasNext()){
            Selector selector = iter.next();
            sb.append(selector);
            if(iter.hasNext()){
                sb.append(", ");
            }
        }
        return sb.toString();
    }
}
