package com.stratio.sdh.meta;

import com.stratio.sdh.meta.generated.MetaLexer;
import com.stratio.sdh.meta.generated.MetaParser;
import com.stratio.sdh.meta.statements.Statement;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;

public class MetaTest{

    public static void main(String[] args) {
        try {
            System.out.print("Insert query: ");
            String queryTxt = new Scanner(System.in).nextLine();
            
            
           while(!queryTxt.equalsIgnoreCase("exit")){                                                                
                ANTLRStringStream input = new ANTLRStringStream(queryTxt);
                System.out.println("ANTLRInputStream created");
                MetaLexer lexer = new MetaLexer(input);
                System.out.println("MetaLexer created");
                CommonTokenStream tokens = new CommonTokenStream(lexer);
                System.out.println("CommonTokenStream created");
               MetaParser parser = new MetaParser(tokens);   
                System.out.println("MetaParser created");
                Statement statement = parser.query();
                System.out.println(statement.toString());                
                System.out.print("Insert query: ");
                queryTxt = new Scanner(System.in).nextLine();
            }
        } catch (RecognitionException ex) {
            Logger.getLogger(MetaTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
