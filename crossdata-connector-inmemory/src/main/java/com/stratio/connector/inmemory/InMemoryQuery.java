/*
 * Licensed to STRATIO (C) under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  The STRATIO (C) licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.stratio.connector.inmemory;

import com.stratio.connector.inmemory.datastore.InMemoryOperations;
import com.stratio.connector.inmemory.datastore.InMemoryRelation;
import com.stratio.connector.inmemory.datastore.selector.*;
import com.stratio.crossdata.common.exceptions.ExecutionException;
import com.stratio.crossdata.common.logicalplan.*;
import com.stratio.crossdata.common.statements.structures.*;

import java.util.*;

/**
 * Class that encapsulate the Project parse and
 */
public class InMemoryQuery {

    /**
     * Map with the equivalences between crossdata operators and the ones supported by our datastore.
     */
    private static final Map<Operator, InMemoryOperations> OPERATIONS_TRANFORMATIONS = new HashMap<>();

    static {
        OPERATIONS_TRANFORMATIONS.put(Operator.EQ, InMemoryOperations.EQ);
        OPERATIONS_TRANFORMATIONS.put(Operator.GT, InMemoryOperations.GT);
        OPERATIONS_TRANFORMATIONS.put(Operator.LT, InMemoryOperations.LT);
        OPERATIONS_TRANFORMATIONS.put(Operator.GET, InMemoryOperations.GET);
        OPERATIONS_TRANFORMATIONS.put(Operator.LET, InMemoryOperations.LET);
    }

    String tableName;
    String catalogName;
    List<InMemoryRelation> relations = new ArrayList<>();
    List<InMemorySelector> outputColumns;
    Project project;
    OrderBy orderByStep = null;
    Select selectStep;
    Join joinStep;
    int limit = -1;


    public InMemoryQuery(Project project) throws ExecutionException {
        this.project = project;
        extractSteps(project);
        tableName = project.getTableName().getName();
        catalogName = project.getCatalogName();
        outputColumns = transformIntoSelectors(selectStep.getColumnMap().keySet());

        processJoins();
    }

    public InMemoryQuery(Project project, List<Object[]> results) throws ExecutionException {
        this(project);

        out: for(Object[] row: results){
            for (Object field: row){
                if (field instanceof AbstractMap.SimpleEntry){
                    String columnName = joinStep.getJoinRelations().get(0).getRightTerm().getColumnName().getName();
                    new InMemoryRelation(columnName, OPERATIONS_TRANFORMATIONS.get(Operator.EQ), ((AbstractMap.SimpleEntry)field).getValue());
                    break out;
                }
            }
        }
    }

    private void processJoins() {


        if (joinStep != null){
            String name = joinStep.toString();
            Selector myTerm, otherTerm;
            if(joinStep.getJoinRelations().get(0).getLeftTerm().getTableName().equals(project.getTableName())) {
                myTerm =joinStep.getJoinRelations().get(0).getLeftTerm();
                otherTerm = joinStep.getJoinRelations().get(0).getRightTerm();
            }else{
                otherTerm =joinStep.getJoinRelations().get(0).getLeftTerm();
                myTerm = joinStep.getJoinRelations().get(0).getRightTerm();
            }
            outputColumns.add(new InMemoryJoinSelector(name,myTerm,otherTerm));
        }
    }

    private void extractSteps(Project project) throws ExecutionException{
        try {
            LogicalStep currentStep = project;
            while(currentStep != null){
                if(currentStep instanceof OrderBy){
                    orderByStep = (OrderBy) (currentStep);
                }else if(currentStep instanceof Select){
                    selectStep = (Select) currentStep;
                }else if (currentStep instanceof Join){
                    joinStep = (Join) currentStep;
                    break;
                } else if(currentStep instanceof Filter){
                    relations.add(toInMemoryRelation(Filter.class.cast(currentStep)));
                }else if(currentStep instanceof Limit){
                    limit = Limit.class.cast(currentStep).getLimit();
                }
                currentStep = currentStep.getNextStep();
            }
        } catch(ClassCastException e) {
            throw new ExecutionException("Invalid workflow received", e);
        }
    }

