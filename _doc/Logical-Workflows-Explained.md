# Logical Workflows Explained #

This document will provided a detailed description of the types of elements that may appear in a Crossdata 
*LogicalWorkflow* and how the should be interpreted by the connectors. The document is organized as follows:

1. [Definition of LogicalWorkflow](#definition-of-logicalworkflow)
2. [Types of LogicalSteps](#types-of-logical-steps)
  1. [Project](#project)
  3. [Filter](#filter)
  2. [Select](#select)
  4. [Limit](#limit)
  5. [Window](#window)
  6. [Join](#join)
  7. [PartialResults](#partialresults)
  8. [GroupBy](#groupby)
  
Definition of LogicalWorkflow
=============================

The use of *LogicalWorkflow* is associated with *SELECT* statements. This type of statements is decomposed in a set 
of *LogicalStep* and those steps are organized in a directed graph that contain all the information required to 
perform a query. This approch is similar to the classical *Logical plan* found in databases. The difference is that 
in Crossdata we first characterize and define the *LogicalSteps* involved, and then we plan the execution of portions
 of this graph using the available connectors producing the final *ExecutionWorkflows*. Notice that a Crossdata 
 *LogicalWorkflow* is a similar concept to the classical database *LogicalPlan*.
 
A *LogicalWorkflow* is characterized by having several entry points for the graph. Those entry points are named 
initial steps and contain a list of Project LogicalSteps. From each initial step we can navigate the graph until the 
*last step* is reached. The motivation of allowing several initial steps is to facilitate the processing allowing 
parallel reading of several tables in union-like sentences such as INNER JOIN.

Types of LogicalSteps
=====================

A *LogicalWorkflow* contains a graph structure with the different *LogicalSteps*. A *LogicalStep* is the base class 
for the different operations that can be executed in the system. All *LogicalStep* will have an *Operation* 
associated to them that matches those defined in the connector manifest. In this way, 
each logical step has information about the type of operation to be executed. Apart from that, the *LogicalStep* 
provides methods to navigate the graph of logical steps.

```java

    /**
     * Get the type of operation associated with this filter.
     *
     * @return A {@link com.stratio.crossdata.common.metadata.Operations}.
     */
    public Operations getOperation() {
        return operation;
    }
```

Based on the number of input/ouput connections of a *LogicalStep*, we distinguish two types: *TransformationStep*, 
and *UnionStep*. A *TransformationStep* contains an operation that takes as input the output of a single 
*LogicalStep* and connects with a single *LogicalStep* afterwards. A *UnionStep* on the other hand, 
has several previous *LogicalSteps* (e.g., a *Join*).

Project
-------

Filter
------

Select
------

Limit
-----

Window
------

Join
----

PartialResults
--------------

GroupBy
-------

Examples
--------

**Basic SELECT**

Given the following query:

```sql

    SELECT catalog.table1.column1, catalog.table1.column2 FROM catalog.table1;
```

Crossdata will generate the following LogicalWorkflow:

```

    SELECT catalog.table1.column1, catalog.table1.column2 FROM catalog.table1;
```

**Join with where clause**

Given the following query:

```sql

    SELECT catalog.table1.a, catalog.table1.b, catalog.table2.c, catalog.table2.d 
    FROM catalog.table1 INNER JOIN catalog.table2 ON catalog.table1.id = catalog.table2.id
    WHERE catalog.table1.a > 10 AND catalog.table2.d < 10;
```

Crossdata will generate the following LogicalWorkflow:

```

    PROJECT catalog.table1 ON cluster.test_cluster (catalog.table1.a, catalog.table1.b, catalog.table1.id)
    	FILTER - FILTER_NON_INDEXED_GT - catalog.table1.a > 10
    PROJECT catalog.table2 ON cluster.test_cluster (catalog.table2.c, catalog.table2.d, catalog.table2.id)
    	FILTER - FILTER_NON_INDEXED_LT - catalog.table2.d < 10
    JOIN ([catalog.table1, catalog.table2) ON [catalog.table1.id = catalog.table1.id]
    	SELECT (catalog.table1.a AS catalog.table1.a, catalog.table1.b AS catalog.table1.b, 
    	catalog.table2.c AS catalog.table2.c, catalog.table2.d AS catalog.table2.d)
```

