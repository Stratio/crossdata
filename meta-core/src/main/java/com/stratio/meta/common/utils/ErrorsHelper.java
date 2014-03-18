package com.stratio.meta.common.utils;

import com.stratio.meta.common.statements.MetaStatement;

import java.util.ArrayList;
import java.util.List;

public class ErrorsHelper {
    
    private List<AntlrError> antlrErrors = new ArrayList<>();

    public List<AntlrError> getAntlrErrors(){
        return antlrErrors;
    }

    public void setAntlrErrors(List<AntlrError> antlrErros){
        this.antlrErrors = antlrErros;
    }        
    
    public void clearErrors(){
        antlrErrors.clear();
        antlrErrors = new ArrayList<>();
    }
    
    public void addError(AntlrError antlrError){
        antlrErrors.add(antlrError);
    }
    
    public AntlrError getError(int n){
        return antlrErrors.get(n);
    }
    
    public void removeError(int n){
        antlrErrors.remove(n);
    }   
    
    public boolean isEmpty(){
        return antlrErrors.isEmpty();
    }   
 
    public int getNumberOfErrors(){
        return antlrErrors.size();
    }
    
    @Override
    public String toString(){
        return toString(null, null); 
    }    
    
    public String toString(String query, MetaStatement stmt){        
        StringBuilder sb = new StringBuilder("\033[31mParser exception: \033[0m");
        for(AntlrError ae: antlrErrors){
            sb.append(System.getProperty("line.separator"));
            sb.append(ae.toString()).append(System.getProperty("line.separator"));
            sb.append(ae.toStringWithTokenTranslation()).append(System.getProperty("line.separator"));
            sb.append("\t").append(MetaUtils.getQueryWithSign(query, ae));
            if(!query.equalsIgnoreCase("")){
                sb.append(System.getProperty("line.separator")).append("\t");
                sb.append(MetaUtils.getSuggestion(query, ae));
            }
            break;
        }
        return sb.toString(); 
    }
    
}
