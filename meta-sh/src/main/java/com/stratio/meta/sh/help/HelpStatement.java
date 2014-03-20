package com.stratio.meta.sh.help;

public class HelpStatement {

	private final HelpType _type;
	
	public HelpStatement(HelpType type){
		_type = type;
	}
	
	public HelpType getType(){
		return _type;
	}
	
	@Override
	public String toString() {
		return "HELP " + _type;
	}
}
