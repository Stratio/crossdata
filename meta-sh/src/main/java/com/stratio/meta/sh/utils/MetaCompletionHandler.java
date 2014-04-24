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

import jline.console.ConsoleReader;
import jline.console.CursorBuffer;
import jline.console.completer.CandidateListCompletionHandler;

import java.io.IOException;
import java.util.List;

public class MetaCompletionHandler extends CandidateListCompletionHandler {

    public MetaCompletionHandler() {
        super();
    }
    
    @Override
    public boolean complete(final ConsoleReader reader, final List<CharSequence> candidates, final int pos) throws IOException{
        CursorBuffer buf = reader.getCursorBuffer();

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
        } else if (candidates.size() > 1) {
            setBuffer(reader, buf.toString(), pos);
        }

        printCandidates(reader, candidates);

        // redraw the current console buffer
        reader.drawLine();

        return true;
    }
    
}
