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

package com.stratio.meta2.core.structures;

import com.stratio.meta.common.utils.StringUtils;

import java.util.List;

public class PropertyClusteringOrder extends Property {

  private List<Ordering> order;

  public PropertyClusteringOrder() {
    super(TYPE_CLUSTERING_ORDER);
  }

  public PropertyClusteringOrder(List<Ordering> order) {
    super(TYPE_CLUSTERING_ORDER);
    this.order = order;
  }

  public List<Ordering> getOrder() {
    return order;
  }

  public void setOrder(List<Ordering> order) {
    this.order = order;
  }

  @Override
  public String toString() {
    return "CLUSTERING ORDER BY ("+ StringUtils.stringList(order, ", ")+")";
  }

}
