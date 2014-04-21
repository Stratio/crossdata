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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class that contains the list of {@link HelpEntry} with the help contents.
 */
public class HelpContent {

	/**
	 * The list of {@link HelpEntry}.
	 */
	public List<HelpEntry> content;
	
	/**
	 * A mapped view of the help entries.
	 */
	Map<HelpType, String> _help;
	
	/**
	 * Load the mapped view of the help contents.
	 */
	public void loadMap(){
		_help = new HashMap<HelpType, String>();
		for(HelpEntry e : content){
			_help.put(HelpType.valueOf(e.entry), e.help);
		}
	}
	
	/**
	 * Retrieve the help associated with {@link HelpType}.
	 * @param type The requested {@link HelpType}
	 * @return The help string or null if the help is not available.
	 */
	public String searchHelp(HelpType type){
		return _help.get(type);
	}
}
