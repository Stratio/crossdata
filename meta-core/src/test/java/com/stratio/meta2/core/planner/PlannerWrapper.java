package com.stratio.meta2.core.planner;

import java.util.List;

import com.stratio.meta.common.exceptions.PlanningException;
import com.stratio.meta.common.executionplan.ExecutionPath;
import com.stratio.meta.common.logicalplan.LogicalStep;
import com.stratio.meta2.common.metadata.ConnectorMetadata;

/**
 * Planner wrapper to test protected methods
 */
public class PlannerWrapper extends Planner{

    @Override
    public ExecutionPath defineExecutionPath(LogicalStep initial,
            List<ConnectorMetadata> availableConnectors) throws PlanningException{
        return super.defineExecutionPath(initial, availableConnectors);
    }
}
