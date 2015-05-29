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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ConnectorType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ConnectorType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ConnectorName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="DataStores" type="{}DataStoreRefsType"/>
 *         &lt;element name="Version" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Native" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="RequiredProperties" type="{}PropertiesType" minOccurs="0"/>
 *         &lt;element name="OptionalProperties" type="{}PropertiesType" minOccurs="0"/>
 *         &lt;element name="SupportedOperations" type="{}SupportedOperationsType" minOccurs="0"/>
 *         &lt;element name="Functions" type="{}ConnectorFunctionsType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ConnectorType", propOrder = {
                "connectorName",
                "dataStores",
                "version",
                "_native",
                "requiredProperties",
                "optionalProperties",
                "supportedOperations",
                "functions"
})
public class ConnectorType extends CrossdataManifest {

    private static final long serialVersionUID = -2878873028930698316L;
    @XmlElement(name = "ConnectorName", required = true)
    protected String connectorName;
    @XmlElement(name = "DataStores", required = true)
    protected DataStoreRefsType dataStores;
    @XmlElement(name = "Version", required = true)
    protected String version;
    @XmlElement(name = "Native", defaultValue = "false")
    protected Boolean _native;
    @XmlElement(name = "RequiredProperties")
    protected PropertiesType requiredProperties;
    @XmlElement(name = "OptionalProperties")
    protected PropertiesType optionalProperties;
    @XmlElement(name = "SupportedOperations")
    protected SupportedOperationsType supportedOperations;
    @XmlElement(name = "Functions")
    protected ConnectorFunctionsType functions;

    /**
     * Constructor class.
     */
    public ConnectorType() {
        super(TYPE_CONNECTOR);
    }

    /**
     * Gets the value of the connectorName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConnectorName() {
        return connectorName;
    }

    /**
     * Sets the value of the connectorName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConnectorName(String value) {
        this.connectorName = value;
    }

    /**
     * Gets the value of the dataStores property.
     * 
     * @return
     *     possible object is
     *     {@link DataStoreRefsType }
     *     
     */
    public DataStoreRefsType getDataStores() {
        return dataStores;
    }

    /**
     * Sets the value of the dataStores property.
     * 
     * @param value
     *     allowed object is
     *     {@link DataStoreRefsType }
     *     
     */
    public void setDataStores(DataStoreRefsType value) {
        this.dataStores = value;
    }

    /**
     * Gets the value of the version property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersion() {
        return version;
    }

    /**
     * Sets the value of the version property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersion(String value) {
        this.version = value;
    }

    /**
     * Gets the value of the native property.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isNative() {
        return _native;
    }

    /**
     * Sets the value of the native property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setNative(Boolean value) {
        this._native = value;
    }

    /**
     * Gets the value of the requiredProperties property.
     * 
     * @return
     *     possible object is
     *     {@link PropertiesType }
     *     
     */
    public PropertiesType getRequiredProperties() {
        return requiredProperties;
    }

    /**
     * Sets the value of the requiredProperties property.
     * 
     * @param value
     *     allowed object is
     *     {@link PropertiesType }
     *     
     */
    public void setRequiredProperties(PropertiesType value) {
        this.requiredProperties = value;
    }

    /**
     * Gets the value of the optionalProperties property.
     * 
     * @return
     *     possible object is
     *     {@link PropertiesType }
     *     
     */
    public PropertiesType getOptionalProperties() {
        return optionalProperties;
    }

    /**
     * Sets the value of the optionalProperties property.
     * 
     * @param value
     *     allowed object is
     *     {@link PropertiesType }
     *     
     */
    public void setOptionalProperties(PropertiesType value) {
        this.optionalProperties = value;
    }

    /**
     * Gets the value of the supportedOperations property.
     * 
     * @return
     *     possible object is
     *     {@link SupportedOperationsType }
     *     
     */
    public SupportedOperationsType getSupportedOperations() {
        return supportedOperations;
    }

    /**
     * Sets the value of the supportedOperations property.
     * 
     * @param value
     *     allowed object is
     *     {@link SupportedOperationsType }
     *     
     */
    public void setSupportedOperations(SupportedOperationsType value) {
        this.supportedOperations = value;
    }

    /**
     * Gets the value of the functions property.
     * 
     * @return
     *     possible object is
     *     {@link ConnectorFunctionsType }
     *     
     */
    public ConnectorFunctionsType getFunctions() {
        return functions;
    }

    /**
     * Sets the value of the functions property.
     * 
     * @param value
     *     allowed object is
     *     {@link ConnectorFunctionsType }
     *     
     */
    public void setFunctions(ConnectorFunctionsType value) {
        this.functions = value;
    }

}
