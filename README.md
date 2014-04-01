# stratio-meta #

One LANGUAGE to rule them all

# Project description #

Stratio META unifies batch and streaming queries in a single language. META provides a distributed fault-tolerant server architecture that has the unique ability to execute queries in Cassandra, Spark, and/or Stratio Streaming. From an architecture point of view, Stratio META is composed of:

 * meta-common: Definition of common classes used by meta-server and meta-driver.
 * meta-core: Grammar definition, statement parsing, validation, and execution.
 * meta-server: The META server listens for client connections and executes the requested commands
 * meta-driver: Java API offered to client applications.
 * meta-sh: The interactive shell.

## Full documentation ##

See the Wiki for full documentation, examples, operational details and other information.

See the [Javadoc] () and [Language reference] () for the internal details.

## Prerequisites ##

In order to execute META, the following elements are required:

1. A working installation of Cassandra. The Stratio-cassandra fork is required to support the LUCENE indexes.
2. A working installation of Stratio Deep. Otherwise, advance capabilities such as SELECT with INNER JOIN will not be available.

## Compiling META ##

Compiling META involves generating a set of files (.tokens, Lexers, and Parsers) from the different grammar files. To automatically build Stratio META execute the following command:

```
   > mvn clean compile install
```

## Running the META-shell##

The META-shell allows the users to launch interactive queries against a set of META servers. The shell features:

 - History support (arrow navigation)
 - History search (ctrl-r)
 - Token completion (tab)
 - Help command

```
   > mvn exec:java -pl meta-sh -Dexec.mainClass="com.stratio.meta.sh.Metash"
```

## Useful commands ##

Once the shell is running, you can exit the program introducing the word **exit** or **quit** in the query prompt. A command help system is available by introducing the command **help**. A help entry is available per command, to check specify help topics use **help command**.

# License #

Stratio Meta is licensed as [LGPL](https://www.gnu.org/licenses/gpl-howto.html)

Copyright (c) 2014, Stratio, All rights reserved.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 3.0 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library.
