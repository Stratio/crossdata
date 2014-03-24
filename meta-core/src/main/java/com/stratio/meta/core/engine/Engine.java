package com.stratio.meta.core.engine;

import com.datastax.driver.core.Session;
import com.stratio.meta.common.result.QueryResult;
import com.stratio.meta.core.executor.Executor;
import com.stratio.meta.core.parser.Parser;
import com.stratio.meta.core.planner.MetaPlan;
import com.stratio.meta.core.planner.Planner;
import com.stratio.meta.core.utils.MetaQuery;
import com.stratio.meta.core.validator.MetaValidation;
import com.stratio.meta.core.validator.Validator;

public class Engine {
    private Parser parser;
    private Validator validator;
    private Planner planner;
    private Executor executor;
    
    public Engine(Session session) {
        parser = new Parser();
        validator = new Validator();
        planner = new Planner();
        executor = new Executor(session);
    }
       
    public Parser getParser() {
        return parser;
    }

    public void setParser(Parser parser) {
        this.parser = parser;
    }

    public Validator getValidator() {
        return validator;
    }

    public void setValidator(Validator validator) {
        this.validator = validator;
    }

    public Planner getPlanner() {
        return planner;
    }

    public void setPlanner(Planner planner) {
        this.planner = planner;
    }

    public Executor getExecutor() {
        return executor;
    }

    public void setExecutor(Executor executor) {
        this.executor = executor;
    }        

    public MetaQuery parseStatement(String query) {
        return parser.parseStatement(query);
    }

    public MetaValidation validateQuery(MetaQuery metaQuery) {
        return validator.validateQuery(metaQuery);
    }

    public MetaPlan planQuery(MetaQuery metaQuery) {
        return planner.planQuery(metaQuery);
    }

    public QueryResult executeQuery(MetaQuery metaQuery) {
        return executor.executeQuery(metaQuery);
    }
    
}
