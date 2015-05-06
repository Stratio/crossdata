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

package com.stratio.crossdata.common.manifest;

import java.io.Serializable;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the generated package. 
 * <p>An DataStoreFactory allows you to programatically
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class DataStoreFactory implements Serializable {

    private final static QName _DataStore_QNAME = new QName("", "DataStore");
    private static final long serialVersionUID = -1800454505250424666L;

    /**
     * Create a new DataStoreFactory that can be used to create new instances of schema derived classes for package:
     * generated.
     * 
     */
    public DataStoreFactory() {
    }

    /**
     * Create an instance of {@link DataStoreType }.
     * @return {@link com.stratio.crossdata.common.manifest.DataStoreFactory}.
     */
    public DataStoreType createDataStoreType() {
        return new DataStoreType();
    }

    /**
     * Create an instance of {@link PropertiesType }.
     * @return {@link com.stratio.crossdata.common.manifest.PropertiesType}
     */
    public PropertiesType createPropertiesType() {
        return new PropertiesType();
    }

    /**
     * Create an instance of {@link DataStoreFunctionsType }.
     * @return A {@link DataStoreFunctionsType }.
     */
    public DataStoreFunctionsType createDataStoreFunctionsType() {
        return new DataStoreFunctionsType();
    }

    /**
     * Create an instance of {@link PropertyType }.
     * @return A {@link com.stratio.crossdata.common.manifest.PropertyType}.
     */
    public PropertyType createPropertyType() {
        return new PropertyType();
    }

    /**
     * Create an instance of {@link BehaviorsType }.
     * @return A {@link com.stratio.crossdata.common.manifest.BehaviorsType}.
     */
    public BehaviorsType createBehaviorsType() {
        return new BehaviorsType();
    }

    /**
     * Create an instance of {@link FunctionType }.
     * @return A {@link com.stratio.crossdata.common.manifest.FunctionType}.
     */
    public FunctionType createFunctionType() {
        return new FunctionType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DataStoreType }{@code >}.
     * @return A {@link javax.xml.bind.JAXBElement}.
     */
    @XmlElementDecl(namespace = "", name = "DataStore")
    public JAXBElement<DataStoreType> createDataStore(DataStoreType value) {
        return new JAXBElement<>(_DataStore_QNAME, DataStoreType.class, null, value);
    }

}
