package com.stratio.meta.core.structures;

import com.stratio.meta.common.utils.MetaUtils;

import java.util.List;

public class SelectorFunction extends SelectorMeta {

    private String name;
    private List<SelectorMeta> params;

    public SelectorFunction(String name, List<SelectorMeta> params) {
        this.type = TYPE_FUNCTION;
        this.name = name;
        this.params = params;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SelectorMeta> getParams() {
        return params;
    }

    public void setParams(List<SelectorMeta> params) {
        this.params = params;
    }        
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(name);
        sb.append("(").append(MetaUtils.StringList(params, ", ")).append(")");
        return sb.toString();
    }
    
}
