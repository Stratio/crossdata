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
import java.io.Serializable;
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

import com.stratio.crossdata.common.data.TableName;
import com.stratio.crossdata.common.metadata.ColumnType;
import com.stratio.crossdata.common.metadata.DataType;
import com.stratio.crossdata.common.statements.structures.BooleanSelector;
import com.stratio.crossdata.common.statements.structures.FloatingPointSelector;
import com.stratio.crossdata.common.statements.structures.ISqlExpression;
import com.stratio.crossdata.common.statements.structures.IntegerSelector;
import com.stratio.crossdata.common.statements.structures.Selector;
import com.stratio.crossdata.common.statements.structures.StringSelector;

import difflib.DiffUtils;
import difflib.Patch;
import difflib.PatchFailedException;

/**
 * Utility class for String transformation operations.
 */
public final class StringUtils implements Serializable {

    private static final long serialVersionUID = 4917945078917981844L;

    /**
     * Private constructor as StringUtils is a utility class.
     */
    private StringUtils() {
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
        for (Object value: ids) {
            sb.append(value.toString()).append(separator);
        }
        if (sb.length() > separator.length()) {
            return sb.substring(0, sb.length() - separator.length());
        } else {
            return "";
        }
    }

    /**
     * Transform a JSON into a map of selectors.
     *
     * @param tableName The associated {@link com.stratio.crossdata.common.data.TableName}.
     * @param json      The JSON string.
     * @return A map of {@link com.stratio.crossdata.common.statements.structures.Selector} matching the JSON document.
     */
    public static Map<Selector, Selector> convertJsonToOptions(TableName tableName, String json) {
        if ((json == null) || (json.isEmpty())) {
            return new HashMap<>();
        }
        String jsonModified;
        jsonModified = json.replaceAll("<[^>]*>\\.", "");
        Map<Selector, Selector> options = new LinkedHashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        JsonFactory factory = mapper.getJsonFactory();
        JsonParser jp;
        try {
            jp = factory.createJsonParser(jsonModified);
            JsonNode root = mapper.readTree(jp);
            Iterator<Map.Entry<String, JsonNode>> iter = root.getFields();
            while (iter.hasNext()) {
                Map.Entry<String, JsonNode> entry = iter.next();
                Selector selector = convertJsonNodeToCrossdataParserType(tableName, entry.getValue());
                options.put(new StringSelector(tableName, entry.getKey()), selector);
            }
        } catch (IOException e) {
            LOG.error(e);
        }
        return options;
    }

    /**
     * Transform a JSON node into a equivalent Crossdata selector.
     *
     * @param tableName The associated {@link com.stratio.crossdata.common.data.TableName}.
     * @param jsonNode  The {@link org.codehaus.jackson.JsonNode}.
     * @return A {@link com.stratio.crossdata.common.statements.structures.Selector}.
     */
    private static Selector convertJsonNodeToCrossdataParserType(TableName tableName, JsonNode jsonNode) {
        Selector selector;
        if (jsonNode.isBigDecimal() || jsonNode.isDouble()) {
            selector = new FloatingPointSelector(tableName, jsonNode.getDoubleValue());
        } else if (jsonNode.isBoolean()) {
            selector = new BooleanSelector(tableName, jsonNode.getBooleanValue());
        } else if (jsonNode.isInt() || jsonNode.isBigInteger() || jsonNode.isLong()) {
            selector = new IntegerSelector(tableName, jsonNode.getIntValue());
        } else {
            selector = new StringSelector(tableName, jsonNode.getTextValue());
        }
        return selector;
    }

    /**
     * Get the actor reference of a given object.
     * @param object The object.
     * @return The actor ref.
     */
    public static String getAkkaActorRefUri(Object object) {
        return getAkkaActorRefUri(object, true);
    }

    /**
     * Get the string representation of a AKKA actor reference URI.
     *
     * @param object The object with the Actor ref.
     * @param resultForActorSelection The result for an actor.
     * @return A string with the URI
     */
    public static String getAkkaActorRefUri(Object object, boolean resultForActorSelection) {
        if (object != null) {
            // context.actorSelection("akka.tcp://app@otherhost:1234/user/serviceB")
            String result = object.toString().replace("Actor[", "").replace("]", "").split("\\$")[0].split("#")[0];
            if(resultForActorSelection){
                if (result.contains("akka.tcp")) {
                    result = result.substring(result.lastIndexOf("akka.tcp"));
                }
                if(result.contains("%3A%2F%2F")) {
                    result = result.replace("%3A%2F%2F", "://");
                }
                if(result.contains("%40")) {
                    result = result.replace("%40", "@");
                }
                if(result.contains("%3A")){
                    result = result.replace("%3A", ":");
                }
                if(result.contains("%2F")) {
                    result = result.replace("%2F", "/");
                }
                if(result.contains("%")) {
                    result = result.substring(0, result.lastIndexOf('%'));
                }
            }
            return result;
        }
        return null;
    }

    /**
     * Convert the java types to Crossdata types.
     * @param javaType The java type
     * @return A Crossdata {@link ColumnType}
     */

