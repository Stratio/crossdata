package com.stratio.meta.common.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CreateTokensFile {
    
    public static void main(String[] args) {                
        try {            
            String metaGrammarPath = "src/main/java/com/stratio/meta/grammar/Meta.g";
            String metaTokens = "src/main/resources/com/stratio/meta/server/parser/tokens.txt";
            BufferedReader br = new BufferedReader(new FileReader(new File(metaGrammarPath)));
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File(metaTokens)));
            String line = br.readLine();
            while (line != null){                
                if(line.startsWith("T_")){
                    String token = line.substring(2, line.indexOf(":")+2);
                    String replacement = line.substring(line.indexOf(":")+1);
                    replacement = replacement.replace(";", "");
                    replacement = replacement.replace("'", "");
                    replacement = replacement.replace(" ", "");
                    replacement = "[#"+replacement+"#]";
                    bw.write(token+replacement);
                    bw.newLine();
                }                
                line = br.readLine();
            }
            br.close();
            bw.flush();
            bw.close();            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CreateTokensFile.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CreateTokensFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
