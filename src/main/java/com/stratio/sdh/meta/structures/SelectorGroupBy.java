package com.stratio.sdh.meta.structures;

import com.stratio.sdh.meta.utils.MetaUtils;
import java.util.List;

public class SelectorGroupBy extends SelectorMeta {
    
    private GroupByFunction gbFunction;
    private List<SelectorMeta> params;

    public SelectorGroupBy(GroupByFunction gbFunction, List<SelectorMeta> params) {
        this.gbFunction = gbFunction;
        this.params = params;
    }   
        
    public GroupByFunction getGbFunction() {
        return gbFunction;
    }

    public void setGbFunction(GroupByFunction gbFunction) {
        this.gbFunction = gbFunction;
    }

    public List<SelectorMeta> getParams() {
        return params;
    }

    public void setParams(List<SelectorMeta> params) {
        this.params = params;
    }        
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(gbFunction.name());
        sb.append("(").append(MetaUtils.StringList(params, ", ")).append(")");
        return sb.toString();
    }
    
}
