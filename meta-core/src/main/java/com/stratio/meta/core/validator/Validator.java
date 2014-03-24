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

package com.stratio.meta.core.validator;

import com.stratio.meta.core.utils.MetaQuery;
import org.apache.log4j.Logger;

public class Validator {

    private final Logger logger = Logger.getLogger(Validator.class);
    
    public MetaQuery validateQuery(MetaQuery metaQuery) {
        logger.warn("Not supported yet.");
        MetaValidation validation =new MetaValidation();
        metaQuery.setValidation(validation);
        return metaQuery;
    }
    
}
