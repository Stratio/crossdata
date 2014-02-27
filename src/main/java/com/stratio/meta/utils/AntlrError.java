package com.stratio.meta.utils;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

public class AntlrError {
    
    /**
     * Class logger.
     */
    private static final Logger _logger = Logger.getLogger(AntlrError.class.getName());
    
    private String header;
    private String message;   

    private void initLog(){
        ConsoleAppender console = new ConsoleAppender();
        //String PATTERN = "%d [%p|%c|%C{1}] %m%n";
        //String PATTERN = "%d [%p|%c] %m%n";
        String PATTERN = "%d{dd-MM-yyyy HH:mm:ss.SSS} [%p|%c{1}] %m%n";
        console.setLayout(new PatternLayout(PATTERN)); 
        console.setThreshold(Level.INFO);
        console.activateOptions();
        Logger.getRootLogger().addAppender(console);
    }
    
    public AntlrError(String header, String message) {
        initLog();
        this.header = header;
        this.message = message;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder("\tError recognized: ");        
        sb.append(header).append(": ").append(message);
        return sb.toString();        
    }
    
    public String toStringWithTokenTranslation(){
        StringBuilder sb = new StringBuilder("\tError recognized: ");        
        sb.append(header).append(": ");
        sb.append(MetaUtils.translateToken(message, _logger));
        return sb.toString();        
    }
    
}
