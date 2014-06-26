/*
 * Stratio Meta
 *
 * Copyright (c) 2014, Stratio, All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 */

package com.stratio.meta.driver.config

import akka.util.Timeout


class BasicDriverConfig(val driverSection:DriverSectionConfig,val serverSection:ServerSectionConfig)
class DriverSectionConfig(val retryTimes:Int, val retryDuration:Timeout)
class ServerSectionConfig(val clusterName:String, val clusterActor:String, val clusterHosts:Array[String])



