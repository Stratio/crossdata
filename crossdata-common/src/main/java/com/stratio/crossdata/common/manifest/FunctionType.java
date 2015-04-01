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
import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for FunctionType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="FunctionType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="FunctionName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Signature" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="FunctionType" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FunctionType", propOrder = {
    "functionName",
    "signature",
    "functionType",
    "description"
})
public class FunctionType implements Serializable {

    private static final long serialVersionUID = -3188035910520681360L;
    @XmlElement(name = "FunctionName", required = true)
    protected String functionName;
    @XmlElement(name = "Signature", required = true)
    protected String signature;
    @XmlElement(name = "FunctionType", required = true)
    protected String functionType;
    @XmlElement(name = "Description")
    protected String description;

    /**
     * Gets the value of the functionName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFunctionName() {
        return functionName;
    }

    /**
     * Sets the value of the functionName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFunctionName(String value) {
        this.functionName = value;
    }

    /**
     * Gets the value of the signature property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSignature() {
        return signature;
    }

    /**
     * Sets the value of the signature property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSignature(String value) {
        this.signature = value;
    }

    /**
     * Gets the value of the functionType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFunctionType() {
        return functionType;
    }

    /**
     * Sets the value of the functionType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFunctionType(String value) {
        this.functionType = value;
    }

    /**
     * Gets the value of the description property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setDescription(String value) {
        this.description = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        FunctionType that = (FunctionType) o;

        if (description != null ? !description.equals(that.description) : that.description != null) {
            return false;
        }
        if (functionName != null ? !functionName.equals(that.functionName) : that.functionName != null) {
            return false;
        }
        if (functionType != null ? !functionType.equals(that.functionType) : that.functionType != null) {
            return false;
        }
        if (signature != null ? !signature.equals(that.signature) : that.signature != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = functionName != null ? functionName.hashCode() : 0;
        result = 31 * result + (signature != null ? signature.hashCode() : 0);
        result = 31 * result + (functionType != null ? functionType.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }

    /**
     * Check the input signature compatibility given a registered input signature.
     * @param storedSignature The string with the registered input signature.
     * @param querySignature The string with the input signature inferred from the function selector.
     * @return A boolean with the check result.
     */
    public static boolean checkInputSignatureCompatibility(String storedSignature, String querySignature) {

        String [] typesInQuerySignature = tokenizeInputSignature(querySignature);
        String [] typesInStoreSignature = tokenizeInputSignature(storedSignature);
        boolean wrongQuerySignatureFound = false;

        int storeIndex = 0;
        int queryIndex = 0;

        while (!wrongQuerySignatureFound && storeIndex < typesInStoreSignature.length && queryIndex < typesInQuerySignature.length){

            String storeType = typesInStoreSignature[storeIndex];
            if(!storeType.endsWith("*")){

                if(!storeType.equals("Any")) {
                    if (storeType.equals("")){
                        wrongQuerySignatureFound = !(typesInQuerySignature[queryIndex].equals(storeType) || typesInQuerySignature[queryIndex].endsWith("*"));
                    } else {
                        wrongQuerySignatureFound = !(
                                typesInQuerySignature[queryIndex].equals(storeType)
                                || typesInQuerySignature[queryIndex].endsWith(storeType+"*")
                                || checkNumericTypes(typesInQuerySignature[queryIndex], storeType));
                    }
                }
            }else{
                if(!storeType.equals("Any*")) {
                    storeType = storeType.substring(0, storeType.length() - 1);

                    for (int i = queryIndex; i < typesInQuerySignature.length; i++) {
                        if (!(typesInQuerySignature[i].startsWith(storeType) || typesInQuerySignature[i].equals("")
                                        || typesInQuerySignature[i].startsWith("Any"))) {
                            wrongQuerySignatureFound = true;
                            break;
                        }
                    }
                }
            }
            queryIndex++;
            storeIndex++;
        }

        return !wrongQuerySignatureFound;
    }

    private static boolean checkNumericTypes(String t1, String t2) {
        Set<String> numericTypes = new HashSet<>();
        numericTypes.add("int");
        numericTypes.add("bigint");
        numericTypes.add("long");
        numericTypes.add("float");
        numericTypes.add("double");

        if(numericTypes.contains(t1.toLowerCase()) && numericTypes.contains(t2.toLowerCase())){
            return true;
        }
        return false;
    }

    private static String[] tokenizeInputSignature(String iSignature) {
        String typesInStoresSignature = iSignature.substring( iSignature.indexOf("Tuple[") + 6, iSignature.indexOf(']'));
        return typesInStoresSignature.split(",");
    }
}
