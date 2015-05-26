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

package com.stratio.crossdata.utils

import com.stratio.crossdata.common.manifest._
import java.io.{StringReader, FileInputStream, InputStream}
import javax.xml.validation.{Schema, SchemaFactory}
import javax.xml.XMLConstants
import javax.xml.bind.{JAXBElement, Unmarshaller, JAXBContext}
import com.stratio.crossdata.common.exceptions.ManifestException


object ManifestUtils {

  private final val DATASTORE_SCHEMA_PATH: String = "/com/stratio/crossdata/connector/DataStoreDefinition.xsd"
  private final val CONNECTOR_SCHEMA_PATH: String = "/com/stratio/crossdata/connector/ConnectorDefinition.xsd"

  /**
   * Parse an XML document into a {@link com.stratio.crossdata.common.manifest.CrossdataManifest}.
   * @param manifestType The type of manifest.
   * @param path The XML path.
   * @return A { @link com.stratio.crossdata.common.manifest.CrossdataManifest}.
   * @throws ManifestException If the XML is not valid.
   * @throws FileNotFoundException If the XML file does not exist.
   */
  @throws(classOf[ManifestException])
  def parseFromXmlToManifest(manifestType: Int, path: String): CrossdataManifest = {
    if (manifestType == CrossdataManifest.TYPE_DATASTORE) {
      return parseFromXmlToDataStoreManifest(new FileInputStream(path))
    }
    else {
      return parseFromXmlToConnectorManifest(new FileInputStream(path))
    }
  }

  /**
   * Parse an XML document into a {@link com.stratio.crossdata.common.manifest.CrossdataManifest}.
   * @param manifestType The type of manifest.
   * @param path The { @link java.io.InputStream} to retrieve the XML.
   * @return A { @link com.stratio.crossdata.common.manifest.CrossdataManifest}.
   * @throws ManifestException If the XML is not valid.
   */
  @throws(classOf[ManifestException])
  def parseFromXmlToManifest(manifestType: Int, path: InputStream): CrossdataManifest = {
    if (manifestType == CrossdataManifest.TYPE_DATASTORE) {
      return parseFromXmlToDataStoreManifest(path)
    }
    else {
      return parseFromXmlToConnectorManifest(path)
    }
  }


  @throws(classOf[ManifestException])
  private def parseFromXmlToDataStoreManifest(manifest: String): DataStoreType = {
    try {
      val sf: SchemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI)
      val schema: Schema = sf.newSchema(getClass.getResource(DATASTORE_SCHEMA_PATH))
      val jaxbContext: JAXBContext = JAXBContext.newInstance(classOf[DataStoreFactory])
      val unmarshaller: Unmarshaller = jaxbContext.createUnmarshaller
      unmarshaller.setSchema(schema)

      val reader = new StringReader(manifest);

      val unmarshalledDataStore: JAXBElement[DataStoreType] = unmarshaller.unmarshal(reader)
        .asInstanceOf[JAXBElement[DataStoreType]]
      return unmarshalledDataStore.getValue
    }
    catch {
      case e: Any => {
        throw new ManifestException(e)
      }
    }
  }

  @throws(classOf[ManifestException])
  private def parseFromXmlToConnectorManifest(manifest: String): CrossdataManifest = {
    try {
      val sf: SchemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI)
      val schema: Schema = sf.newSchema(getClass.getResource(CONNECTOR_SCHEMA_PATH))
      val jaxbContext: JAXBContext = JAXBContext.newInstance(classOf[ConnectorFactory])
      val unmarshaller: Unmarshaller = jaxbContext.createUnmarshaller
      unmarshaller.setSchema(schema)
      val reader = new StringReader(manifest);
      val unmarshalledDataStore: JAXBElement[ConnectorType] = unmarshaller.unmarshal(reader)
        .asInstanceOf[JAXBElement[ConnectorType]]
      return unmarshalledDataStore.getValue
    }
    catch {
      case e: Any => {
        throw new ManifestException(e)
      }
    }
  }

  @throws(classOf[ManifestException])
  private def parseFromXmlToDataStoreManifest(path: InputStream): DataStoreType = {
    try {
      val sf: SchemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI)
      val schema: Schema = sf.newSchema(getClass.getResource(DATASTORE_SCHEMA_PATH))
      val jaxbContext: JAXBContext = JAXBContext.newInstance(classOf[DataStoreFactory])
      val unmarshaller: Unmarshaller = jaxbContext.createUnmarshaller
      unmarshaller.setSchema(schema)
      val unmarshalledDataStore: JAXBElement[DataStoreType] = unmarshaller.unmarshal(path).asInstanceOf[JAXBElement[DataStoreType]]
      return unmarshalledDataStore.getValue
    }
    catch {
      case e: Any => {
        throw new ManifestException(e)
      }
    }
  }

  @throws(classOf[ManifestException])
  private def parseFromXmlToConnectorManifest(path: InputStream): CrossdataManifest = {
    try {
      val sf: SchemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI)
      val schema: Schema = sf.newSchema(getClass.getResource(CONNECTOR_SCHEMA_PATH))
      val jaxbContext: JAXBContext = JAXBContext.newInstance(classOf[ConnectorFactory])
      val unmarshaller: Unmarshaller = jaxbContext.createUnmarshaller
      unmarshaller.setSchema(schema)
      val unmarshalledDataStore: JAXBElement[ConnectorType] = unmarshaller.unmarshal(path).asInstanceOf[JAXBElement[ConnectorType]]
      return unmarshalledDataStore.getValue
    }
    catch {
      case e: Any => {
        throw new ManifestException(e)
      }
    }
  }
}
