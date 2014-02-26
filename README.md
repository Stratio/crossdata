stratio-meta
============

One LANGUAGE to rule them all

--- How to run ---

1.- The fist thing is to create the archives derivated of the grammar (.g file). In order to do it, we have to use a maven command:

	mvn generate-sources

This will create 3 archives: Meta.tokens, MetaLexer.java and MetaParser.java in the com.stratio.sdh.meta.generated package as pom.xml is configured to do it automatically.

2.- Now it's ready to be runned:
	
	- Maven: mvn exec:java -Dexec.mainClass="com.stratio.meta.ui.shell.server.Metash"
	
3.- Once is running, if we want to exit the program, we only have to introduce the word "exit" when a query is asked.

