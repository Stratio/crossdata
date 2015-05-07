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

package com.stratio.crossdata.common.statements.structures;

import java.io.Serializable;

/**
 * OrderByClause Class.
 */
public class OrderByClause implements Serializable, ISqlExpression{

    private static final long serialVersionUID = 738701511608570694L;
    private Selector selector;
    private OrderDirection direction = OrderDirection.ASC;

    /**
     * Constructor Class.
     */
    public OrderByClause() {
    }

    /**
     * Constructor Class.
     * @param direction The order direction.
     * @param selector The selector.
     */
    public OrderByClause(OrderDirection direction,
            Selector selector) {
        this.direction = direction;
        this.selector = selector;
    }

    /**
     * Constructor Class.
     * @param selector The selector.
     */
    public OrderByClause(Selector selector) {
        this.selector = selector;
    }

    /**
     * Get the Selector of the Group By.
     * @return The Selector of the Group By.
     */
    public Selector getSelector() {
        return selector;
    }

    /**
     * Set the selector of the Group By.
     * @param selector The selector.
     */
    public void setSelector(Selector selector) {
        this.selector = selector;
    }

    /**
     * Get the direction of the Group By.
     * @return Direction of the Group By.
     */
    public OrderDirection getDirection() {
        return direction;
    }

    /**
     * Set the direction of the GroupByClause.
     * @param direction
     */
    public void setDirection(OrderDirection direction) {
        this.direction = direction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        OrderByClause that = (OrderByClause) o;

        if (direction != that.direction) {
            return false;
        }
        if (selector != null ? !selector.equals(that.selector) : that.selector != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = selector != null ? selector.hashCode() : 0;
        result = 31 * result + (direction != null ? direction.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(selector);

        if (direction == OrderDirection.DESC) {
            sb.append(" DESC");
        }

        return sb.toString();
    }

    /**
     * Get the sql string of the order by clause.
     * @param withAlias Whether the expression must use alias or qualified names.
     * @return
     */
    public String toSQLString(boolean withAlias) {
        StringBuilder sb = new StringBuilder();

        sb.append(selector.toSQLString(false));

        if (direction == OrderDirection.DESC) {
            sb.append(" DESC");
        }

        return sb.toString();
    }
}
