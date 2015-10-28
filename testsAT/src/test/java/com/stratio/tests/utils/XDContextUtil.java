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
package com.stratio.tests.utils;

/**
 * Created by hdominguez on 13/10/15.
 */
public class XDContextUtil {
    private static XDContextUtil instance = new XDContextUtil();
    private final XDContextUtils cUtils = new XDContextUtils();

    private XDContextUtil() {
    }

    public static XDContextUtil getInstance() {
        return instance;
    }

    public XDContextUtils getXdContext() {
        return cUtils;
    }

}
