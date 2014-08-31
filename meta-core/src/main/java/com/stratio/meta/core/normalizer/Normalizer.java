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

package com.stratio.meta.core.normalizer;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.stratio.meta.common.exceptions.ValidationException;
import com.stratio.meta.common.exceptions.validation.AmbiguousNameException;
import com.stratio.meta.common.exceptions.validation.NotExistNameException;
import com.stratio.meta2.common.metadata.TableMetadata;
import com.stratio.meta2.common.data.CatalogName;
import com.stratio.meta2.common.data.ColumnName;
import com.stratio.meta2.common.data.TableName;
import com.stratio.meta2.common.statements.structures.selectors.*;
import com.stratio.meta2.core.metadata.MetadataManager;
import com.stratio.meta2.core.query.NormalizedQuery;
import com.stratio.meta2.core.query.SelectParsedQuery;

public class Normalizer {

  public NormalizedQuery normalize(SelectParsedQuery parsedQuery) throws ValidationException {
    NormalizedFields fields=new NormalizedFields();

    return new NormalizedQuery(parsedQuery);
  }




}
