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

package utils;

public class BeanUtils {
    /*
    public static Map<String, String> recursiveDescribe(Object object) {
        Set cache = new HashSet();
        return recursiveDescribe(object, null, cache);
    }

    private static Map<String, String> recursiveDescribe(Object object, String prefix, Set cache) {
        if (object == null || cache.contains(object)) return Collections.EMPTY_MAP;
        cache.add(object);
        prefix = (prefix != null) ? prefix + "." : "";

        Map<String, String> beanMap = new TreeMap<String, String>();

        Map<String, Object> properties = getProperties(object);
        for (String property : properties.keySet()) {
            Object value = properties.get(property);
            try {
                if (value == null) {
                    //ignore nulls
                } else if (Collection.class.isAssignableFrom(value.getClass())) {
                    beanMap.putAll(convertAll((Collection) value, prefix + property, cache));
                } else if (value.getClass().isArray()) {
                    beanMap.putAll(convertAll(Arrays.asList((Object[]) value), prefix + property, cache));
                } else if (Map.class.isAssignableFrom(value.getClass())) {
                    beanMap.putAll(convertMap((Map) value, prefix + property, cache));
                } else {
                    beanMap.putAll(convertObject(value, prefix + property, cache));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return beanMap;
    }

    private static Map<String, Object> getProperties(Object object) {
        Map<String, Object> propertyMap = getFields(object);
        //getters take precedence in case of any name collisions
        propertyMap.putAll(getGetterMethods(object));
        return propertyMap;
    }

    private static Map<String, Object> getGetterMethods(Object object) {
        Map<String, Object> result = new HashMap<String, Object>();
        BeanInfo info;
        try {
            info = Introspector.getBeanInfo(object.getClass());
            for (PropertyDescriptor pd : info.getPropertyDescriptors()) {
                Method reader = pd.getReadMethod();
                if (reader != null) {
                    String name = pd.getName();
                    if (!"class".equals(name)) {
                        try {
                            Object value = reader.invoke(object);
                            result.put(name, value);
                        } catch (Exception e) {
                            //you can choose to do something here
                        }
                    }
                }
            }
        } catch (IntrospectionException e) {
            //you can choose to do something here
        } finally {
            return result;
        }

    }

    private static Map<String, Object> getFields(Object object) {
        return getFields(object, object.getClass());
    }

    private static Map<String, Object> getFields(Object object, Class<?> classType) {
        Map<String, Object> result = new HashMap<String, Object>();

        Class superClass = classType.getSuperclass();
        if (superClass != null) result.putAll(getFields(object, superClass));

        //get public fields only
        Field[] fields = classType.getFields();
        for (Field field : fields) {
            try {
                result.put(field.getName(), field.get(object));
            } catch (IllegalAccessException e) {
                //you can choose to do something here
            }
        }
        return result;
    }

    private static Map<String, String> convertAll(Collection<Object> values, String key, Set cache) {
        Map<String, String> valuesMap = new HashMap<String, String>();
        Object[] valArray = values.toArray();
        for (int i = 0; i < valArray.length; i++) {
            Object value = valArray[i];
            if (value != null) valuesMap.putAll(convertObject(value, key + "[" + i + "]", cache));
        }
        return valuesMap;
    }

    private static Map<String, String> convertMap(Map<Object, Object> values, String key, Set cache) {
        Map<String, String> valuesMap = new HashMap<String, String>();
        for (Object thisKey : values.keySet()) {
            Object value = values.get(thisKey);
            if (value != null) valuesMap.putAll(convertObject(value, key + "[" + thisKey + "]", cache));
        }
        return valuesMap;
    }

    private static ConvertUtilsBean converter = BeanUtilsBean.getInstance().getConvertUtils();

    private static Map<String, String> convertObject(Object value, String key, Set cache) {
        //if this type has a registered converted, then get the string and return
        if (converter.lookup(value.getClass()) != null) {
            String stringValue = converter.convert(value);
            Map<String, String> valueMap = new HashMap<String, String>();
            valueMap.put(key, stringValue);
            return valueMap;
        } else {
            //otherwise, treat it as a nested bean that needs to be described itself
            return recursiveDescribe(value, key, cache);
        }
    }
    */
}
