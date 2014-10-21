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

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;

import com.stratio.crossdata.common.statements.structures.selectors.BooleanSelector;
import com.stratio.crossdata.common.statements.structures.selectors.FloatingPointSelector;
import com.stratio.crossdata.common.statements.structures.selectors.IntegerSelector;
import com.stratio.crossdata.common.statements.structures.selectors.Selector;
import com.stratio.crossdata.common.statements.structures.selectors.StringSelector;

/**
 * Utility class for String transformation operations.
 */
public class StringUtils {

    /**
     * Class logger.
     */
    private static final Logger LOG = Logger.getLogger(StringUtils.class);

    /**
     * Create a string from a list of objects using a separator between objects.
     *
     * @param ids       The list of objects.
     * @param separator The separator.
     * @return A String.
     */
    public static String stringList(List<?> ids, String separator) {
        StringBuilder sb = new StringBuilder();
        for (Object value : ids) {
            sb.append(value.toString()).append(separator);
        }
        if (sb.length() > separator.length()) {
            return sb.substring(0, sb.length() - separator.length());
        } else {
            return "";
        }
    }

    public static Map<Selector, Selector> convertJsonToOptions(String json) {
        if ((json == null) || (json.isEmpty())) {
            return null;
        }
        Map<Selector, Selector> options = new LinkedHashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        JsonFactory factory = mapper.getJsonFactory();
        JsonParser jp;
        try {
            jp = factory.createJsonParser(json);
            JsonNode root = mapper.readTree(jp);
            Iterator<Map.Entry<String, JsonNode>> iter = root.getFields();
            while (iter.hasNext()) {
                Map.Entry<String, JsonNode> entry = iter.next();
                Selector selector = convertJsonNodeToMetaParserType(entry.getValue());
                options.put(new StringSelector(entry.getKey()), selector);
            }
        } catch (IOException e) {
            LOG.error(e);
        }
        return options;
    }

    public static Map<String, Object> convertJsonToMap(String json) {
        Map<String, Object> options = new LinkedHashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        JsonFactory factory = mapper.getJsonFactory();
        JsonParser jp;
        try {
            jp = factory.createJsonParser(json);
            JsonNode root = mapper.readTree(jp);
            Iterator<Map.Entry<String, JsonNode>> iter = root.getFields();
            while (iter.hasNext()) {
                Map.Entry<String, JsonNode> entry = iter.next();
                Object obj = convertJsonNodeToJavaType(entry.getValue());
                options.put(entry.getKey(), obj);
            }
        } catch (IOException e) {
            LOG.error(e);
        }
        return options;
    }

    private static Selector convertJsonNodeToMetaParserType(JsonNode jsonNode) {
        Selector selector;
        if (jsonNode.isBigDecimal() || jsonNode.isDouble()) {
            selector = new FloatingPointSelector(jsonNode.getDoubleValue());
        } else if (jsonNode.isBoolean()) {
            selector = new BooleanSelector(jsonNode.getBooleanValue());
        } else if (jsonNode.isInt() || jsonNode.isBigInteger() || jsonNode.isLong()) {
            selector = new IntegerSelector(jsonNode.getIntValue());
        } else {
            selector = new StringSelector(jsonNode.getTextValue());
        }
        return selector;
    }

    private static Object convertJsonNodeToJavaType(JsonNode jsonNode) {
        Object obj;
        if (jsonNode.isBigDecimal() || jsonNode.isDouble()) {
            obj = jsonNode.getDoubleValue();
        } else if (jsonNode.isBoolean()) {
            obj = jsonNode.getBooleanValue();
        } else if (jsonNode.isInt() || jsonNode.isBigInteger() || jsonNode.isLong()) {
            obj = jsonNode.getIntValue();
        } else {
            obj = jsonNode.getTextValue();
        }
        return obj;
    }

    public static String getStringFromOptions(Map<Selector, Selector> options) {
        StringBuilder sb = new StringBuilder("{");
        Iterator<Map.Entry<Selector, Selector>> entryIt = options.entrySet().iterator();
        Map.Entry<Selector, Selector> e;
        while (entryIt.hasNext()) {
            e = entryIt.next();
            sb.append(e.getKey()).append(": ").append(e.getValue());
            if (entryIt.hasNext()) {
                sb.append(", ");
            }
        }
        sb.append("}");
        return sb.toString();
    }

    public static String getAkkaActorRefUri(Object object){
        if(object != null) {
            return object.toString().replace("Actor[", "").replace("]", "").split("\\$")[0].split("#")[0];
        }
        return null;
    }

}
