package com.stratio.meta.core.engine;

import com.stratio.meta.core.executor.Executor;
import com.stratio.meta.core.parser.Parser;
import com.stratio.meta.core.planner.Planner;
import com.stratio.meta.core.validator.Validator;

public class Engine {
    private final Parser parser;
    private final Validator validator;
    private final Planner planner;
    private final Executor executor;
    
    public Engine(EngineConfig config) {
        parser = new Parser();
        validator = new Validator();
        planner = new Planner();
        executor = new Executor(config.getCassandraHosts(),config.getCassandraPort());
    }
       
    public Parser getParser() {
        return parser;
    }

    public Validator getValidator() {
        return validator;
    }

    public Planner getPlanner() {
        return planner;
    }

    public Executor getExecutor() {
        return executor;
    }
    
}
