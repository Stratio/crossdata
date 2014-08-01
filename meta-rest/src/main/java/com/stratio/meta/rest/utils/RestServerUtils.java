package com.stratio.meta.rest.utils;

import java.io.ObjectOutputStream;
import org.apache.commons.codec.binary.Base64;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.io.output.ByteArrayOutputStream;

import com.stratio.meta.common.data.MetaResultSet;
import com.stratio.meta.common.data.Row;
import com.stratio.meta.common.result.QueryResult;
import com.stratio.meta.rest.models.JsonMetaResultSet;
import com.stratio.meta.rest.models.JsonQueryResult;
import com.stratio.meta.rest.models.JsonRow;

public class RestServerUtils {
  private static Base64 base64 = new Base64();

  public static JsonQueryResult toJsonQueryResult(QueryResult qr) {

    List<JsonRow> jrows = new ArrayList<JsonRow>();
    List<Row> rows = ((MetaResultSet) qr.getResultSet()).getRows();
    for (Row r : rows) {
      JsonRow jr = new JsonRow();
      jr.setCells(r.getCells());
      jrows.add(jr);
    }

    JsonMetaResultSet jrs = new JsonMetaResultSet();
    jrs.setColumnMetadata(((MetaResultSet) qr.getResultSet()).getColumnMetadata());
    jrs.setRows(jrows);
    JsonQueryResult jqr =
        new JsonQueryResult(jrs, qr.getResultPage(), qr.isCatalogChanged(), qr.getCurrentCatalog(),
            qr.isLastResultSet());


    return jqr;
  }

  public static String serializeObjectToString(Object object) throws Exception {

    ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
    GZIPOutputStream gzipOutputStream = new GZIPOutputStream(arrayOutputStream);
    ObjectOutputStream objectOutputStream = new ObjectOutputStream(gzipOutputStream);

    objectOutputStream.writeObject(object);

    objectOutputStream.flush();
    gzipOutputStream.close();
    arrayOutputStream.close();
    objectOutputStream.close();
    String objectString = new String(base64.encode(arrayOutputStream.toByteArray()));

    return objectString;
  }
}
