package com.stratio.meta.client.help;

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
		System.out.println("Search help for: " + type);
		return _help.get(type);
	}
}
