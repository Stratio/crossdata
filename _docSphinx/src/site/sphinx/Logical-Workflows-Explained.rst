Logical Workflows Explained
***************************
This document will provided a detailed description of the types of elements that may appear in a Crossdata 
*LogicalWorkflow* and how the should be interpreted by the connectors.
  
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
provides methods to navigate the graph of logical steps.::


    /**
     * Get the type of operation associated with this filter.
     *
     * @return A {@link com.stratio.crossdata.common.metadata.Operations}.
     */
    public Operations getOperation() {
        return operation;
    }


Based on the number of input/ouput connections of a *LogicalStep*, we distinguish two types: *TransformationStep*, 
and *UnionStep*. A *TransformationStep* contains an operation that takes as input the output of a single 
*LogicalStep* and connects with a single *LogicalStep* afterwards. A *UnionStep* on the other hand, 
has several previous *LogicalSteps* (e.g., a *Join*).

Project
-------

The *Project* operator specifies the columns of a table that need to be retrieved from the database and are involved 
in the query. In a *SELECT* statement it will include the columns requested as output of the query, 
and the columns involved in any checking (e.g., columns in the where or join clauses).

Filter
------

The *Filter* operator specifies a filtering operation that should be applied to all the rows retrieved before that 
filtering element. In a *SELECT* statement *Filter* operator are associated with the relations found in the where
 clause. A *Filter* includes the left and right parts of the relationship as well as the *Operator* to be applied.

Select
------

The *Select* operator specifies which columns should be returned to the user, their order, 
and the alias to be applied when building the *QueryResult*.

Limit
-----

The *Limit* operator specifies the amount of rows to be returned to the user.

Window
------

The *Window* operator specifies a time window to retrieve data from streaming sources. The windows could either be 
specified in amount of tuples to be returned or in a time window. For example, a *SELECT ... WITH WINDOW 2 minutes* 
should provide results back to the user every 2 minutes after the query is started. In the case of absolute windows 
(*SELECT ... WITH WINDOW 3 ROWS*) the user will receive back results as soon as 3 elements matching the query arrive 
to the system.

Join
----

The *Join* operator specifies that the results comming from 2 different branches (they will be started by a 
*Project*) need to be merged. The operator contains information about the related *Project* logical steps, 
as well as information about the conditions to be applied during the join.

PartialResults
--------------

The *PartialResults* operator specifies a source of rows to be used for further processing. As an example, 
imagine a user trying to perform a *Join* between two datastore: d1 and d2; each of them accessible with a connector 
c1 and c2 respectively. If non of the connectors (c1 and c2) is able to directly perform a *Join* between tables in 
d1 and d2, but one of them supports joins with partial results the query will be solved in the following steps. 
Crossdata will define a LogicalWorkflow and based on the available connector it decides that requires two of them. 
Asuming c2 is the one with joins on partial results, Crossdata will send first a query to c1 to retrieve data from c1
. The resulting data will be injected in a *PartialResult* logical step, and that step linked as the input for the 
join operation. Then Crossdata will send the query to c2 in order to make the join.

GroupBy
-------

The *GroupBy* operator specifies the list of columns to be used to group results.

Examples
========

**Basic SELECT**

Given the following query::

    SELECT catalog.table1.column1, catalog.table1.column2 FROM catalog.table1;


Crossdata will generate the following LogicalWorkflow::

    SELECT catalog.table1.column1, catalog.table1.column2 FROM catalog.table1;


**Join with where clause**

Given the following query::

    SELECT catalog.table1.a, catalog.table1.b, catalog.table2.c, catalog.table2.d 
    FROM catalog.table1 INNER JOIN catalog.table2 ON catalog.table1.id = catalog.table2.id
    WHERE catalog.table1.a > 10 AND catalog.table2.d < 10;


Crossdata will generate the following LogicalWorkflow::


    PROJECT catalog.table1 ON cluster.test_cluster (catalog.table1.a, catalog.table1.b, catalog.table1.id)
    	FILTER - FILTER_NON_INDEXED_GT - catalog.table1.a > 10
    PROJECT catalog.table2 ON cluster.test_cluster (catalog.table2.c, catalog.table2.d, catalog.table2.id)
    	FILTER - FILTER_NON_INDEXED_LT - catalog.table2.d < 10
    JOIN ([catalog.table1, catalog.table2) ON [catalog.table1.id = catalog.table1.id]
    	SELECT (catalog.table1.a AS catalog.table1.a, catalog.table1.b AS catalog.table1.b, 
    	catalog.table2.c AS catalog.table2.c, catalog.table2.d AS catalog.table2.d)


More Information
================

For more information about the available operations, check the `Connector Operations <ConnectorOperations.html>`_
document
