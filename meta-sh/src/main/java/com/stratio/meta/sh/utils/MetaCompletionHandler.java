package com.stratio.meta.sh.utils;

import java.io.IOException;
import java.util.List;
import jline.console.ConsoleReader;
import jline.console.CursorBuffer;
import jline.console.completer.CandidateListCompletionHandler;

public class MetaCompletionHandler extends CandidateListCompletionHandler {

    public MetaCompletionHandler() {
        super();
    }
    
    @Override
    public boolean complete(final ConsoleReader reader, final List<CharSequence> candidates, final int pos) throws IOException{
        //System.out.println();
        CursorBuffer buf = reader.getCursorBuffer();
        //System.out.println("buf="+buf.toString());
        
        // if there is only one completion, then fill in the buffer
        if (candidates.size() == 1) {
            CharSequence value = candidates.get(0);

            // fail if the only candidate is the same as the current buffer
            if (value.equals(buf.toString())) {
                return false;
            }

            String currentBuf = buf.toString();
            currentBuf = currentBuf.trim();
            if(currentBuf.contains(" ")){
                currentBuf = currentBuf.replaceAll(" \\S*$", " "+value.toString());
            } else {
                currentBuf = value.toString();
            }            
            
            setBuffer(reader, currentBuf, pos);

            return true;
        }
        else if (candidates.size() > 1) {
            //String value = getUnambiguousCompletions(candidates);
            setBuffer(reader, buf.toString(), pos);
        }

        printCandidates(reader, candidates);

        // redraw the current console buffer
        reader.drawLine();

        return true;
    }
    
    /**
     * Returns a root that matches all the {@link String} elements of the specified {@link List},
     * or null if there are no commonalities. For example, if the list contains
     * <i>foobar</i>, <i>foobaz</i>, <i>foobuz</i>, the method will return <i>foob</i>.
     */
    private String getUnambiguousCompletions(final List<CharSequence> candidates) {
        if (candidates == null || candidates.isEmpty()) {
            return null;
        }

        // convert to an array for speed
        String[] strings = candidates.toArray(new String[candidates.size()]);

        String first = strings[0];
        StringBuilder candidate = new StringBuilder();

        for (int i = 0; i < first.length(); i++) {
            if (startsWith(first.substring(0, i + 1), strings)) {
                candidate.append(first.charAt(i));
            }
            else {
                break;
            }
        }

        return candidate.toString();
    }
    
    /**
     * @return true is all the elements of <i>candidates</i> start with <i>starts</i>
     */
    private boolean startsWith(final String starts, final String[] candidates) {
        for (String candidate : candidates) {
            if (!candidate.startsWith(starts)) {
                return false;
            }
        }

        return true;
    }
    
}
