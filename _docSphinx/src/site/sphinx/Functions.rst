CROSSDATA Functions
---

Version: 0.2.0

Date: 7, Jan, 2015


* * * * *

Introduction
============

Crossdata functions have been added as a new feature since version 0.2.0. This document introduces the key concepts
of this feature and provides some examples in order to help the developer to understand the idea behind the Crossdata
 functions.

* * * * *

Declaration
===========

Functions declarations can be carried out in two documents:

 - Datastore manifest
 - Connector manifest

When a function is declared in a datastore manifest, all the connectors attached to a cluster of that datastore will
inherit these functions and thus these connectors could invoke the declared functions. This behaviour can be modified
 as connectors manifests can add its own functions and it can also exclude functions already declared in the datastore
 which is attached to.

The required structure for adding a function to a manifest is:

    <Functions>
        <Function>
            <FunctionName>function_name</FunctionName>
            <Signature>signature</Signature>
            <FunctionType>function_type</FunctionType>
            <Description>function description</Description>
        </Function>
        ...
    </Functions>

If a exclusion is required, we only have to mention the name in the connector manifest:

    <Functions>
            ...
            <Exclude>
                <FunctionName>function_name<FunctionName>
            </Exclude>
    </Functions>

* * * * *

Function Name
=============

Functions names must be a char sequence starting with a letter, without spaces and they can contain letters,
numbers and the underscore symbol.

* * * * *

Signature
=========

This tag must provide the information about the data types of the incoming parameters of the function and the
expected data type of the result. The data types have to meet the ones supported by Crossdata. In order to give an
abstraction of the column, we will enclose the data types in square brackets. An asterisk can be also used to declare
 a variable number of columns. A generic signature must coincide with the next structure:

    function_name(Tuple[data_type1, data_type2, ..., data_typeN]):Tuple[data_type]

* * * * *

Function Type
=============

Function type should keep to two types:

 - simple
 - aggregation


* * * * *

Description
===========

This is an optional tag but its usage is highly recommended given that other users can obtain information about the
intention and the expected behaviour of the function.

* * * * *

Use in grammar
==============

As for version 0.2.0, **THE USE OF FUNCTIONS IS LIMITED TO SELECTORS**. Functions are not supported yet for where
clauses and other structures of a query. Therefore, when issuing a select statement,
we only have to provide the name of the function and the name of the columns (if any) in parenthesis. In that way,
a generic query with a function will look like:

    SELECT col1, function_name(col2, col3) FROM table_name;

* * * * *

Example
=======

We'll start with the declaration of two functions in a datastore manifest:

    <DataStore>
        ...
        <Functions>
                <Function>
                    <FunctionName>count</FunctionName>
                    <Signature>count(Tuple[Any*]):Tuple[Int]</Signature>
                    <FunctionType>aggregation</FunctionType>
                    <Description>It counts the number of rows</Description>
                </Function>
                <Function>
                    <FunctionName>round</FunctionName>
                    <Signature>round(Tuple[Double]):Tuple[Int]</Signature>
                    <FunctionType>simple</FunctionType>
                    <Description>It rounds a decimal number to a integer number</Description>
                </Function>
        </Functions>
    </DataStore>

Let's imagine that a cluster was created and attached to this datastore and now we want to attach a connector to that
 cluster but previously we have to add the manifest of this connector, adding a new function and excluding one of the
 function of the datastore:

     <Connector>
         ...
         <SupportedOperations>
            ...
            <operation>SELECT_FUNCTIONS</operation>
         </SupportedOperations>
         <Functions>
                 <Function>
                    <FunctionName>concat</FunctionName>
                    <Signature>concat(Tuple[Text, Text]):Tuple[Text]</Signature>
                    <FunctionType>simple</FunctionType>
                    <Description>It concats the content of two columns</Description>
                 </Function>
                 <Exclude>
                     <FunctionName>round</FunctionName>
                 </Exclude>
         </Functions>
     </Connector>

Once we have added these two manifest and we have attached the cluster and the connector,
we can use the declared functions:

    SELECT COUNT(*) FROM clients;
    SELECT Concat(name, surname) FROM clients;

