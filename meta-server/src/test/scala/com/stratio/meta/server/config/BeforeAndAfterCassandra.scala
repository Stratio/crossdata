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

package com.stratio.meta.server.config

import java.io.File
import scala.sys.process._
import org.scalatest.{Suite, BeforeAndAfterAll}

trait BeforeAndAfterCassandra extends BeforeAndAfterAll {
  this:Suite =>
    def beforeCassandraStart(): Unit = {

    }

    override def beforeAll(): Unit = {
      beforeCassandraStart()
      val file = new File(getClass.getResource("/com/stratio/meta/test/test.sh").getFile)
      file.setExecutable(true)
      val output = file.getAbsolutePath.!!
      println(output)
      afterCassandraStart()
    }

    def afterCassandraStart(): Unit = {

    }

    def beforeCassandraFinish(): Unit = {

    }
    override def afterAll(): Unit = {
      beforeCassandraFinish()
      val file = new File(getClass.getResource("/com/stratio/meta/test/close.sh").getFile)
      file.setExecutable(true)
      val output = file.getAbsolutePath.!!
      println(output)
      afterCassandraFinish()
    }

    def afterCassandraFinish(): Unit = {

    }

}
