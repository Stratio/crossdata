package com.stratio.meta.core.structures;

import java.util.ArrayList;
import java.util.List;

public class CollectionLiteral extends ValueCell {
    
    private List<Term> literals;

    public CollectionLiteral(){
        literals = new ArrayList<>();
        this.type = TYPE_COLLECTION_LITERAL;
    }
    
    public CollectionLiteral(List<Term> literals) {
        this();
        this.literals = literals;
    }
    
    public List<Term> getLiterals() {
        return literals;
    }

    public void setLiterals(List<Term> literals) {
        this.literals = literals;
    }        
    
    public void addLiteral(Term term){
        literals.add(term);
    }
    
    public Term getLiteral(int index){
        return literals.get(index);
    }
    
    public void deleteLiteral(Term term){
        literals.remove(term);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        for(Term term: literals){
            sb.append(term.getTerm()).append(", ");
        }        
        return sb.substring(0, sb.length()-2)+"}";
        //return sb.toString();
    }
    
}
