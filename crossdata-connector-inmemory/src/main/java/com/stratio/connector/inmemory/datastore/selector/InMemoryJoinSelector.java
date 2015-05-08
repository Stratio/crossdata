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

package com.stratio.connector.inmemory.datastore.selector;

import com.stratio.crossdata.common.statements.structures.Selector;

/**
 * Represents a Join Selector.
 */
public class InMemoryJoinSelector extends InMemorySelector{

    private Selector myTerm;
    private Selector otherTerm;

    /**
     * Build a Join Selector with the Join Name, the Term that owns this side of the relation and the other join term.

     * @param name Join Name.
     * @param myTerm The Selector that represents this Join Side.
     * @param otherTerm The selector that represents the other join side.
     */
    public InMemoryJoinSelector(String name, Selector myTerm, Selector otherTerm) {
        super(name);
        this.myTerm = myTerm;
        this.otherTerm = otherTerm;
    }

    /**
     * @return other join term.
     */
    public Selector getOtherTerm() {
        return otherTerm;
    }

    /**
     * @return this Join side term.
     */
    public Selector getMyTerm() {
        return myTerm;
    }
}
