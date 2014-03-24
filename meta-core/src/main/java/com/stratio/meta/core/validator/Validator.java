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
