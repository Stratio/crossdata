/*
 * Licensed to STRATIO (C) under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  The STRATIO (C) licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.stratio.crossdata.sh.utils;

import java.io.IOException;
import java.util.List;

import jline.console.ConsoleReader;
import jline.console.CursorBuffer;
import jline.console.completer.CandidateListCompletionHandler;

/**
 * Completion helper for the Crossdata console.
 */
public class XDshCompletionHandler extends CandidateListCompletionHandler {

    /**
     * Class constructor that calls the parent constructor.
     */
    public XDshCompletionHandler() {
        super();
    }

    @Override
    public boolean complete(final ConsoleReader reader, final List<CharSequence> candidates, final int pos)
            throws IOException {
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
            if (currentBuf.contains(" ")) {
                currentBuf = currentBuf.replaceAll(" \\S*$", " " + value.toString());
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
