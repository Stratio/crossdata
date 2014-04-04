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

package com.stratio.meta.core.metadata;

import com.datastax.driver.core.ColumnMetadata;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.stratio.meta.core.structures.IndexType;
import com.stratio.meta.core.utils.MetaQuery;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Class that processes the options found in a Lucene index and
 * builds a map with the set of columns mapped by the specified
 * Lucene index. Notice that a Lucene index may cover several
 * columns.
 */
public class LuceneIndexHelper {

    /**
     * Class logger.
     */
    private static final Logger _logger = Logger.getLogger(LuceneIndexHelper.class.getName());

    /**
     * Session used to retrieve custom Lucene index metadata.
     */
    private final Session _session;

    /**
     * Class constructor.
     * @param session The session used to retrieve Lucene index metadata.
     */
    public LuceneIndexHelper(Session session){
        _session = session;
    }

    /**
     * Get the Lucene index associated with a column.
     * @param column The column with the index.
     * @param indexName The name of the index.
     * @return A {@link com.stratio.meta.core.metadata.CustomIndexMetadata} or null if the options cannot be found.
     */
    public CustomIndexMetadata getLuceneIndex(ColumnMetadata column, String indexName){
        CustomIndexMetadata result = null;
        StringBuilder sb = new StringBuilder("SELECT index_options FROM system.schema_columns WHERE keyspace_name='");
        sb.append(column.getTable().getKeyspace().getName());
        sb.append("' AND columnfamily_name='");
        sb.append(column.getTable().getName());
        sb.append("' AND column_name='");
        sb.append(column.getName());
        sb.append("'");
        MetaQuery mq = new MetaQuery();
        mq.setQuery(sb.toString());
        ResultSet indexOptions = _session.execute(sb.toString());
        Row options = indexOptions.one();
        if(options != null){
            result = processLuceneMapping(column, indexName, options.getString("index_options"));
        }
        return result;
    }

    /**
     * Process the mapping of columns in the index by analyzing the JSON options.
     * @param metadata The index column.
     * @param indexName The name of the index.
     * @param options The index options.
     * @return A {@link com.stratio.meta.core.metadata.CustomIndexMetadata} or null if the options cannot be found.
     */
    public CustomIndexMetadata processLuceneMapping(ColumnMetadata metadata, String indexName, String options){
        CustomIndexMetadata result = null;
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        JsonFactory factory = mapper.getJsonFactory();
        JsonParser jp = null;
        try {
            jp = factory.createJsonParser(options);
            JsonNode root = mapper.readTree(jp);
            JsonNode schema = root.get("schema");
            if(schema != null){
                JsonNode node = null;
                String schemaString = schema.toString()
                        .substring(1, schema.toString().length() - 1)
                        .replace("\\", "");
                JsonNode schemaRoot = mapper.readTree(factory.createJsonParser(schemaString));
                if(schemaRoot != null && schemaRoot.get("fields") != null){
                    JsonNode fields = schemaRoot.get("fields");
                    List<String> mappedColumns = new ArrayList<>();
                    Iterator<String> it = fields.getFieldNames();
                    while(it.hasNext()){
                        mappedColumns.add(it.next());
                    }

                    result = new CustomIndexMetadata(metadata, indexName, IndexType.LUCENE, mappedColumns);
                }else{
                    _logger.error("Fields not found in Lucene index with JSON: " + root.toString());
                }
            }else{
                _logger.error("Schema not found in Lucene index with JSON: " + root.toString());
            }
        } catch (IOException e) {
            _logger.error("Cannot process Lucene index options", e);
        } finally {
            try {
                if (jp != null) { jp.close(); }
            } catch (IOException e) { _logger.error("Cannot close JSON parser"); }
        }
        return result;
    }

}
