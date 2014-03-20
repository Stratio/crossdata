# stratio-meta #

One LANGUAGE to rule them all

# Compiling META #

Compiling META involves generating a set of files (.tokens, Lexers, and Parsers) from the different grammar files. To automatically buildMETA execute the following command:
```
   > mvn clean compile install
```

Alternativelly, you can manually generate the grammar derived files first, and the proceed to the usual compilation using:
```
   > mvn generate-sources
   > mvn compile install
```

Both methods will create the following files required to compile and execute META.
```
   com/stratio/meta/core/grammar/generated/Meta.tokens
   com/stratio/meta/core/grammar/generated/MetaLexer.java
   com/stratio/meta/core/grammar/generated/MetaParser.java
   com/stratio/meta/sh/help/generated/MetaHelp.tokens
   com/stratio/meta/sh/help/generated/MetaHelpLexer.java
   com/stratio/meta/sh/help/generated/MetaHelpParser.java
```

# Running the META-shell#

The META-shell can be executed in two differentiated modes: client and server. A client shell launches a shell that connects to a set of META servers. Additionally, a server-shell is available to be launched in the META servers themselves. This type of execution does not required any type of network communication between the shell and the META server.

## META server shell ##

```
   > mvn exec:java -pl meta-sh -Dexec.mainClass="com.stratio.meta.sh.Metash"
```

## Useful commands ##
Once the shell is running, you can exit the program introducing the word **exit** or **quit** in the query prompt. A command help system is available by introducing the command **help**. A help entry is available per command, to check specify help topics use **help command**.

