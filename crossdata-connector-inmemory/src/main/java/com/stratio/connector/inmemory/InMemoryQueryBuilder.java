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
import com.stratio.connector.inmemory.datastore.InMemoryQuery;
import com.stratio.connector.inmemory.datastore.InMemoryRelation;
import com.stratio.connector.inmemory.datastore.datatypes.JoinValue;
import com.stratio.connector.inmemory.datastore.datatypes.SimpleValue;
import com.stratio.connector.inmemory.datastore.structures.InMemoryColumnSelector;
import com.stratio.connector.inmemory.datastore.structures.InMemoryJoinSelector;
import com.stratio.connector.inmemory.datastore.structures.InMemorySelector;
import com.stratio.crossdata.common.data.ColumnName;
import com.stratio.crossdata.common.data.TableName;
import com.stratio.crossdata.common.exceptions.ExecutionException;
import com.stratio.crossdata.common.logicalplan.*;
import com.stratio.crossdata.common.statements.structures.*;

import java.util.*;

/**
 * Class that encapsulate the {@link com.stratio.crossdata.common.logicalplan.Project} parse.
 */
public class InMemoryQueryBuilder {

    /**
     * Map with the equivalences between crossdata operators and the ones supported by our datastore.
     */
    private static final Map<Operator, InMemoryOperations> OPERATIONS_TRANFORMATIONS = new HashMap<>();

    /**
     * Singleton instance.
     */
    private final static InMemoryQueryBuilder instance = new InMemoryQueryBuilder();

    static {
        OPERATIONS_TRANFORMATIONS.put(Operator.EQ, InMemoryOperations.EQ);
        OPERATIONS_TRANFORMATIONS.put(Operator.GT, InMemoryOperations.GT);
        OPERATIONS_TRANFORMATIONS.put(Operator.LT, InMemoryOperations.LT);
        OPERATIONS_TRANFORMATIONS.put(Operator.GET, InMemoryOperations.GET);
        OPERATIONS_TRANFORMATIONS.put(Operator.LET, InMemoryOperations.LET);
        OPERATIONS_TRANFORMATIONS.put(Operator.IN , InMemoryOperations.IN);
    }

    public static InMemoryQueryBuilder instance() {
        return instance;
    }

    public InMemoryQuery build(Project project) throws ExecutionException {
        InMemoryQuery result = new InMemoryQuery();

        result.setTableName(project.getTableName().getName());
        result.setCatalogName(project.getCatalogName());
        result.getRelations().addAll(extractFilters(project));
        result.getOutputColumns().addAll(transformIntoSelectors(project.getColumnList()));

        processJoins(result.getOutputColumns(), project);


        return result;
    }

    /**
     *
     * Build a InMemoryQuery using a {@link com.stratio.crossdata.common.logicalplan.Project} and a
     * result of the others parts of the query, to by added as filters in this part.
     *
     * @param project
     * @param results
     * @return
     */
    public InMemoryQuery build(Project project,List<SimpleValue[]> results) throws ExecutionException {
        InMemoryQuery query = build(project);
        query.getRelations().addAll(getJoinFilters(project, results));

        return query;
    }


    /**
     * Return the JoinTerm of the project.
     *
     * @param joinStep
     * @param project
     * @return
     */
    private Selector getMyJoinTerm(Join joinStep, Project project) {

        Selector myTerm;

        TableName leftTable = joinStep.getJoinRelations().get(0).getLeftTerm().getTableName();
        TableName thisTableName = project.getTableName();

        if(leftTable.getQualifiedName().equals(thisTableName.getQualifiedName())
                || leftTable.getName().equals(thisTableName.getAlias())) {
            myTerm =joinStep.getJoinRelations().get(0).getLeftTerm();
        }else{
            myTerm = joinStep.getJoinRelations().get(0).getRightTerm();
        }
        return myTerm;
    }

    /**
     * Adds the new Output Columns that holds the JoinColumns.
     */
    private void processJoins(List<InMemorySelector> outputColumns, Project project) throws ExecutionException {

        Join joinStep = extractStep(project, Join.class);
        if (joinStep != null){
            String name = joinStep.toString();
            Selector myTerm = getMyJoinTerm(joinStep, project);
            Selector otherTerm;

            TableName leftTable = joinStep.getJoinRelations().get(0).getLeftTerm().getTableName();
            TableName thisTableName = project.getTableName();

            if (leftTable.getQualifiedName().equals(thisTableName.getQualifiedName()) ||
                    leftTable.getName().equals(thisTableName.getAlias())) {
                otherTerm = joinStep.getJoinRelations().get(0).getRightTerm();
            }else{
                otherTerm =joinStep.getJoinRelations().get(0).getLeftTerm();
            }

            outputColumns.add(new InMemoryJoinSelector(name,myTerm,otherTerm));
        }
    }

    /**
     * Adds a "IN" filter for each JoinColumn, using the values from the result List.
     *
     * @param results the results of the other terms that will be used as filter values.
     */
    private List<InMemoryRelation>  getJoinFilters(Project project, List<SimpleValue[]> results) throws ExecutionException {


        int columnIndex = 0;
        Join joinStep = extractStep(project, Join.class);
        List<InMemoryRelation> relations  = new ArrayList<>();

        for (SimpleValue field: results.get(0)){
            if (field instanceof JoinValue){
                String columnName = getMyJoinTerm(joinStep, project).getColumnName().getName();
                List<Object> joinValues = new ArrayList<>();
                for (SimpleValue[] row:results){
                    joinValues.add(row[columnIndex].getValue());
                }
                InMemoryRelation joinFilter =  new InMemoryRelation(columnName, OPERATIONS_TRANFORMATIONS.get(Operator.IN), joinValues);
                relations.add(joinFilter);
            }

            columnIndex++;
        }

        return relations;
    }

    /**
     * Parse and transform the {@link Project} until a {@link com.stratio.crossdata.common.logicalplan.Join} to a
     * InMemory Query representation.
     *
     * @param project
     * @throws ExecutionException
     */
    private <T extends LogicalStep> T extractStep(Project project, Class<T> stepWanted) throws ExecutionException{
        try {
            LogicalStep currentStep = project;
            while(currentStep != null){
                if(stepWanted.isInstance(currentStep)){
                    return stepWanted.cast(currentStep);
                }
                currentStep = currentStep.getNextStep();
            }
        } catch(ClassCastException e) {
            throw new ExecutionException("Invalid workflow received", e);
        }

        return null;
    }

    private  List<InMemoryRelation> extractFilters(Project project) throws ExecutionException{

        List<InMemoryRelation> relations = new ArrayList<>();

        try {
            LogicalStep currentStep = project;
            while(currentStep != null){
                if(Filter.class.isInstance(currentStep)){
                    relations.add(toInMemoryRelation(Filter.class.cast(currentStep)));
                }
                currentStep = currentStep.getNextStep();
            }
        } catch(ClassCastException e) {
            throw new ExecutionException("Invalid workflow received", e);
        }

        return relations;
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
     * @param columns The set of crossdata selectors.
     * @return A list of in-memory selectors.
     */
    private List<InMemorySelector> transformIntoSelectors(List<ColumnName> columns) {
        List<InMemorySelector> result = new ArrayList<>();
        for(ColumnName columnName: columns){
            result.add(new InMemoryColumnSelector(columnName.getName()));
        }
        return result;
    }

}
