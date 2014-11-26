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

package com.stratio.crossdata.common.metadata;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.stratio.crossdata.common.data.ClusterName;
import com.stratio.crossdata.common.data.DataStoreName;
import com.stratio.crossdata.common.manifest.ManifestHelper;
import com.stratio.crossdata.common.manifest.PropertyType;

public class DataStoreMetadata implements IMetadata {
    private final DataStoreName name;
    private final String version;
    private final Set<PropertyType> requiredProperties;
    private final Set<PropertyType> othersProperties;
    private final Set<String> behaviors;
    private Map<ClusterName, ClusterAttachedMetadata> clusterAttachedRefs;

    public DataStoreMetadata(DataStoreName name, String version, Set<PropertyType> requiredProperties,
            Set<PropertyType> othersProperties, Set<String> behaviors) {
        this.name = name;
        this.version = version;
        this.requiredProperties = (requiredProperties==null)? new HashSet<PropertyType>(): requiredProperties;
        this.othersProperties = (othersProperties==null)? new HashSet<PropertyType>(): othersProperties;
        this.behaviors = (behaviors==null)? new HashSet<String>(): behaviors;
        this.clusterAttachedRefs = new HashMap<>();
    }

    public DataStoreMetadata(DataStoreName name, String version, List<PropertyType> requiredProperties,
            List<PropertyType> othersProperties, List<String> behaviors) {
        this.name = name;
        this.version = version;
        if(requiredProperties != null){
            this.requiredProperties = ManifestHelper.convertManifestPropertiesToMetadataProperties(requiredProperties);
        } else {
            this.requiredProperties = new HashSet<>();
        }
        if(othersProperties != null){
            this.othersProperties = ManifestHelper.convertManifestPropertiesToMetadataProperties(othersProperties);
        } else {
            this.othersProperties = new HashSet<>();
        }
        if(behaviors != null){
            this.behaviors = ManifestHelper.convertManifestBehaviorsToMetadataBehaviors(behaviors);
        } else {
            this.behaviors = new HashSet<>();
        }
        this.clusterAttachedRefs = new HashMap<>();
    }

    public DataStoreName getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public Set<PropertyType> getRequiredProperties() {
        return requiredProperties;
    }

    public Set<PropertyType> getOthersProperties() {
        return othersProperties;
    }

    public Map<ClusterName, ClusterAttachedMetadata> getClusterAttachedRefs() {
        return clusterAttachedRefs;
    }

    public Set<String> getBehaviors() {
        return behaviors;
    }

    public void setClusterAttachedRefs(
            Map<ClusterName, ClusterAttachedMetadata> clusterAttachedRefs) {
        this.clusterAttachedRefs = clusterAttachedRefs;
    }

    @Override public String toString() {
        return "DataStoreMetadata{" +
                "name=" + name +
                ", version='" + version + '\'' +
                ", requiredProperties=" + requiredProperties +
                ", othersProperties=" + othersProperties +
                ", behaviors=" + behaviors +
                ", clusterAttachedRefs=" + clusterAttachedRefs +
                '}';
    }
}
