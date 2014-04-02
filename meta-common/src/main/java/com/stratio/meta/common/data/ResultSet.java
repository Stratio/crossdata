package com.stratio.meta.common.data;

import java.io.Serializable;

public abstract class ResultSet implements Serializable {

    public abstract Row next();

    public abstract boolean hasNext();

    public abstract void close();

}
