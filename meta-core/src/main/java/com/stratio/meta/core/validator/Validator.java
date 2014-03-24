package com.stratio.meta.core.validator;

import com.stratio.meta.core.utils.MetaQuery;
import org.apache.log4j.Logger;

public class Validator {

    private final Logger logger = Logger.getLogger(Validator.class);
    
    public MetaValidation validateQuery(MetaQuery metaQuery) {
        logger.warn("Not supported yet.");
        return new MetaValidation();
    }
    
}
