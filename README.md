# stratio-meta #

One LANGUAGE to rule them all

# Compiling META #

Compiling META involves generating a set of files (.tokens, Lexers, and Parsers) from the different grammar files. To automatically buildMETA execute the following command:
```
   > mvn clean compile
```

Alternativelly, you can manually generate the grammar derived files first, and the proceed to the usual compilation using:
```
   > mvn generate-sources
   > mvn compile
```

Both methods will create the following files required to compile and execute META.
```
   com/stratio/meta/grammar/generated/Meta.tokens
   com/stratio/meta/grammar/generated/MetaLexer.java
   com/stratio/meta/grammar/generated/MetaParser.java
   com/stratio/meta/client/help/generated/MetaHelp.tokens
   com/stratio/meta/client/help/generated/MetaHelpLexer.java
   com/stratio/meta/client/help/generated/MetaHelpParser.java
```
1. 

# Running the META-shell#

The META-shell can be executed in two differentiated modes: client and server. A client shell launches a shell that connects to a set of META servers. Additionally, a server-shell is available to be launched in the META servers themselves. This type of execution does not required any type of network communication between the shell and the META server.

## META server shell ##

```
   > mvn exec:java -Dexec.mainClass="com.stratio.meta.ui.shell.server.Metash"
```

## Useful commands ##
Once the shell is running, you can exit the program introducing the word **exit** or **quit** in the query prompt. A command help system is available by introducing the command **help**. A help entry is available per command, to check specify help topics use **help command**.

