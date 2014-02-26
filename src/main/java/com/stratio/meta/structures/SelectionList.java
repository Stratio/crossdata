package com.stratio.meta.structures;

public class SelectionList extends SelectionClause {

    private boolean distinct;
    private Selection selection;

    public SelectionList(boolean distinct, Selection selection) {
        this.type = TYPE_SELECTION;
        this.distinct = distinct;
        this.selection = selection;
    }   
    
    public boolean isDistinct() {
        return distinct;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }        

    public Selection getSelection() {
        return selection;
    }

    public void setSelection(Selection selection) {
        this.selection = selection;
    }        
    
    public int getTypeSelection(){
        return selection.getType();
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if(distinct){
            sb.append("DISTINCT ");
        }
        sb.append(selection.toString());
        return sb.toString();
    }
    
}
