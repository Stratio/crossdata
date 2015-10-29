/**
 * Copyright (C) 2015 Stratio (http://stratio.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
