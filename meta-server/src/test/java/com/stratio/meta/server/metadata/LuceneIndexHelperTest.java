package com.stratio.meta.server.metadata;

import com.stratio.meta.core.metadata.IndexType;
import com.stratio.meta.core.metadata.LuceneIndexHelper;
import com.stratio.meta.core.metadata.CustomIndexMetadata;
import com.stratio.meta.core.metadata.CustomIndexMetadata;
import com.stratio.meta.core.metadata.IndexType;
import com.stratio.meta.core.metadata.LuceneIndexHelper;
import com.stratio.meta.server.cassandra.BasicCassandraTest;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

/**
 * Created by dhiguero on 3/17/14.
 */
public class LuceneIndexHelperTest extends BasicCassandraTest {

    @BeforeClass
    public static void setUpBeforeClass(){
        BasicCassandraTest.setUpBeforeClass();
        loadTestData("demo", "demoKeyspace.cql");
    }

    @Test
    public void processLuceneOptions(){
        String options = "{\"schema\":\"{default_analyzer:\\\"org.apache.lucene.analysis.standard.StandardAnalyzer\\\",fields:{name:{type:\\\"string\\\"}, gender:{type:\\\"string\\\"}, email:{type:\\\"string\\\"}, age:{type:\\\"integer\\\"}, bool:{type:\\\"boolean\\\"}, phrase:{type:\\\"text\\\", analyzer:\\\"org.apache.lucene.analysis.en.EnglishAnalyzer\\\"}}}\",\"refresh_seconds\":\"1\",\"class_name\":\"org.apache.cassandra.db.index.stratio.RowIndex\"}";
        int numColumns = 6;
        LuceneIndexHelper lih = new LuceneIndexHelper();
        Map<String, List<CustomIndexMetadata>> indexes = lih.processLuceneOptions(null, options);
        assertEquals("Invalid number of indexes", numColumns, indexes.size());

        for(Map.Entry<String, List<CustomIndexMetadata>> entry : indexes.entrySet()){
            assertEquals("Column has several indexes", 1, entry.getValue().size());
            assertEquals("Invalid type of index", IndexType.CUSTOM, entry.getValue().get(0).getIndexType());
        }

    }

    @Test
    public void getIndexedColumns(){
        String keyspace = "demo";
        String table = "users";
        String column = "lucene_index_1";
        int numIndexedColumns = 6;
        fail("TODO");
        /*ColumnMetadata cm = MetaDriver.getClusterMetadata()
                .getKeyspace(keyspace)
                .getTable(table)
                .getColumn(column);
        assertNotNull("Cannot retrieve test column", cm);
        LuceneIndexHelper lih = new LuceneIndexHelper();
        Map<String, List<CustomIndexMetadata>> indexedColumns = lih.getIndexedColumns(cm);
        assertEquals("Invalid number of indexes", numIndexedColumns, indexedColumns.size());
        */
    }

}
