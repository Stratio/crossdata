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

import com.stratio.crossdata.common.utils.StringUtils;

public class PropertyClusteringOrder extends Property {

    private OrderBy orderBy;

    public PropertyClusteringOrder() {
        super(TYPE_CLUSTERING_ORDER);
    }

    public PropertyClusteringOrder(OrderBy orderBy) {
        super(TYPE_CLUSTERING_ORDER);
        this.orderBy = orderBy;
    }

    public OrderBy getOrder() {
        return orderBy;
    }

    public void setOrder(OrderBy orderBy) {
        this.orderBy = orderBy;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("CLUSTERING ORDER BY ");
        sb.append("(");
        sb.append(StringUtils.stringList(orderBy.getSelectorList(), ","));
        if (orderBy.getDirection() == OrderDirection.DESC) {
            sb.append(" DESC");
        }
        sb.append(")");
        return sb.toString();
    }

}
