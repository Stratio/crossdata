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

package com.stratio.meta.sh.help;

import org.testng.annotations.Test;

import static org.testng.Assert.assertNotNull;

public class HelpManagerTest {

    private HelpManager manager = new HelpManager();

    @Test
    public void testLoadHelpContent() throws Exception {
        HelpContent content = manager.loadHelpContent();
        assertNotNull(content, "Cannot load help");
        assertNotNull(content.getContent(), "Null help content returned.");

        HelpEntry entry = content.getContent().get(0);
        assertNotNull(entry.getEntry(), "Cannot read entry");
        assertNotNull(entry.getHelp(), "Cannot read help");

        assertNotNull(content.getHelp(), "Null help map returned.");
        for(HelpType type : HelpType.values()){
            assertNotNull(content.getHelp().containsKey(type), "Cannot find help associated with " + type + " in help map");
            assertNotNull(content.searchHelp(type), "Cannot find help associated with " + type);
        }
    }
}
