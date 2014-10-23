/*
 * Licensed to STRATIO (C) under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  The STRATIO (C) licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.stratio.crossdata.core.structures;

import java.io.Serializable;

import com.stratio.crossdata.common.statements.structures.Selector;

@Deprecated
public class Ordering implements Serializable {

    private static final long serialVersionUID = -5118851402738503002L;

    private Selector selector;
    private boolean dirInc;
    private OrderDirection orderDir;

    public Ordering(Selector selector, boolean dirInc, OrderDirection orderDir) {
        this.selector = selector;
        this.dirInc = dirInc;
        this.orderDir = orderDir;
    }

    public Ordering(Selector selector) {
        this(selector, false, null);
    }

    public Selector getSelector() {
        return selector;
    }

    public void setSelector(Selector selector) {
        this.selector = selector;
    }

    public boolean isDirInc() {
        return dirInc;
    }

    public void setDirInc(boolean dirInc) {
        this.dirInc = dirInc;
    }

    public OrderDirection getOrderDir() {
        return orderDir;
    }

    public void setOrderDir(OrderDirection orderDir) {
        this.dirInc = true;
        this.orderDir = orderDir;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(selector.toString());
        if (dirInc) {
            sb.append(" ").append(orderDir);
        }
        return sb.toString();
    }

}
