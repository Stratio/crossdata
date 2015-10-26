package com.stratio.crossdata.testsAT.specs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.stratio.specs.CommonG;
import com.stratio.tests.utils.ThreadProperty;
import com.stratio.tests.utils.XDContextUtil;
import com.stratio.tests.utils.XDContextUtils;

/**
 * Created by hdominguez on 13/10/15.
 */
public class Common extends CommonG {

    private final Logger logger = LoggerFactory.getLogger(ThreadProperty.get("class"));

    private final XDContextUtil XDCONTEXT = XDContextUtil.getInstance();

    public Logger getLogger() {
        return this.logger;
    }

    public XDContextUtils getXdContext(){
        return XDCONTEXT.getXdContext();
    }




}