    public static ColumnType convertJavaTypeToXdType(String javaType) {
        ColumnType ct = new ColumnType(DataType.NATIVE);
        if (javaType.equalsIgnoreCase("Long")) {
            ct = new ColumnType(DataType.BIGINT);
        } else if (javaType.equalsIgnoreCase("Boolean")) {
            ct = new ColumnType(DataType.BOOLEAN);
        } else if (javaType.equalsIgnoreCase("Double")) {
            ct = new ColumnType(DataType.DOUBLE);
        } else if (javaType.equalsIgnoreCase("Float")) {
            ct = new ColumnType(DataType.DOUBLE);
        } else if (javaType.equalsIgnoreCase("Integer")) {
            ct = new ColumnType(DataType.INT);
        } else if (javaType.equalsIgnoreCase("String")) {
            ct = new ColumnType(DataType.TEXT);
        } else if (javaType.equalsIgnoreCase("Set")) {
            ct = new ColumnType(DataType.SET);
        } else if (javaType.equalsIgnoreCase("List")) {
            ct = new ColumnType(DataType.LIST);
        } else if (javaType.equalsIgnoreCase("Map")) {
            ct = new ColumnType(DataType.MAP);
        }
        if(ct.getDataType() == DataType.NATIVE){
            ct.setDbType(javaType);
            ct.setODBCType(javaType);
        }
        return ct;
    }

    /**
     * Return a patch of the diff between two objects using their serialized string representation.
     *
     * @param oa The first object.
     * @param ob The second object.
     * @return A {@link difflib.Patch}.
     */
    public static difflib.Patch objectDiff(Object oa, Object ob){
        String[] a = serializeObject2String(oa).split("\n");
        String[] b = serializeObject2String(ob).split("\n");
        ArrayList<String> lista = new ArrayList<String>(Arrays.asList(a));
        ArrayList<String> listb = new ArrayList<String>(Arrays.asList(b));
        return DiffUtils.diff(lista, listb);
    }

    /**
     * Return a String with the result json adding to the object the patch.
     *
     * @param a The object to patch.
     * @param diff The patch.
     * @return String.
     * @throws PatchFailedException .
     */
    public static String patchObject(Object a, Patch diff) throws PatchFailedException {
        //apply patch to a
        String[] lista = StringUtils.serializeObject2String(a).split("\n");
        List<String> partialresult = (List<String>) diff.applyTo(Arrays.asList(lista));
        StringBuffer jsonresult=new StringBuffer();
        for(String res:partialresult){
            jsonresult.append(res);
        }
        return jsonresult.toString();
    }

    /**
     * Deserialize an object.
     *
     * @param serializedObject The serialize object.
     * @param arrayListClass
     * @return The deserialized object.
     */
    public static Object deserializeObjectFromString(String serializedObject, Class arrayListClass) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(mapper.readTree(serializedObject), List.class);
        } catch (IOException e) {
            LOG.info(e.getMessage(),e);
        }
        return null;
    }

    /**
     * Serialize a Object to string.
     * @param obj The object to serialize.
     * @return A String with the object serialized.
     */
    public static String serializeObject2String(Object obj) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationConfig.Feature.INDENT_OUTPUT);
        mapper.enable(SerializationConfig.Feature.SORT_PROPERTIES_ALPHABETICALLY);
        String serialized = null;
        try {
            serialized = mapper.writeValueAsString(obj);
        } catch (IOException e) {
            LOG.info(e.getMessage(),e);
        }
        return serialized;
    }

    /**
     * Transfor a Crossdata type into a column type.
     *
     * @param xdType The crossdata type.
     * @return A {@link com.stratio.crossdata.common.metadata.ColumnType}.
     */
    public static ColumnType convertXdTypeToColumnType(String xdType) {
        ColumnType ct = new ColumnType(DataType.NATIVE);
        String stringType = xdType.replace("Tuple", "").replace("[", "").replace("]", "").trim();
        if (stringType.equalsIgnoreCase("BigInt")) {
            ct = new ColumnType(DataType.BIGINT);
        } else if (stringType.equalsIgnoreCase("Bool") || stringType.equalsIgnoreCase("Boolean")) {
            ct = new ColumnType(DataType.BOOLEAN);
        } else if (stringType.equalsIgnoreCase("Double")) {
            ct = new ColumnType(DataType.DOUBLE);
        } else if (stringType.equalsIgnoreCase("Float")) {
            ct = new ColumnType(DataType.FLOAT);
        } else if (stringType.equalsIgnoreCase("Int") || stringType.equalsIgnoreCase("Integer")) {
            ct = new ColumnType(DataType.INT);
        } else if (stringType.equalsIgnoreCase("Text")) {
            ct = new ColumnType(DataType.TEXT);
        } else if (stringType.equalsIgnoreCase("Varchar")) {
            ct = new ColumnType(DataType.VARCHAR);
        } else if (stringType.equalsIgnoreCase("Set")) {
            ct = new ColumnType(DataType.SET);
        } else if (stringType.equalsIgnoreCase("List")) {
            ct = new ColumnType(DataType.LIST);
        } else if (stringType.equalsIgnoreCase("Map")) {
            ct = new ColumnType(DataType.MAP);
        }
        return ct;
    }

    /**
     * Return the type of the crossdata function signature.
     * @param signature The signature of the function.
     * @return A String with the type of signature.
     */
    public static String getReturningTypeFromSignature(String signature) {
        return signature.substring(signature.indexOf(':')+1)
                .replace("Tuple[", "")
                .replace("]", "")
                .trim();
    }

    /**
     * Transform a Json to a map of key as string and its value as an object.
     * @param json The JSON string.
     * @return A map of String and Object with the JSON parsed.
     */
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

    /**
     * Extrat the identification of the Host from a akka URL
     * @param ar a akka URL
     * @return the Host Address
     */
    public static String extractHost(String ar) {
        String host = ar;
        if(ar.matches("akka\\.tcp://(.+)@(.+):(.+)")){
            host = ar.substring(ar.indexOf("@")+1, ar.indexOf(":", ar.indexOf(":")+1));
        }
        return host;
    }
}
