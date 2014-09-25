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

package com.stratio.meta.rest.utils;

import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.io.output.ByteArrayOutputStream;

import com.stratio.meta.common.data.Row;
import com.stratio.meta.common.result.QueryResult;
import com.stratio.meta.rest.models.JsonMetaResultSet;
import com.stratio.meta.rest.models.JsonQueryResult;
import com.stratio.meta.rest.models.JsonRow;

public class RestServerUtils {

    public static JsonQueryResult toJsonQueryResult(QueryResult qr) {
        JsonMetaResultSet jrs = new JsonMetaResultSet();
        List<JsonRow> jrows = new ArrayList<JsonRow>();
        if (qr.getResultSet().size() > 0) {
            Iterator<Row> rows = qr.getResultSet().iterator();
            while (rows.hasNext()) {
                Row r = rows.next();
                JsonRow jr = new JsonRow();
                jr.setCells(r.getCells());
                jrows.add(jr);
            }
            // TODO: Update this with new structures
            //jrs.setColumnMetadata(((MetaResultSet) qr.getResultSet()).getColumnMetadata());
            jrs.setRows(jrows);
        }
        JsonQueryResult jqr =
                new JsonQueryResult(jrs, qr.getResultPage(), qr.isCatalogChanged(), qr.getCurrentCatalog(),
                        qr.isLastResultSet());
        jqr.setQueryId(qr.getQueryId());
        jqr.setError(qr.hasError());
        return jqr;
    }

    public static String serializeObjectToString(Object object) throws Exception {

        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        GZIPOutputStream gzipOutputStream = new GZIPOutputStream(arrayOutputStream);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(gzipOutputStream);

        objectOutputStream.writeObject(object);

        objectOutputStream.flush();
        gzipOutputStream.close();
        arrayOutputStream.close();
        objectOutputStream.close();
        //TODO: Review api rest result
        //String objectString = new String(base64.encode(arrayOutputStream.toByteArray()));

        return null;
    }
}
