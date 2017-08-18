/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
 */
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
