package com.stratio.qa.specs;

import com.stratio.qa.specs.CommonG;
import com.stratio.qa.utils.XDContextUtil;
import com.stratio.qa.utils.XDContextUtils;
import com.stratio.qa.utils.XDJavaDriver;
//import com.stratio.tests.utils.XDJavaDriver;

/**
 * Created by hdominguez on 13/10/15.
 */
public class Common extends CommonG {

    //private CommonG commongspec;

    public Common() {
        
    }
//    public Common(CommonG spec) {
//        this.commongspec = spec;
//    }
//
//    public CommonG getCommonGspec() {
//        return this.commongspec;
//    }

    private final XDContextUtil XDCONTEXT = XDContextUtil.getInstance();

    public XDContextUtils getXdContext(){
        return XDCONTEXT.getXdContext();
    }

    public XDJavaDriver getXdDriver(){
        return XDCONTEXT.getXdDriver();
    }

}
