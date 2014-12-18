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

import com.stratio.crossdata.common.data.TableName;

/**
 * Abstract class that represents the selection clause of a {@code SELECT} statement. It can either be
 * an Asterisk or a set of columns.
 */
public abstract class Selection {

    /**
     * Constant to define that the selection is composed of a set of fields.
     */
    public static final int TYPE_SELECTOR = 1;

    /**
     * Constant to define that the selection should include all fields of the target table.
     */
    public static final int TYPE_ASTERISK = 2;

    /**
     * Type of selection.
     */
    protected int type;

    /**
     * Get the type of selection.
     *
     * @return A number being 1 a {@code TYPE_SELECTOR} and 2 a {@code TYPE_ASTERISK}.
     */
    public int getType() {
        return type;
    }

    /**
     * Set the type of selection.
     *
     * @param type 1 for {@code TYPE_SELECTOR} and 2 for {@code TYPE_ASTERISK}.
     */
    public void setType(int type) {
        this.type = type;
    }

    @Override
    public abstract String toString();

    /**
     * Add a tablename to that relates to the list of tables refered by the selection.
     *
     * @param tablename The table name.
     */
    public abstract void addTablename(TableName tablename);

    /**
     * Checks whether the selection clause contains some includes or not.
     *
     * @return true, if functions are used; false, otherwise.
     */
    public abstract boolean containsFunctions();
}
