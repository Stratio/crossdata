/*
 * Stratio Meta
 *
 * Copyright (c) 2014, Stratio, All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 */

package com.stratio.meta.sh.utils;

import com.stratio.meta.common.utils.MetaUtils;
import java.util.Collection;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import jline.console.completer.Completer;
import static jline.internal.Preconditions.checkNotNull;

public class MetaCompletor implements Completer {

    private final SortedSet<String> strings = new TreeSet<>();
    
    public MetaCompletor() {
    }        

    public MetaCompletor(final Collection<String> strs) {
        checkNotNull(strs);
        strings.addAll(strs);
    }        
    
    @Override
    public int complete(final String buffer, final int cursor, final List<CharSequence> candidates) {
        //System.out.println("MetaCompletor.complete");
        checkNotNull(candidates);
        if ((buffer == null) || (buffer.length()<1)) {            
            candidates.addAll(MetaUtils.initials);   
        } else {                       
            // Last char is a space ==> NO completion implemented yet
            if(buffer.charAt(buffer.length()-1) == ' '){ 
                return -1;
            }  
            strings.clear(); 
            String[] partialTokens = buffer.split(" ");
            String partialQuery = buffer.trim().toUpperCase();
            if(partialTokens.length == 1) { // First token
                strings.addAll(MetaUtils.initials);
            } else { // NO first token and new token initiated
                strings.addAll(MetaUtils.noInitials);
                partialQuery = partialTokens[partialTokens.length-1].trim().toUpperCase();
            }            

            for (String match: strings.tailSet(partialQuery)) {
                if (!match.startsWith(partialQuery)) {
                    break;
                }
                candidates.add(match);
            }

            /*
            MetaStatement result = null;
            ANTLRStringStream input = new ANTLRStringStream(buffer);
            MetaLexer lexer = new MetaLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            MetaParser parser = new MetaParser(tokens);
            try {
                result = parser.query();
            } catch (RecognitionException ex) {
                //nothing to do
            }            
            for(Parser delegate: parser.getDelegates()){
                candidates.add(delegate.toString());
            }
            for(String rule: parser.getRuleInvocationStack()){
                candidates.add(rule);
            }
            //candidates.addAll(Arrays.asList(parser.getTokenNames()));
            try {
                result = parser.query();
            } catch (RecognitionException ex) {
                Logger.getLogger(MetaCompletor.class.getName()).log(Level.SEVERE, null, ex);
            }
            */            
        }
        if (candidates.size() == 1) {
            candidates.set(0, candidates.get(0) + " ");
        }
        return candidates.isEmpty()? -1 : 0;
    }
    
}
