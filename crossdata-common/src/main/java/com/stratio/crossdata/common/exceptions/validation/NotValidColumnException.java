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

package com.stratio.crossdata.common.exceptions.validation;

import com.stratio.crossdata.common.exceptions.ValidationException;
import com.stratio.crossdata.common.data.ColumnName;

/**
 * Exception Class that is thrown when a column is not valid in validator phase.
 */
public class NotValidColumnException extends ValidationException {

    private static final long serialVersionUID = 479970826365384829L;

    /**
     * Constructor class.
     * @param name The name of the column affected.
     */
    public NotValidColumnException(ColumnName name) {
        super(name + " is not valid column in this sentence");
    }
}
