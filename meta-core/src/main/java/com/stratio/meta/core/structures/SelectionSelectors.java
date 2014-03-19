package com.stratio.meta.core.structures;

import com.stratio.meta.sh.utils.ShUtils;

import java.util.List;

public class SelectionSelectors extends Selection {

    private List<SelectionSelector> selectors;

    public SelectionSelectors(List<SelectionSelector> selectors) {
        this.type = TYPE_SELECTOR;
        this.selectors = selectors;
    }   
    
    public List<SelectionSelector> getSelectors() {
        return selectors;
    }

    public void setSelectors(List<SelectionSelector> selectors) {
        this.selectors = selectors;
    }   
    
    public void addSelectionSelector(SelectionSelector ss){
        selectors.add(ss);
    }
    
    public SelectionSelector getSelectionSelector(int index){
        return selectors.get(index);
    }
    
    public void removeSelectionSelector(int index){
        selectors.remove(index);
    }
    
    public int numberOfSelectors(){
        return selectors.size();
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(ShUtils.StringList(selectors, ", "));
        return sb.toString();
    }
    
}
