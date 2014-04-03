package com.stratio.meta.common.data;

import java.util.Iterator;

public abstract class ResultSet implements Iterable<Row> {

    public abstract Iterator iterator();

}
