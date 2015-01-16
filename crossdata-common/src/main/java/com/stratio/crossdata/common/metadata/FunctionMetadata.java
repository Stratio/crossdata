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

package com.stratio.crossdata.common.metadata;

import com.stratio.crossdata.common.data.FunctionName;

/**
 * Metadata information associated with a function.
 */
public class FunctionMetadata {

    /**
     * The name of the function.
     */
    private final FunctionName functionName;

    /**
     * The function signature.
     */
    private final String signature;

    /**
     * The function type distinguishing between simple or aggregation.
     */
    private final String functionType;

    /**
     * Class constructor.
     *
     * @param functionName The function name.
     * @param signature    The function signature.
     * @param functionType The function type.
     */
    public FunctionMetadata(FunctionName functionName, String signature, String functionType) {
        this.functionName = functionName;
        this.signature = signature;
        this.functionType = functionType;
    }

    /**
     * Get the name of the function.
     *
     * @return A {@link com.stratio.crossdata.common.data.FunctionName}.
     */
    public FunctionName getFunctionName() {
        return functionName;
    }

    /**
     * Get the function signature.
     *
     * @return A string representation of the signature.
     */
    public String getSignature() {
        String signature = this.signature;
        if (functionType.equalsIgnoreCase("aggregation")) {
            signature = signature.replace("(", "(List<").replace(")", ">)");
        }
        return signature;
    }

    /**
     * Get the function type.
     *
     * @return Either simple or aggregation.
     */
    public String getFunctionType() {
        return functionType;
    }

    /**
     * Get the returning type.
     *
     * @return The returning signature type.
     */
    public String getReturningType() {
        return signature.substring(signature.indexOf(":") + 1, signature.length());
    }
}