    /**
     * Transform a crossdata relationship into an in-memory relation.
     * @param f The {@link com.stratio.crossdata.common.logicalplan.Filter} logical step.
     * @return An equivalent {@link com.stratio.connector.inmemory.datastore.InMemoryRelation}.
     * @throws ExecutionException If the relationship cannot be translated.
     */
    private InMemoryRelation  toInMemoryRelation(Filter f) throws ExecutionException {
        ColumnSelector left = ColumnSelector.class.cast(f.getRelation().getLeftTerm());
        String columnName = left.getName().getName();
        InMemoryOperations relation;

        if(OPERATIONS_TRANFORMATIONS.containsKey(f.getRelation().getOperator())){
            relation = OPERATIONS_TRANFORMATIONS.get(f.getRelation().getOperator());
        }else{
            throw new ExecutionException("Operator " + f.getRelation().getOperator() + " not supported");
        }
        Selector rightSelector = f.getRelation().getRightTerm();
        Object rightPart = null;

        if(SelectorType.STRING.equals(rightSelector.getType())){
            rightPart = StringSelector.class.cast(rightSelector).getValue();
        }else if(SelectorType.INTEGER.equals(rightSelector.getType())){
            rightPart = IntegerSelector.class.cast(rightSelector).getValue();
        }else if(SelectorType.BOOLEAN.equals(rightSelector.getType())){
            rightPart = BooleanSelector.class.cast(rightSelector).getValue();
        }else if(SelectorType.FLOATING_POINT.equals(rightSelector.getType())){
            rightPart = FloatingPointSelector.class.cast(rightSelector).getValue();
        }

        return new InMemoryRelation(columnName, relation, rightPart);
    }

    /**
     * Transform a set of crossdata selectors into in-memory ones.
     * @param selectors The set of crossdata selectors.
     * @return A list of in-memory selectors.
     */
    private List<InMemorySelector> transformIntoSelectors(Set<Selector> selectors) {
        List<InMemorySelector> result = new ArrayList<>();
        for(Selector s: selectors){
            result.add(transformCrossdataSelector(s));
        }
        return result;
    }

    /**
     * Transform a Crossdata selector into an InMemory one.
     * @param selector The Crossdata selector.
     * @return The equivalent InMemory selector.
     */
    private InMemorySelector transformCrossdataSelector(Selector selector){
        InMemorySelector result;
        if(FunctionSelector.class.isInstance(selector)){
            FunctionSelector xdFunction = FunctionSelector.class.cast(selector);
            String name = xdFunction.getFunctionName();
            List<InMemorySelector> arguments = new ArrayList<>();
            for(Selector arg : xdFunction.getFunctionColumns()){
                arguments.add(transformCrossdataSelector(arg));
            }
            result = new InMemoryFunctionSelector(name, arguments);
        }else if(ColumnSelector.class.isInstance(selector)){
            ColumnSelector cs = ColumnSelector.class.cast(selector);
            result = new InMemoryColumnSelector(cs.getName().getName());
        }else{
            result = new InMemoryLiteralSelector(selector.getStringValue());
        }
        return result;
    }

    public List<Object[]> orderResult(List<Object[]> results) throws ExecutionException {
        if (orderByStep != null) {
            List<Object[]> orderedResult = new ArrayList<>();
            if ((results != null) && (!results.isEmpty())) {
                for (Object[] row : results) {
                    if (orderedResult.isEmpty()) {
                        orderedResult.add(row);
                    } else {
                        int order = 0;
                        for (Object[] orderedRow : orderedResult) {
                            if (compareRows(row, orderedRow, outputColumns, orderByStep)) {
                                break;
                            }
                            order++;
                        }
                        orderedResult.add(order, row);
                    }
                }
            }

            return orderedResult;
        }

        return results;

    }

    private boolean compareRows(
            Object[] candidateRow,
            Object[] orderedRow,
            List<InMemorySelector> outputColumns,
            OrderBy orderByStep) {
        boolean result = false;

        List<String> columnNames = new ArrayList<>();
        for(InMemorySelector selector : outputColumns){
            columnNames.add(selector.getName());
        }

        for(OrderByClause clause: orderByStep.getIds()){
            int index = columnNames.indexOf(clause.getSelector().getColumnName().getName());
            int comparison = compareCells(candidateRow[index], orderedRow[index], clause.getDirection());
            if(comparison != 0){
                result = (comparison > 0);
                break;
            }
        }
        return result;
    }

    private int compareCells(Object toBeOrdered, Object alreadyOrdered, OrderDirection direction) {
        int result = -1;
        InMemoryOperations.GT.compare(toBeOrdered, alreadyOrdered);
        if(InMemoryOperations.EQ.compare(toBeOrdered, alreadyOrdered)){
            result = 0;
        } else if(direction == OrderDirection.ASC){
            if(InMemoryOperations.LT.compare(toBeOrdered, alreadyOrdered)){
                result = 1;
            }
        } else if(direction == OrderDirection.DESC){
            if(InMemoryOperations.GT.compare(toBeOrdered, alreadyOrdered)){
                result = 1;
            }
        }
        return result;
    }
}
