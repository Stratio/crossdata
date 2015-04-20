package com.stratio.crossdata.common.statements.structures;

import java.util.Iterator;
import java.util.List;

import com.stratio.crossdata.common.data.TableName;
import com.stratio.crossdata.common.utils.StringUtils;

public class ListSelector extends Selector {

    private static final long serialVersionUID = 6144378661087640561L;

    private final List<Selector> selectorsList;

    /**
     * Class constructor.
     *
     * @param tableName The associated {@link com.stratio.crossdata.common.data.TableName}.
     */
    public ListSelector(TableName tableName, List<Selector> selectorsList) {
        super(tableName);
        this.selectorsList = selectorsList;
    }

    public List<Selector> getSelectorsList() {
        return selectorsList;
    }

    /**
     * Get the selector type.
     *
     * @return A {@link SelectorType}.
     */
    @Override
    public SelectorType getType() {
        return SelectorType.LIST;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("(");
        Iterator<Selector> iter = selectorsList.iterator();
        while(iter.hasNext()){
            Selector s = iter.next();
            sb.append(s);
            if(iter.hasNext()){
                sb.append(", ");
            }
        }
        sb.append(")");
        return sb.toString();
    }

    @Override
    public String toSQLString(boolean withAlias) {
        StringBuilder sb = new StringBuilder("(");
        sb.append(StringUtils.sqlStringList(selectorsList, ", ", withAlias));
        sb.append(")");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ListSelector that = (ListSelector) o;

        if (selectorsList != null ? !selectorsList.equals(that.selectorsList) : that.selectorsList != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return selectorsList != null ? selectorsList.hashCode() : 0;
    }
}
