package com.stratio.meta.sh.help;

import java.io.IOException;
import java.io.InputStream;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

public class HelpManager {

	private final String HELP_PATH = "com/stratio/meta/sh/help/MetaClientHelp.yaml";
	
	public HelpContent loadHelpContent(){
		HelpContent result = null;
		InputStream is = HelpManager.class.getClassLoader().getResourceAsStream(HELP_PATH);
        try{
        	Constructor constructor = new Constructor(HelpContent.class);
        	Yaml yaml = new Yaml(constructor);
            result = yaml.loadAs(is, HelpContent.class);
            result.loadMap();
        }finally{
        	try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
        return result;
	}
}
