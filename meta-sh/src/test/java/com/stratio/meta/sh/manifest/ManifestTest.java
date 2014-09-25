/*
 * Licensed to STRATIO (C) under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  The STRATIO (C) licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.stratio.meta.sh.manifest;

import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import org.testng.annotations.Test;

import com.stratio.meta.common.exceptions.ManifestException;
import com.stratio.meta.sh.utils.ConsoleUtils;
import com.stratio.meta2.common.api.Manifest;
import com.stratio.meta2.common.api.ManifestHelper;

public class ManifestTest {

    @Test
    public void testDataStoreManifest() {

        Manifest manifest = null;
        try {
            manifest = ConsoleUtils.parseFromXmlToManifest(Manifest.TYPE_DATASTORE,
                    "meta-common/src/main/resources/com/stratio/meta/connector/DataStoreDefinition.xml");
        } catch (ManifestException e) {
            fail("Manifest validation failed", e);
        }

        String parsedManifest = ManifestHelper.manifestToString(manifest);

        StringBuilder sb = new StringBuilder("DATASTORE");
        sb.append(System.lineSeparator());

        // NAME
        sb.append("Name: ").append("string").append(System.lineSeparator());

        // VERSION
        sb.append("Version: ").append("string").append(System.lineSeparator());

        // REQUIRED PARAMETERS
        sb.append("Required parameters: ").append(System.lineSeparator());
        sb.append("\t").append("Cluster: ").append(System.lineSeparator());
        sb.append("\t").append("\t").append("Name: ").append("string").append(System.lineSeparator());
        sb.append("\t").append("\t").append("Hosts: ").append(System.lineSeparator());
        sb.append("\t").append("\t").append("\t").append("Host: ").append("string").append(System.lineSeparator());
        sb.append("\t").append("\t").append("\t").append("Port: ").append("string").append(System.lineSeparator());

        // OPTIONAL PROPERTIES
        sb.append("Optional properties: ").append(System.lineSeparator());
        sb.append("\t").append("Property").append(System.lineSeparator());
        sb.append("\t").append("\t").append("Name: ").append("string").append(System.lineSeparator());

        // ERROR MESSAGE
        StringBuilder sbError = new StringBuilder(System.lineSeparator());
        sbError.append("Expecting:  ").append(sb.toString()).append(System.lineSeparator());
        sbError.append("Instead of: ").append(parsedManifest);

        assertTrue(parsedManifest.equalsIgnoreCase(sb.toString()), sbError.toString());
    }

    @Test
    public void testConnectorManifest() {

        Manifest manifest = null;
        try {
            manifest = ConsoleUtils.parseFromXmlToManifest(Manifest.TYPE_CONNECTOR,
                    "meta-common/src/main/resources/com/stratio/meta/connector/ConnectorDefinition.xml");
        } catch (ManifestException e) {
            fail("Manifest validation failed", e);
        }

        String parsedManifest = ManifestHelper.manifestToString(manifest);

        StringBuilder sb = new StringBuilder("CONNECTOR");
        sb.append(System.lineSeparator());

        // CONNECTOR NAME
        sb.append("ConnectorName: ").append("string").append(System.lineSeparator());

        // DATA STORES NAME
        sb.append("DataStoresName: ").append(System.lineSeparator());
        sb.append("\t").append("Datastore: ").append("string").append(System.lineSeparator());

        // VERSION
        sb.append("Version: ").append("string").append(System.lineSeparator());

        // REQUIRED PROPERTIES
        sb.append("Required properties: ").append(System.lineSeparator());
        sb.append("\t").append("Property: ").append(System.lineSeparator());
        sb.append("\t").append("\t").append("Name: ").append("string").append(
                System.lineSeparator());

        // OPTIONAL PROPERTIES
        sb.append("Optional properties: ").append(System.lineSeparator());
        sb.append("\t").append("Property: ").append(System.lineSeparator());
        sb.append("\t").append("\t").append("Name: ").append("string").append(
                System.lineSeparator());

        // SUPPORTED OPERATIONS
        sb.append("Supported operations: ").append(System.lineSeparator());
        sb.append("\t").append("Operation: ").append("CREATE_CATALOG").append(System.lineSeparator());

        // ERROR MESSAGE
        StringBuilder sbError = new StringBuilder(System.lineSeparator());
        sbError.append("Expecting:  ").append(sb.toString()).append(System.lineSeparator());
        sbError.append("Instead of: ").append(parsedManifest);

        assertTrue(parsedManifest.equalsIgnoreCase(sb.toString()), sbError.toString());
    }

}
