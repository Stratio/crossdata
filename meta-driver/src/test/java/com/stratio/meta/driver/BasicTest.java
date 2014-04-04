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

package com.stratio.meta.driver;

import com.stratio.meta.common.result.Result;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static org.testng.Assert.*;


public class BasicTest {
    BasicDriver driver;
    @BeforeTest
    public void init(){
        //System.setProperty("meta-driver.user.config.filename",
        //        "/Users/aagea/Documents/Stratio/Desarrollo/develop/stratio-meta/meta-driver/src/test/resources" +
        //        "/driver-application.conf");
        driver=new BasicDriver();
    }

    @Test
    public void connectTest(){
        Result metaResult= driver.connect("TEST_USER");
        assertFalse(metaResult.hasError());
    }

}
