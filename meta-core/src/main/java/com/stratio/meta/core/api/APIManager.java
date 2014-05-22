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

package com.stratio.meta.core.api;

import com.datastax.driver.core.Session;
import com.stratio.meta.common.ask.APICommand;
import com.stratio.meta.common.ask.Command;
import com.stratio.meta.common.result.CommandResult;
import com.stratio.meta.common.result.MetadataResult;
import com.stratio.meta.common.result.Result;
import com.stratio.meta.core.metadata.MetadataManager;
import org.apache.log4j.Logger;

public class APIManager {

    /**
     * Class logger.
     */
    private static final Logger LOG = Logger.getLogger(APIManager.class);

    /**
     * Metadata manager.
     */
    private final MetadataManager metadata;

    /**
     * Class constructor.
     * @param session Cassandra session used to retrieve the metadata.
     */
    public APIManager(Session session){
        metadata = new MetadataManager(session);
        metadata.loadMetadata();
    }

    /**
     * Process an incoming API request.
     * @param cmd The commnand to be executed.
     * @return A {@link com.stratio.meta.common.result.MetadataResult}.
     */
    public Result processRequest(Command cmd){
        Result result = null;
        if(APICommand.LIST_CATALOGS().equals(cmd.commandType())){
            LOG.info("Processing " + APICommand.LIST_CATALOGS().toString());
            result = MetadataResult.createSuccessMetadataResult(metadata.getKeyspacesNames(), true);
        }else if(APICommand.LIST_TABLES().equals(cmd.commandType())){
            LOG.info("Processing " + APICommand.LIST_TABLES().toString());
            result = MetadataResult.createSuccessMetadataResult(metadata.getTablesNames(cmd.params().get(0)), false);
        }else{
            result = CommandResult.createFailCommanResult("Command " + cmd.commandType() + " not supported");
            LOG.error(result.getErrorMessage());
        }
        return result;
    }
}
