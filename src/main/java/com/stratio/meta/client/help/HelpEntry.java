package com.stratio.meta.client.help;

public class HelpEntry {
	public String entry = null;
	public String help = null;
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(entry);
		sb.append("\n");
		sb.append(help);
		return sb.toString();
	}
}
