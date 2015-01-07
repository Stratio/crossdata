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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

import com.stratio.crossdata.common.metadata.ColumnType;
import com.stratio.crossdata.common.statements.structures.BooleanSelector;
import com.stratio.crossdata.common.statements.structures.FloatingPointSelector;
import com.stratio.crossdata.common.statements.structures.IntegerSelector;
import com.stratio.crossdata.common.statements.structures.Selector;
import com.stratio.crossdata.common.statements.structures.StringSelector;

import difflib.DiffUtils;
import difflib.Patch;
import difflib.PatchFailedException;

/**
 * Utility class for String transformation operations.
 */
public final class StringUtils {

    private StringUtils(){

    }

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
            return new HashMap<>();
        }
        json = json.replaceAll("<[^>]*>\\.", "");
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
                Selector selector = convertJsonNodeToCrossdataParserType(entry.getValue());
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

    private static Selector convertJsonNodeToCrossdataParserType(JsonNode jsonNode) {
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

    public static String getAkkaActorRefUri(Object object){
        if(object != null) {
            return object.toString().replace("Actor[", "").replace("]", "").split("\\$")[0].split("#")[0];
        }
        return null;
    }

    public static ColumnType convertJavaTypeToXdType(String javaType) {
        ColumnType ct = ColumnType.NATIVE;
        if(javaType.equalsIgnoreCase("Long")){
            ct = ColumnType.BIGINT;
        } else if(javaType.equalsIgnoreCase("Boolean")){
            ct = ColumnType.BOOLEAN;
        } else if(javaType.equalsIgnoreCase("Double")){
            ct = ColumnType.DOUBLE;
        } else if(javaType.equalsIgnoreCase("Float")){
            ct = ColumnType.DOUBLE;
        } else if(javaType.equalsIgnoreCase("Integer")){
            ct = ColumnType.INT;
        } else if(javaType.equalsIgnoreCase("String")){
            ct = ColumnType.TEXT;
        } else if(javaType.equalsIgnoreCase("Set")){
            ct = ColumnType.SET;
        } else if(javaType.equalsIgnoreCase("List")){
            ct = ColumnType.LIST;
        } else if(javaType.equalsIgnoreCase("MAP")){
            ct = ColumnType.MAP;
        }
        return ct;
    }

    public static difflib.Patch objectDiff(Object oa, Object ob){
        String[] a = serializeObject2String(oa).split("\n");
        String[] b = serializeObject2String(ob).split("\n");
        ArrayList<String> lista = new ArrayList<String>(Arrays.asList(a));
        ArrayList<String> listb = new ArrayList<String>(Arrays.asList(b));
        return DiffUtils.diff(lista, listb);
    }
    
    public static String patchObject(ArrayList<String> a, Patch diff) throws PatchFailedException {
        String[] lista = StringUtils.serializeObject2String(a).split("\n"); //apply patch to a
        List<String> partialresult = (List<String>) diff.applyTo(Arrays.asList(lista));
        String jsonresult="";
        for(String res:partialresult){ jsonresult+=res; }
        return jsonresult;
    }
    
    public static Object deserializeObjectFromString(String serializedObject, Class objectsClass) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(mapper.readTree(serializedObject), ArrayList.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String serializeObject2String(Object obj) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable( SerializationConfig.Feature.INDENT_OUTPUT );
        mapper.enable( SerializationConfig.Feature.SORT_PROPERTIES_ALPHABETICALLY);
        String serialized = null;
        try {
            serialized = mapper.writeValueAsString(obj);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return serialized;
    }
}
