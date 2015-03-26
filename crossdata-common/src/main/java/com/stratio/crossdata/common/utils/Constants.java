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
package com.stratio.crossdata.common.utils;

/**
 * Class holding several constants.
 */
public final class Constants {

    /**
     * Default priority value.
     */
     public static final int DEFAULT_PRIORITY = 5;

    /**
     * Default pagination.
     */
    public static final int DEFAULT_PAGINATION = 0;

    /**
     * Virtual catalog name. It is used in nested queries as the catalog name of the virtual table.
     */
    public static final String VIRTUAL_CATALOG_NAME = "#virtual";

    public static final String TRIGGER_TOKEN = "_T";

    private Constants() {
    }
}
