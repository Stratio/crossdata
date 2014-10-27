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

import com.stratio.crossdata.common.statements.structures.Selector;

public class Option {

    public static final int OPTION_PROPERTY = 1;
    public static final int OPTION_COMPACT = 2;
    public static final int OPTION_CLUSTERING = 3;

    private int fixedOption;
    private Selector nameProperty;
    private Selector valueProperty;

//TODO:javadoc
    public Option(int fixedOption, Selector nameProperty, Selector properties) {
        this.fixedOption = fixedOption;
        this.nameProperty = nameProperty;
        this.valueProperty = properties;
    }

//TODO:javadoc
    public Option(int fixedOption) {
        this(fixedOption, null, null);
    }

//TODO:javadoc
    public Option(Selector nameProperty, Selector properties) {
        this.fixedOption = OPTION_PROPERTY;
        this.nameProperty = nameProperty;
        this.valueProperty = properties;
    }

//TODO:javadoc
    public int getFixedOption() {
        return fixedOption;
    }

//TODO:javadoc
    public void setFixedOption(int fixedOption) {
        this.fixedOption = fixedOption;
    }

//TODO:javadoc
    public Selector getNameProperty() {
        return nameProperty;
    }

//TODO:javadoc
    public void setNameProperty(Selector nameProperty) {
        this.nameProperty = nameProperty;
    }

//TODO:javadoc
    public Selector getProperties() {
        return valueProperty;
    }

//TODO:javadoc
    public void setProperties(Selector properties) {
        this.valueProperty = properties;
    }

    @Override
//TODO:javadoc
    public String toString() {
        StringBuilder sb = new StringBuilder();
        switch (fixedOption) {
        case OPTION_PROPERTY:
            sb.append(nameProperty).append(" = ").append(valueProperty.toString());
            break;
        case OPTION_COMPACT:
            sb.append("COMPACT STORAGE");
            break;
        case OPTION_CLUSTERING:
            sb.append("CLUSTERING ORDER");
            break;
        default:
            break;
        }
        return sb.toString();
    }

}
