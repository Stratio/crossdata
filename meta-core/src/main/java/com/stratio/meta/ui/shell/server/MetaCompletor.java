package com.stratio.meta.ui.shell.server;

import com.stratio.meta.utils.MetaUtils;
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
        checkNotNull(candidates);
        if ((buffer == null) || (buffer.length()<1)) {            
            candidates.addAll(MetaUtils.initials);   
        }else {
            /*for (String match : strings.tailSet(buffer)) {
                if (!match.startsWith(buffer)) {
                    break;
                }
                candidates.add(match);
            }*/
        }
        if (candidates.size() == 1) {
            candidates.set(0, candidates.get(0) + " ");
        }
        return candidates.isEmpty()? -1 : 0;
    }
    
}
