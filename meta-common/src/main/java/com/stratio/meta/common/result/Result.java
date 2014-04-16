/*
 * Stratio Meta
 *
 * Copyright (c) 2014, Stratio, All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 */

package com.stratio.meta.common.result;

import java.io.Serializable;

public abstract class Result implements Serializable {

    private static final long serialVersionUID = 8596330240278204430L;
    private final boolean error ;
    private final String errorMessage ;
    private final boolean ksChanged ;
    private final String currentKeyspace ;

    Result(boolean error, String errorMessage, boolean ksChanged, String currentKeyspace) {
        this.error = error;
        this.errorMessage = errorMessage;
        this.ksChanged = ksChanged;
        this.currentKeyspace = currentKeyspace;
    }

    public boolean hasError() {
        return error;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public boolean isKsChanged() {
        return ksChanged;
    }

    public String getCurrentKeyspace() {
        return currentKeyspace;
    }

}
