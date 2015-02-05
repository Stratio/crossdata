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

package com.stratio.crossdata.core.validator.requirements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * ValidationRequirements.
 */
public class ValidationRequirements {
    private final List<ValidationTypes> validations;

    /**
     * Class Constructor.
     *
     * @param types The enum with the validation types
     */
    public ValidationRequirements(ValidationTypes... types) {
        this.validations = Arrays.asList(types);
    }

    /**
     * Initialize the validations.
     */
    public ValidationRequirements() {
        this.validations = new ArrayList<>();
    }

    /**
     * Get the validations of a query.
     *
     * @return A {@link java.util.List} with {@link com.stratio.crossdata.core.validator.requirements.ValidationTypes}
     */
    public List<ValidationTypes> getValidations() {
        return validations;
    }

    /**
     * Add a new validation for a query.
     *
     * @param requirement The validation.
     * @return {@link com.stratio.crossdata.core.validator.requirements.ValidationRequirements}
     */
    public ValidationRequirements add(ValidationTypes requirement) {
        this.validations.add(requirement);
        return this;
    }
}
