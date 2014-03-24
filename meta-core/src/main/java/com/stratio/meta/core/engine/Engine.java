/*
 * Stratio Meta
 *
 * Copyright (c) 2014, Stratio, All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 */

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
